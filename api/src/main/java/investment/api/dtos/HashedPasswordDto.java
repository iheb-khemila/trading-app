package investment.api.dtos;

import lombok.Data;

@Data
public class HashedPasswordDto {

    public HashedPasswordDto(byte[] hashedPassword, byte[] salt) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    private byte[] hashedPassword;

    private byte[] salt;

}
