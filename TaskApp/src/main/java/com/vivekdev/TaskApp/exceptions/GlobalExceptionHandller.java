package com.vivekdev.TaskApp.exceptions;

import com.vivekdev.TaskApp.dto.Response;
import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Builder
public class GlobalExceptionHandller {

    //If no Specified exception occurs then this is will Be intercepted
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllUnknownException(Exception ex){
        Response<?> response=Response.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Not Found Exception will be Triggered
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex){
        Response<?> response=Response.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    //Bad Request Exception will be Triggered
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleBadRequestException(BadRequestException ex){
        Response<?> response=Response.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> notBlankException(MethodArgumentNotValidException exception){

        Map<String, String> map=new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(err->{
            String fieldName= ((FieldError)err).getField();
            String   message=err.getDefaultMessage();
            map.put(fieldName,message);
        });
     String message=  map.values().stream().findFirst().orElse("Validation Error");
        Response<?> response=Response.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
