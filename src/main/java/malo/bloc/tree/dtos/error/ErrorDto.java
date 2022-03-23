package malo.bloc.tree.dtos.error;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto implements ErrorDtoInterface {
    private String message;
}
