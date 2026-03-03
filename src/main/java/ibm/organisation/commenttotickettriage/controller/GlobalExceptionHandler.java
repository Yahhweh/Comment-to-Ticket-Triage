package ibm.organisation.commenttotickettriage.controller;

import ibm.organisation.commenttotickettriage.service.ServiceException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleFieldException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();
        exp
            .getBindingResult()
            .getFieldErrors()
            .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<String> handleServiceException(ServiceException exp){
        String error = exp.getMessage();
        return ResponseEntity.internalServerError().body(error);
    }
}
