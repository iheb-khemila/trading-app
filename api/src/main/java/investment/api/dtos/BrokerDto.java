package investment.api.dtos;

import lombok.Data;

@Data
public class BrokerDto {
    private int id;
    private String username;
    private String company;

    public BrokerDto(int id, String username, String company) {
        this.id = id;
        this.username = username;
        this.company = company;
    }
}
