package one.digitalinnovation.sodastock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SodaNotFoundException extends Exception {

    public SodaNotFoundException (String name) {
        super(String.format("Soda with name %s not found in system.", name));
    }

    public SodaNotFoundException (Long id) {
        super(String.format("Soda with id %s not found in system.", id));
    }
}
