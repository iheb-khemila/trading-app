package investment.api.repositories;

import investment.api.repositories.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    List<Asset> findAssetsByBroker_id(int broker_id);
}
