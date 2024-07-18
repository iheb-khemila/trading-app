package investment.api.business;

import investment.api.dtos.HashedPasswordDto;
import investment.api.dtos.LoginDto;
import investment.api.dtos.RegisterBrokerDto;
import investment.api.dtos.RegisterInvestorDto;
import investment.api.repositories.BrokerRepository;
import investment.api.repositories.InvestorRepository;
import investment.api.repositories.entities.Broker;
import investment.api.repositories.entities.Investor;
import investment.api.security.CustomPasswordEncoder;
import investment.api.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationBusiness {

    private final BrokerRepository brokerRepository;
    private final InvestorRepository investorRepository;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenService;

    public AuthenticationBusiness(BrokerRepository brokerRepository, InvestorRepository investorRepository, CustomPasswordEncoder customPasswordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenService) {
        this.brokerRepository = brokerRepository;
        this.investorRepository = investorRepository;
        this.customPasswordEncoder = customPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<String> registerBroker(RegisterBrokerDto brokerDto) {
        // Check if a Broker has been registered with the supplied username.
        if(brokerRepository.existsByUsername(brokerDto.getUsername())) {
            // Return a 400 response with an error message.
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Hash the password using the custom password encoder.
        HashedPasswordDto hashedPasswordDto = customPasswordEncoder.hashPassword(brokerDto.getPassword(), java.util.Optional.empty());

        // Create the new Broker to save in the DB.
        Broker broker = new Broker(brokerDto.getUsername(), brokerDto.getCompany(), hashedPasswordDto.getHashedPassword(), hashedPasswordDto.getSalt());

        // Save the broker into the DB.
        brokerRepository.save(broker);

        // Return a 200 response.
        return new ResponseEntity<>("Broker registered successfully!", HttpStatus.OK);
    }

    public ResponseEntity<String> registerInvestor(RegisterInvestorDto investorDto) {
        // Check if an Investor has been registered with the supplied username.
        if(investorRepository.existsByUsername(investorDto.getUsername())) {
            // Return a 400 response with an error message.
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        //Hash the password using the custom password encoder.
        HashedPasswordDto hashedPasswordDto = customPasswordEncoder.hashPassword(investorDto.getPassword(), java.util.Optional.empty());

        // Create the new Investor to save in the DB.
        Investor investor = new Investor(investorDto.getUsername(), investorDto.getFirstName(), investorDto.getLastName(), hashedPasswordDto.getHashedPassword(), hashedPasswordDto.getSalt());

        // Save the investor into the DB.
        investorRepository.save(investor);

        // Return a 200 response.
        return new ResponseEntity<>("Investor registered successfully!", HttpStatus.OK);
    }

    public ResponseEntity<String> login(LoginDto userDetails) {
        try {
            // Try to authenticate the user using the custom authentication manager.
            Authentication authentication = authenticationManager.authenticate(
                    // Appends the role to the User to determine what type of user is trying to authenticate.
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername() + ":" + userDetails.getUserType().toString(), userDetails.getPassword()));

            // Return a JWT Token and a 200 response.
            return new ResponseEntity<>(tokenService.generateToken(authentication), HttpStatus.OK);
        } catch(BadCredentialsException e) {
            // Handle any errors which result from a wrong username or password.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Handle any other exceptions.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
