package investment.api.security;

import investment.api.dtos.UserDto;
import investment.api.repositories.BrokerRepository;
import investment.api.repositories.InvestorRepository;
import investment.api.repositories.entities.Broker;
import investment.api.repositories.entities.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private BrokerRepository brokerRepository;

    @Autowired
    private InvestorRepository InvestorRepository;

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {

        // Split the username and role.
        var name = username.split(":")[0];
        var userType = username.split(":")[1];

        if(Objects.equals(userType, "BROKER")) {
            // Get the broker using the name
            Broker broker = brokerRepository.findByUsername(name);

            // Add the broker role to the users authorities.
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_BROKER"));

            // Return user.
            return new UserDto(broker.getUsername(), broker, formatPassword(broker.getPasswordHash()), broker.getPasswordSalt(), authorities);
        }
        else {
            // Get the investor using the name.
            Investor investor = InvestorRepository.findByUsername(name);

            // Add the investor role to the users authorities.
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR"));

            // Return user.
            return new UserDto(investor.getUsername(), investor, formatPassword(investor.getPasswordHash()), investor.getPasswordSalt(), authorities);
        }
    }

    // Removes any extra bytes added by the DB.
    private byte[] formatPassword(byte[] password) {
        int rlen = password.length;
        while (password[rlen-1] == 0) {
            --rlen;
        }

        return Arrays.copyOf(password, rlen);
    }
}