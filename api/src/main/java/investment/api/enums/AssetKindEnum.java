package investment.api.enums;

public enum AssetKindEnum {

    SHARE("Share"),
    BOND("Bond");

    public final String assetKind;
    AssetKindEnum(String assetKind){
        this.assetKind = assetKind;
    }
}
