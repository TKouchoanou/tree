package malo.bloc.tree.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import malo.bloc.tree.dtos.error.ErrorDto;
import malo.bloc.tree.exceptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotResourceOwnerException.class)
    public  ResponseEntity<ErrorDto> handleException(NotResourceOwnerException ex) {

      final ErrorDto errorDto = ErrorDto.builder()
              .httpStatus(HttpStatus.UNAUTHORIZED.value())
              .message(ex.getMessage())
              .errorCode(ex.getErrorCode())
              .build();

      return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .contentType(MediaType.APPLICATION_JSON).
              body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public  ResponseEntity<ErrorDto> handleException(EntityNotFoundException ex) {

        final ErrorDto errorDto = ErrorDto.builder()
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).
                body(errorDto);
    }

    @ExceptionHandler({UnSupportedFormatException.class,HomeException.class})
    public  ResponseEntity<ErrorDto> handleException(UnSupportedFormatException ex) {

        final ErrorDto errorDto = ErrorDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .build();

        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidEntityException.class})
    public  ResponseEntity<ErrorDto> handleException(InvalidEntityException ex) {

        final ErrorDto errorDto = ErrorDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .errorCode(ex.getErrorCode())
                .message(ex.getErrors().stream().map(er->er.getField()+ " "+er.getDefaultMessage()).collect(Collectors.joining(";")))
                .build();
        log.info(ex.getErrors().toString());
        ex.printStackTrace();

        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class,NullPointerException.class})
    public  ResponseEntity<ErrorDto> handleException(RuntimeException ex) {

        final ErrorDto errorDto = ErrorDto.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCode.UNKNOW_ERROR_EXCEPTION)
                .build();
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON).
                body(errorDto);
    }
}
