package malo.bloc.tree.exceptions.exceptions;

import lombok.Getter;

public class HomeException  extends RuntimeException{
    @Getter
    private ErrorCode errorCode=ErrorCode.SOME_PROBLEM_OCCUR;

    public HomeException(String message){
        super(message);
    }

    public HomeException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode=errorCode;
    }
    public  HomeException(String message,Throwable cause){
        super(message, cause);
    }

    public HomeException(String message,Throwable cause,ErrorCode errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }
}
