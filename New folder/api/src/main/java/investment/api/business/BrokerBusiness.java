package investment.api.business;

import investment.api.dtos.BrokerDto;
import investment.api.dtos.UserDto;
import investment.api.repositories.BrokerRepository;
import investment.api.repositories.entities.Broker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerBusiness {

    private final BrokerRepository brokerRepository;


    public BrokerBusiness(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    public ResponseEntity<List<BrokerDto>> getAllBrokers() {
        // Find all brokers in the DB.
        var brokers = brokerRepository.findAll();

        // Parse all brokers to BrokerDtos.
        var brokerDtos = brokers.stream().map(br -> new BrokerDto(br.getId(), br.getUsername(), br.getCompany())).toList();

        // Return the BrokerDtos and a 200 response.
        return new ResponseEntity<>(brokerDtos, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteBroker(Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Check if the broker exists in the database.
        if(!brokerRepository.existsById(user.getBroker().getId())) {
            // Return a 400 response and error message.
            return new ResponseEntity<>("Broker doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Delete the currently logged in broker from the DB.
        brokerRepository.deleteById(user.getBroker().getId());

        // Return a 200 response.
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
