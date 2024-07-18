package investment.api.repositories;

import investment.api.repositories.entities.Broker;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Integer> {
    Broker findByUsername(String username);
    Broker findById(int id);
    Boolean existsByUsername(String username);
    Boolean existsById(int id);
}
