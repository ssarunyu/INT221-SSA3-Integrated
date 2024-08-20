package int221.sit.taskboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotCreatedException extends ResponseStatusException {
        public NotCreatedException(String message){
            super(HttpStatus.UNAUTHORIZED, message);
        }
}
