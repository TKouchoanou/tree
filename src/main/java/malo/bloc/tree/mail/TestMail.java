package malo.bloc.tree.mail;

import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Builder
public class TestMail extends GenericMail<TestDto>{

    @Override
    protected void setDefaults(org.thymeleaf.context.Context context) {
        TestDto dto= new TestDto();
        context.setVariable("name",dto.name);
        context.setVariable("subscriptionDate",dto.subscriptionDate);
        context.setVariable("hobbies",dto.hobbies);
        context.setVariable("imageResourceName",dto.imageResourceName);
    }

    @Override
    protected String getFragment() {
        return "mail/test";
    }

    @Override
    protected List<Image> getCustomImage() {
        List<Image> images= new ArrayList<>() ;
         images.add(GenericMail.imageBuilder()
                 .name("image")
                 .path("/static/images/mail/index.png")
                 .type("image/png")
                 .build());
         return images;
    }
}
