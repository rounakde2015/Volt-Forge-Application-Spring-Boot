package com.voltforge.app.exception;

import com.voltforge.app.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorResponse = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errorResponse.put(fieldName, message);
        });

        return new ResponseEntity<Map<String, String>>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException e) {
        String errorMessage = e.getMessage();

        APIResponse apiResponse = new APIResponse(errorMessage, false);

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> apiException(APIException e) {
        String errorMessage = e.getMessage();

        APIResponse apiResponse = new APIResponse(errorMessage, false);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
