package investment.api.business;

import investment.api.dtos.AssetDto;
import investment.api.enums.AssetKindEnum;
import investment.api.dtos.UserDto;
import investment.api.repositories.AssetRepository;
import investment.api.repositories.entities.Asset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetBusiness {

    private final AssetRepository assetRepository;

    public AssetBusiness(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public ResponseEntity<List<AssetDto>> getAssets(Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Call the repository method to find assets by brokerID and parse to a List of AssetDtos.
        var assets = assetRepository.findAssetsByBroker_id(user.getBroker().getId())
                .stream()
                .map(asset ->
                        new AssetDto(asset.getAsset_id(),
                                asset.getBroker().getId(),
                                AssetKindEnum.valueOf(asset.getKind().toUpperCase()),
                                asset.getName()
                        )
                );

        // Return the AssetDtos with a 200 response.
        return new ResponseEntity<>(assets.toList(), HttpStatus.OK);
    }

    public ResponseEntity<List<AssetDto>> getAssetsByBrokerId(int id) {
        // Call the repository method to find assets by brokerID.
        var assets = assetRepository.findAssetsByBroker_id(id);

        // Parse the Assets to AssetDtos.
        var assetDto = assets.stream().map(asset -> new AssetDto(asset.getAsset_id(), asset.getBroker().getId(), AssetKindEnum.valueOf(asset.getKind().toUpperCase()), asset.getName())).toList();

        // Return the AssetDtos with a 200 response.
        return new ResponseEntity<>(assetDto, HttpStatus.OK);
    }

    public ResponseEntity createAsset(AssetDto asset, Authentication authentication) {
        // Get the User from the Authentication object.
        UserDto user = (UserDto) authentication.getPrincipal();

        // Create an Asset DB object and save to the database.
        assetRepository.save(new Asset(user.getBroker(), asset.getKind(), asset.getName()));

        // Return a 201 response.
        return new ResponseEntity<>("Asset created", HttpStatus.CREATED);
    }

    public ResponseEntity deleteAsset(int id) {
        // Check to see if the asset exists in the DB.
        if(!assetRepository.existsById(id)) {
            // Return a 400 response with an error message.
            return new ResponseEntity<>("Asset doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        // Find the asset by the ID passed in.
        var asset = assetRepository.findById(id);

        // Check if the asset belongs to any portfolios.
        if(!asset.get().getPortfolios().isEmpty()) {
            // Return a 400 response with an error message.
            return new ResponseEntity<>("Cannot delete Asset as it belongs to a Portfolio.", HttpStatus.BAD_REQUEST);
        }

        // Delete the Asset.
        assetRepository.deleteById(id);

        // Return a 200 Response.
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
