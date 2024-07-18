package investment.api.dtos;

import lombok.Data;

@Data
public class RegisterBrokerDto extends LoginDto {
    private String company;
}
