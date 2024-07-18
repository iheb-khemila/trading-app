package investment.api.dtos;

import investment.api.repositories.entities.Broker;
import investment.api.repositories.entities.Investor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class UserDto extends User {

    @Getter
    private Broker broker;

    @Getter
    private Investor investor;

    @Getter
    private byte[] passwordHash;

    @Getter
    private byte[] salt;

    public UserDto(String username, Broker broker, byte[] password, byte[] salt, Collection<? extends GrantedAuthority> authorities) {
        super(username, Arrays.toString(password), authorities);

        this.broker = broker;
        this.passwordHash = password;
        this.salt = salt;
    }

    public UserDto(String username, Investor investor, byte[] password, byte[] salt, Collection<? extends GrantedAuthority> authorities) {
        super(username, Arrays.toString(password), authorities);
        this.investor = investor;
        this.passwordHash = password;
        this.salt = salt;
    }
}
