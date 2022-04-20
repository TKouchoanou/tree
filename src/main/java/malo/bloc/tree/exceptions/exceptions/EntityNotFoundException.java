package malo.bloc.tree.exceptions.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {
    @Getter
    private ErrorCode errorCode=ErrorCode.ENTITY_NOT_FOUND;

    EntityNotFoundException (String message){
        super(message);
    }

    public EntityNotFoundException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
