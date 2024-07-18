package investment.api.dtos;

public class AuthenticationResponseDto {

    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthenticationResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
