package malo.bloc.tree.dtos.error;

import lombok.*;
import lombok.experimental.Accessors;
import malo.bloc.tree.exceptions.exceptions.ErrorCode;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto implements ErrorDtoInterface {
    private  String message;
    private  int httpStatus;
    private ErrorCode errorCode;


}
