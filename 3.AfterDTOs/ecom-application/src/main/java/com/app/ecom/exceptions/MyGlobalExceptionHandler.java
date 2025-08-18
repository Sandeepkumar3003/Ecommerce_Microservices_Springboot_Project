package com.app.ecom.exceptions;

//import org.springframework.validation.FieldError;

import com.app.ecom.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice

public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName,message);

        });
        return new ResponseEntity<Map<String,String>>(response,
                HttpStatus.BAD_REQUEST);
    }



//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e){
//        String message = e.getMessage();
//        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
//
//    }


//
//    @ExceptionHandler(APIExceptions.class)
//    public ResponseEntity<String> myAPIExceptions(APIExceptions e){
//        String message = e.getMessage();
//        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
//
//    }


//    instead of above two methods we can write as below also

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);

    }



    @ExceptionHandler(APIExceptions.class)
    public ResponseEntity<APIResponse> myAPIExceptions(APIExceptions e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

    }

}
