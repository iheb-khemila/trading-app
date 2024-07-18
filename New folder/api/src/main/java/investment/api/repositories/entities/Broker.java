package investment.api.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collection;

@Entity
public class Broker {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "broker_id")
    private int id;

    @Getter
    private String username;

    @Getter
    private String company;

    @Getter
    private byte[] passwordHash;

    @Getter
    private byte[] passwordSalt;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "broker")
    private Collection<Asset> assets;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "broker")
    private Collection<Portfolio> portfolios;

    public Broker() { }
    public Broker(String username, String company, byte[] passwordHash, byte[] passwordSalt) {
        this.username = username;
        this.company = company;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }
}
