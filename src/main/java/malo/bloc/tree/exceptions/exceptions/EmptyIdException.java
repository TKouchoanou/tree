package malo.bloc.tree.exceptions.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public class EmptyIdException extends RuntimeException{
    @Getter
    private ErrorCode errorCode=ErrorCode.EMPTY_ID;
  

    public EmptyIdException (String message){
        super(message);
    }

    public  EmptyIdException (String message,Throwable cause){
        super(message, cause);
    }

    public EmptyIdException (String message,Throwable cause,ErrorCode errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }

    public EmptyIdException (String message,ErrorCode errorCode){
        super(message);
        this.errorCode=errorCode;
    }

}
