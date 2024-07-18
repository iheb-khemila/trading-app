package investment.api.repositories;

import investment.api.repositories.entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer> {
    Investor findByUsername(String username);
    Boolean existsByUsername(String username);
}
