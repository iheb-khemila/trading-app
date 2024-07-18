package investment.api.security;

import investment.api.dtos.HashedPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CustomPasswordEncoder {

    @Autowired
    private SecureRandom secureRandom;

    @Autowired
    private MessageDigest messageDigest;

    public HashedPasswordDto hashPassword(String passwordToHash, Optional<byte[]> passwordSalt) {
        // Create a new Salt byte array.
        byte[] salt = new byte[32];

        // Assign password salt if its passed in, if not make a new one.
        if(passwordSalt.isPresent()) {
            salt = passwordSalt.get();
        } else{
            secureRandom.nextBytes(salt);
        }

        // Pass the salt to the encryption algorithm.
        messageDigest.update(salt);

        // Hash the password using the salt and return it along with the salt.
        return new HashedPasswordDto(messageDigest.digest(passwordToHash.getBytes(StandardCharsets.UTF_8)), salt);
    }

    public boolean matches(byte[] hashedPassword, byte[] hashedPasswordToCompare) {
        return Arrays.equals(hashedPassword, hashedPasswordToCompare);
    }
}
