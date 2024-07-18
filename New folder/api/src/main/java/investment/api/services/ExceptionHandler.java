package investment.api.services;

import investment.api.enums.AssetKindEnum;
import investment.api.enums.UserTypeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleConversionFailedException(final RuntimeException e) {
        // Check if exception contains AssetKindEnum.
        if (e.getCause().getMessage().contains(AssetKindEnum.class.getName())) {
            // Return a 400 response and error message.

            return new ResponseEntity<>("The Asset Kind provided is not valid Asset type. Valid Asset types are: SHARE or BOND", HttpStatus.BAD_REQUEST);
        }
        // Check if exception contains UserTypeEnum.
        else if (e.getCause().getMessage().contains(UserTypeEnum.class.getName())) {
            // Return a 400 response and error message.
            return new ResponseEntity<>("The User Type provided is not valid User Type. Valid User Types are: INVESTOR or BROKER", HttpStatus.BAD_REQUEST);
        } else {
            // Return a 400 response and error message.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
