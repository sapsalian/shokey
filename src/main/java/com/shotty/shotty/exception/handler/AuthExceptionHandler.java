package com.shotty.shotty.exception.handler;

import com.shotty.shotty.dto.common.ResponseDto;
import com.shotty.shotty.exception.custom_exception.user.UserIdDuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.merge(fieldName, errorMessage, (existingMessage, newMessage) -> existingMessage + "\n" + newMessage);
        });

        ResponseDto responseDto = new ResponseDto(
                (short)4000,
                "요청 필드가 유효하지 않습니다.",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(UserIdDuplicateException.class)
    public ResponseEntity<ResponseDto> handleUserIdDuplicateException(UserIdDuplicateException exception) {
        ResponseDto responseDto = new ResponseDto(
                (short)4090,
                "이미 존재하는 사용자입니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDto);
    }
}
