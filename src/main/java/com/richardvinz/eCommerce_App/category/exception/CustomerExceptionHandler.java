package com.richardvinz.eCommerce_App.category.exception;

import com.richardvinz.eCommerce_App.category.config.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err->{
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
        response.put(fieldName,message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse>resourceNotFoundExceptionHandler(ResourceNotFoundException e){
        return buildResponse(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse> ApiExceptionHandler(APIException e){
        return buildResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiResponse> buildResponse(String message, HttpStatus status){
        ApiResponse response = new ApiResponse(message,false);
    return new ResponseEntity<>(response, status);
    }
}
