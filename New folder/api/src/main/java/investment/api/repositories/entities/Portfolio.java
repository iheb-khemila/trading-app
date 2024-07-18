package investment.api.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Entity
public class Portfolio {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "broker_id")
    @Setter
    @Getter
    private Broker broker;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    @Setter
    @Getter
    private Investor investor;

    @ManyToMany
    @JoinTable(
            name = "portfolio_assets",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_id")
    )
    @Getter
    @Setter
    private Collection<Asset> portfolioAssets;

    @Getter
    @Setter
    private LocalDateTime creation_date;

    public Portfolio(Broker broker, Investor investor, Collection<Asset> portfolioAssets, LocalDateTime creation_date) {
        this.broker = broker;
        this.investor = investor;
        this.portfolioAssets = portfolioAssets;
        this.creation_date = creation_date;
    }

    public Portfolio() {

    }
}
