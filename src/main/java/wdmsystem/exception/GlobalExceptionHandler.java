package wdmsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /// 400 Invalid Input
    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException exception) {
        log.error("Invalid Input Exception: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    /// 401 Unauthorized
    // Mentioned in login authentication api endpoint, but might not be needed
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException exception) {
        log.error("Unauthorized Exception: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

    /// 403 Insufficient Privileges
    @ExceptionHandler({InsufficientPrivilegesException.class})
    public ResponseEntity<Object> handleInsufficientPrivilegesException(InsufficientPrivilegesException exception) {
        log.error("Insufficient Privileges Exception: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }

    /// 404 Not Found
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        log.error("Not Found Exception: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    /// 409 Conflict
    // Used for when a time slot for reservation is already taken
    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<Object> handleConflictException(ConflictException exception) {
        log.error("Conflict Exception: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }
}
