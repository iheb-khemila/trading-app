package investment.api.dtos;

import lombok.Data;

@Data
public class AddAssetToPortfolioDto {

    public int portfolioId;

    public int assetId;
}
