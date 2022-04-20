package malo.bloc.tree.exceptions.exceptions;

import lombok.Getter;

import java.security.acl.NotOwnerException;

public class NotResourceOwnerException extends NotOwnerException {
   @Getter
    private ErrorCode errorCode=ErrorCode.NOT_RESOURCE_OWNER;
   private  String message;

    public NotResourceOwnerException(String message){
        this.message=message;
    }
    public NotResourceOwnerException(){
        this.message="The Principal attempting the modification is not an owner.";
    }

    public NotResourceOwnerException(String message,ErrorCode errorCode){
        this(message);
        this.errorCode=errorCode;
    }
}
