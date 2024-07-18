package investment.api.dtos;

import investment.api.enums.AssetKindEnum;
import lombok.Data;

@Data
public class AssetDto {
    private int asset_id;
    private int broker_id;
    private AssetKindEnum kind;
    private String name;

    public AssetDto(int asset_id, int broker_id, AssetKindEnum kind, String name) {
        this.asset_id = asset_id;
        this.broker_id = broker_id;
        this.kind = kind;
        this.name = name;
    }

    public AssetDto(int broker_id, AssetKindEnum kind, String name) {
        this.broker_id = broker_id;
        this.kind = kind;
        this.name = name;
    }

    public AssetDto() {
    }
}
