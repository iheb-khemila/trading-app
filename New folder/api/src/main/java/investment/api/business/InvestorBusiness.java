package investment.api.business;

import investment.api.dtos.InvestorDto;
import investment.api.dtos.UserDto;
import investment.api.repositories.InvestorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class InvestorBusiness {

    private final InvestorRepository investorRepository;
    private final PortfolioBusiness portfolioBusiness;

    public InvestorBusiness(InvestorRepository investorRepository, PortfolioBusiness portfolioBusiness) {
        this.investorRepository = investorRepository;
        this.portfolioBusiness = portfolioBusiness;
    }

    public ResponseEntity getInvestorProfile(Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Check if the user is an investor.
        if((user.getInvestor() == null)){
            // Return a 400 response and error message.
            return new ResponseEntity<>("Investor doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Get all portfolios owned by the currently logged in user.
        var portfolios = portfolioBusiness.getAllPortfolios(authentication);

        // Parse the user details and portfolios into an InvestorDto
        var investor = new InvestorDto(user.getInvestor().getFirstname(), user.getInvestor().getLastname(), user.getInvestor().getUsername(), portfolios);

        // Return the InvestorDto with a 200 response.
        return new ResponseEntity<>(investor, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteInvestor(Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Check if the investor exists in the DB.
        if(!investorRepository.existsById(user.getInvestor().getId())) {
            return new ResponseEntity<>("Investor doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Delete the currently logged in broker from the DB.
        investorRepository.deleteById(user.getInvestor().getId());

        // Return a 200 response.
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
