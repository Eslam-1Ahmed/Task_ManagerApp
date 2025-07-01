package com.example.taskmanager.execptions;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.taskmanager.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(TaskNotFoundException.class)
   public ResponseEntity<ApiResponse<?>> handleTaskNotFound(TaskNotFoundException ex) {
      ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException ex) {
      ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   @ExceptionHandler(ForbiddenException.class)
   public ResponseEntity<ApiResponse<?>> handleForbidden(ForbiddenException ex) {
      ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   @ExceptionHandler(ProjectNotFoundException.class)
   public ResponseEntity<ApiResponse<?>> handleProjectNotFound(ProjectNotFoundException ex) {
      ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity<ApiResponse<?>> handleEnumConversionError(HttpMessageNotReadableException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof InvalidFormatException e && e.getTargetType().isEnum()) {
         Class<?> enumClass = e.getTargetType();
         String allowedValues = Arrays.toString(enumClass.getEnumConstants());
         return ResponseEntity.badRequest().body(new ApiResponse<>(false, "",
               Map.of("error", "Invalid value. Allowed values: " + allowedValues)));
      }

      return ResponseEntity.badRequest().body(new ApiResponse<>(false, "", Map.of("error", "Malformed request")));
   }
}
