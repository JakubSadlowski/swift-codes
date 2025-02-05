package org.js.swiftcodes.api.validation;

import lombok.extern.apachecommons.CommonsLog;
import org.js.swiftcodes.api.model.Error;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@ControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
@CommonsLog
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Error> handleServiceGeneralException(GeneralException ex, WebRequest request) {
        Error response = Error.of("ERROR", ex.getMessage());
        log.warn("Handled ServiceGeneralException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult()
            .getFieldErrors()) {
            errorMessages.add(error.getField() + "=" + error.getDefaultMessage());
        }
        Collections.sort(errorMessages);
        StringBuilder incorrectFieldsInfo = new StringBuilder();
        for (String errorMessage : errorMessages) {
            if (!incorrectFieldsInfo.isEmpty())
                incorrectFieldsInfo.append(", ");
            incorrectFieldsInfo.append(errorMessage);
        }

        Error response = Error.of("ARGUMENTS_NOT_VALID", incorrectFieldsInfo.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
}
