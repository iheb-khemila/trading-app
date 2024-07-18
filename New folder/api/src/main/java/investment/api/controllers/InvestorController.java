package investment.api.controllers;

import investment.api.business.InvestorBusiness;
import investment.api.business.PortfolioBusiness;
import investment.api.dtos.AddAssetToPortfolioDto;
import investment.api.dtos.CreatePortfolioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investor/")
public class InvestorController {

    @Autowired
    private InvestorBusiness investorBusiness;

    @Autowired
    private PortfolioBusiness portfolioBusiness;

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @GetMapping("/")
    public ResponseEntity getPortfolios(Authentication authentication) {
        return new ResponseEntity(portfolioBusiness.getAllPortfolios(authentication), HttpStatus.OK);
    }

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @GetMapping("/profile")
    public ResponseEntity getInvestor(Authentication authentication) {
        return investorBusiness.getInvestorProfile(authentication);
    }

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @PostMapping("/create-portfolio")
    public ResponseEntity createPortfolio(@RequestBody CreatePortfolioDto portfolio, Authentication authentication) {
        return portfolioBusiness.createPortfolio(portfolio, authentication);
    }

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @PostMapping("/add-asset")
    public ResponseEntity addAssetToPortfolio(@RequestBody AddAssetToPortfolioDto asset, Authentication authentication) {
        return portfolioBusiness.addAsset(asset, authentication);
    }

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @DeleteMapping("/delete-profile")
    public ResponseEntity deleteInvestor(Authentication authentication) {
        return investorBusiness.deleteInvestor(authentication);
    }

    // Check if user has Investor role
    @PreAuthorize("hasRole('INVESTOR')")
    @DeleteMapping("/sell-portfolio/{id}")
    public ResponseEntity sellPortfolio(@PathVariable int id, Authentication authentication) {
        return portfolioBusiness.sellPortfolio(id, authentication);
    }
}
