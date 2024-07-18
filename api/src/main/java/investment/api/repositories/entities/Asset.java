package investment.api.repositories.entities;

import investment.api.enums.AssetKindEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
public class Asset {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_id;

    @ManyToOne
    @JoinColumn(name = "broker_id")
    @Setter
    @Getter
    private Broker broker;

    @Getter
    private String kind;

    @Getter
    private String name;

    @ManyToMany(mappedBy = "portfolioAssets", fetch = FetchType.EAGER)
    @Getter
    private Collection<Portfolio> portfolios;

    public Asset(Broker broker, AssetKindEnum kind, String name) {
        this.broker = broker;
        this.kind = kind.assetKind;
        this.name = name;
    }

    public Asset() {

    }
}
