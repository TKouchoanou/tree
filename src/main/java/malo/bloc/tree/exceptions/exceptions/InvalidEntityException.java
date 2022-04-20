package malo.bloc.tree.exceptions.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class InvalidEntityException extends  RuntimeException{

    @Getter
    private ErrorCode errorCode=ErrorCode.INVALID_ENTITY;

    @Getter
    @Setter
    List<FieldError> errors= new ArrayList<>();

    public InvalidEntityException(String message){
        super(message);
    }

    public  InvalidEntityException(String message,Throwable cause,List<FieldError> errors){
        super(message, cause);
        this.errors=errors;
    }

    public InvalidEntityException(String message,List<FieldError> errors){
        super(message);
        this.errors=errors;
    }

    public  InvalidEntityException(String message,Throwable cause){
        super(message, cause);
    }



    public InvalidEntityException(String message,Throwable cause,ErrorCode errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }

    public InvalidEntityException(String message,ErrorCode errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    public InvalidEntityException(String message,ErrorCode errorCode,List<FieldError> errors){
        super(message);
        this.errorCode=errorCode;
        this.errors=errors;
    }
}
