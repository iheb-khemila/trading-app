package investment.api.repositories;

import investment.api.repositories.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PortfolioRespository extends JpaRepository<Portfolio, Integer> {
    Collection<Portfolio> findAllByInvestorId(int investorId);
}
