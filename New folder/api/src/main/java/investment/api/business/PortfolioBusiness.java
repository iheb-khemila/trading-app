package investment.api.business;

import investment.api.dtos.*;
import investment.api.enums.AssetKindEnum;
import investment.api.repositories.AssetRepository;
import investment.api.repositories.BrokerRepository;
import investment.api.repositories.InvestorRepository;
import investment.api.repositories.PortfolioRespository;
import investment.api.repositories.entities.Broker;
import investment.api.repositories.entities.Portfolio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class PortfolioBusiness {

    private final PortfolioRespository portfolioRepository;
    private final BrokerRepository brokerRepository;
    private final InvestorRepository investorRepository;
    private final AssetRepository assetRepository;

    public PortfolioBusiness(PortfolioRespository portfolioRepository, BrokerRepository brokerRepository, InvestorRepository investorRepository, AssetRepository assetRepository) {
        this.portfolioRepository = portfolioRepository;
        this.brokerRepository = brokerRepository;
        this.investorRepository = investorRepository;
        this.assetRepository = assetRepository;
    }

    public Collection<PortfolioDto> getAllPortfolios(Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Get all investors from the DB.
        var allInvestors = portfolioRepository.findAllByInvestorId(user.getInvestor().getId());

        // Parse all investors.
        var parsedInvestors = allInvestors.stream().map(portfolio ->
            new PortfolioDto(
                    portfolio.getId(),
                    portfolio.getBroker().getId(),
                    portfolio.getCreation_date(),
                    portfolio.getBroker().getCompany(),
                    portfolio.getPortfolioAssets().stream().map(
                            asset -> new AssetDto(
                                    asset.getAsset_id(),
                                    asset.getBroker().getId(),
                                    AssetKindEnum.valueOf(asset.getKind().toUpperCase()),
                                    asset.getName()
                            )
                    ).toList()
                )
        ).toList();

        // Return all parsed investors.
        return parsedInvestors;
    }

    public ResponseEntity createPortfolio(CreatePortfolioDto portfolio, Authentication authentication) {
        // Check if the broker exists in the DB.
        if(!brokerRepository.existsById(portfolio.getBrokerId())) {
            // Return 400 response and error message.
            return new ResponseEntity("Broker doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Get the broker by ID from the DB.
        Broker broker = brokerRepository.findById(portfolio.getBrokerId());

        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Save the portfolio object to the DB.
        portfolioRepository.save(new Portfolio(broker, user.getInvestor(), new ArrayList<>(), LocalDateTime.now()));

        // Return 201 response.
        return new ResponseEntity("Portfolio Created", HttpStatus.CREATED);
    }

    public ResponseEntity sellPortfolio(int id, Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Check if the investor exists in the DB.
        if(!investorRepository.existsById(user.getInvestor().getId())) {
            // Return 400 response and error message.
            return new ResponseEntity<>("Investor doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Check if the portfolio exists in the DB.
        if(!portfolioRepository.existsById(id)) {
            // Return 400 response and error message.
            return new ResponseEntity<>("Portfolio doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Delete portfolio from the DB.
        portfolioRepository.deleteById(id);

        // Return 200 response.
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity addAsset(AddAssetToPortfolioDto assetToAdd, Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Check if the portfolio exists in the DB.
        if(!portfolioRepository.existsById(assetToAdd.portfolioId)) {
            // Return 400 response and error message.
            return new ResponseEntity<>("Portfolio doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Check if the asset exists in the DB.
        if(!assetRepository.existsById(assetToAdd.assetId)) {
            // Return 400 response and error message.
            return new ResponseEntity<>("Asset doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Get Asset and portfolio from DB.
        var asset = assetRepository.findById(assetToAdd.assetId);
        var portfolio = portfolioRepository.findById(assetToAdd.portfolioId);

        // Check if the broker provides the asset to be added.
        if(asset.get().getBroker().getId() != portfolio.get().getBroker().getId()) {
            // Return 400 response and error message.
            return new ResponseEntity<>("Broker doesn't provide this Asset.", HttpStatus.BAD_REQUEST);
        }

        // Check if the investor has the portfolio.
        if(portfolio.get().getInvestor().getId() != user.getInvestor().getId()) {
            // Return 400 response and error message.
            return new ResponseEntity<>("User doesn't have portfolio with the id " + assetToAdd.assetId, HttpStatus.BAD_REQUEST);
        }

        // Add asset to portfolio.
        portfolio.get().getPortfolioAssets().add(asset.get());

        // Save the portfolio to the DB.
        portfolioRepository.save(portfolio.get());

        // Return 200 response.
        return new ResponseEntity("Asset Added", HttpStatus.OK);
    }
}
