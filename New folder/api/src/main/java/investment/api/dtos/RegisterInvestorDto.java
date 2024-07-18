package investment.api.dtos;

import lombok.Data;

@Data
public class RegisterInvestorDto extends LoginDto {
    private String firstName;

    private String lastName;
}
