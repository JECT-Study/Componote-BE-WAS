package ject.componote.global.error;

import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import ject.componote.infra.error.InfraException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error("예상치 못한 에러입니다. ", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR, "예상하지 못한 에러입니다."));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(final SQLException exception) {
        log.error("SQL 예외 발생. ", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR, "SQL 오류입니다."));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(final DataAccessException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, exception.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of(METHOD_NOT_ALLOWED, exception));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(final NoResourceFoundException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(ErrorResponse.of(BAD_REQUEST, "URI가 잘못되었습니다."));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> handleServerException(final ServletException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, exception));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ErrorResponse> handleWebClientRequestException(final WebClientRequestException exception) {
        log.error("외부 API 호출 실패. ", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR, "외부 API 연결에 실패하였습니다. 외부 API 엔드포인트: " + exception.getUri()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log.warn("잘못된 요청입니다. ", exception);
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, "잘못된 요청입니다."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, "HTTP Request Body 가 잘못되었습니다."));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, String.join(", ", getFieldErrorMessage(exception))));
    }

    @ExceptionHandler(ComponoteException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(final ComponoteException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.of(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(InfraException.class)
    public ResponseEntity<ErrorResponse> handleInfraException(final InfraException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.of(exception.getStatus(), exception.getMessage()));
    }

    private List<String> getFieldErrorMessage(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String message = error.getDefaultMessage();
                    return fieldName + ": " + message;
                })
                .toList();
    }
}
