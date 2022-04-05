package malo.bloc.tree.mail;

import lombok.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    String name="";
    Date subscriptionDate=new Date();
    List<String> hobbies= Arrays.asList("Cinema", "Sports", "Music");
    String imageResourceName="/static/images/mail/index.png";
}
