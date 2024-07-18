package investment.api.dtos;

import investment.api.enums.UserTypeEnum;
import lombok.Data;

@Data
public class LoginDto {
   private String username;
   private String password;
   private UserTypeEnum userType;
}
