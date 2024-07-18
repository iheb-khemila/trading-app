package investment.api.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collection;

@Entity
public class Investor {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investor_id")
    private int id;

    @Getter
    private String username;

    @Getter
    private String firstname;

    @Getter
    private String lastname;

    @Getter
    private byte[] passwordHash;

    @Getter
    private byte[] passwordSalt;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "investor")
    @Getter
    private Collection<Portfolio> portfolios;


    public Investor() {}
    public Investor(String username, String firstname, String lastname, byte[] passwordHash, byte[] passwordSalt) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }
}
