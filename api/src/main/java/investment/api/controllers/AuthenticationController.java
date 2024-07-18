package investment.api.controllers;

import investment.api.business.AuthenticationBusiness;
import investment.api.dtos.LoginDto;
import investment.api.dtos.RegisterBrokerDto;
import investment.api.dtos.RegisterInvestorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationBusiness authenticationBusiness;

    @Autowired
    public AuthenticationController(AuthenticationBusiness authenticationBusiness) {
        this.authenticationBusiness = authenticationBusiness;
    }

    @PostMapping("/register-broker")
    public ResponseEntity<String> registerBroker(@RequestBody RegisterBrokerDto broker) {
       return authenticationBusiness.registerBroker(broker);
    }

    @PostMapping("/register-investor")
    public ResponseEntity<String> registerInvestor(@RequestBody RegisterInvestorDto investor) {
        return authenticationBusiness.registerInvestor(investor);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginBroker(@RequestBody LoginDto userDto) {
        return authenticationBusiness.login(userDto);
    }
}
