package malo.bloc.tree.exceptions.exceptions;

import lombok.Getter;

public class UnSupportedFormatException extends RuntimeException{
    @Getter
    private ErrorCode errorCode=ErrorCode.UNSUPPORTED_FORMAT;

    public UnSupportedFormatException(String message){
        super(message);
    }

    public UnSupportedFormatException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode=errorCode;
    }
    public  UnSupportedFormatException(String message,Throwable cause){
        super(message, cause);
    }

    public UnSupportedFormatException(String message,Throwable cause,ErrorCode errorCode){
        super(message, cause);
        this.errorCode=errorCode;
    }
}
