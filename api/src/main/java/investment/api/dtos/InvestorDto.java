package investment.api.dtos;

import investment.api.repositories.entities.Portfolio;
import lombok.Data;

import java.util.Collection;

@Data
public class InvestorDto {

    private String firstname;

    private String lastname;

    private String username;

    private Collection<PortfolioDto> portfolios;

    public InvestorDto(String firstname, String lastname, String username, Collection<PortfolioDto> portfolios) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.portfolios = portfolios;
    }
}
