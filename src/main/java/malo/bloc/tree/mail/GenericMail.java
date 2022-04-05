package malo.bloc.tree.mail;

import lombok.Builder;
import lombok.Data;

import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;


public abstract class GenericMail <T> {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Data
    @Builder(toBuilder = true)
    @Accessors(chain = true)
    public static class Context{
        //generic pur être unique ou list envoie multiple
        private String from;
        private String to;
        private String subject;
        private List<String> cc;
        private List<String> cci;

        public MimeMessageHelper addToMimeMessageHelper(MimeMessageHelper mimeMessageHelper) throws MessagingException {
            mimeMessageHelper.setFrom(this.from);
            mimeMessageHelper.setTo(this.to);
            mimeMessageHelper.setSubject(this.subject);
            if(this.getCc()!=null)
                mimeMessageHelper.setCc(this.cc.toArray(new String[0]));
            if(this.getCci()!=null)
                mimeMessageHelper.setCc(this.cci.toArray(new String[0]));
            return  mimeMessageHelper;
        }
    }

    @Data
    @Builder(toBuilder = true)
    @Accessors(chain = true)
    public static class Image {
        private String name;
        private String path;
        //de type enum
        private String type;

        private MimeMessageHelper addToMimeMessageHelper(MimeMessageHelper mimeMessageHelper )  {
                try {
                    mimeMessageHelper.addInline(this.name,new ClassPathResource(this.path),this.type);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return  mimeMessageHelper;
        }
    }

    @Data
    @Builder(toBuilder = true)
    @Accessors(chain = true)
    public static class Attachment {
        private String filename;
        private byte[] content;
        //de type enum
        private String type;
        private MimeMessageHelper addAttachments(MimeMessageHelper mimeMessageHelper ) {
                try {
                    mimeMessageHelper.addInline(this.filename, new ByteArrayResource(this.content),this.type);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return mimeMessageHelper;
        }
    }

    private void send(SimpleMailMessage simpleMessage) {
        javaMailSender.send(simpleMessage);
    }

    protected abstract void setDefaults(org.thymeleaf.context.Context context);
    protected abstract String getFragment();

    protected List<Image> getCustomImage() {
        return null; 
    }
    protected List<Attachment> getAttachements() {
        return null;
    }


    public void send(Context context,String message){
       this.send(buildSimplemessage(context,message));
    }

    public SimpleMailMessage buildSimplemessage(Context context,String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(context.getFrom());
        mailMessage.setTo(context.to);
        mailMessage.setSubject(context.getSubject());
        mailMessage.setText(message);
        return mailMessage;
    }

    public MimeMessage buildMail(Context context,String message,List<Image> images,List<Attachment> attachments) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true,"UTF-8");
        mimeMessageHelper.setText(message,true);
        setContext(context,mimeMessageHelper);
       if(images!=null) addImages(images,mimeMessageHelper);
       if(attachments!=null)addAttachments(attachments,mimeMessageHelper);
        return mimeMessage;
    }

    @SneakyThrows
    public void send(@NotNull Context context, T dto,@Nullable Locale locale){
        org.thymeleaf.context.Context ctx= locale==null? new org.thymeleaf.context.Context():new org.thymeleaf.context.Context(locale); 
        ctx.setVariable("dto",dto);
        setDefaults(ctx);
        String htmlContent=templateEngine.process(getFragment(),ctx);
        MimeMessage mail=buildMail(context,htmlContent,getCustomImage(),getAttachements());
        javaMailSender.send(mail);
    }
    @SneakyThrows
    public void send(@NotNull Context context, T dto){
       this.send(context,dto,null);
    }

  


    private void setContext(@NotNull Context context, MimeMessageHelper mimeMessageHelper ) throws MessagingException {
        mimeMessageHelper.setFrom(context.getFrom());
        mimeMessageHelper.setTo(context.getTo());
        mimeMessageHelper.setSubject(context.getSubject());
        if(context.getCc()!=null)
            mimeMessageHelper.setCc(context.getCc().toArray(new String[0]));
        if(context.getCci()!=null)
            mimeMessageHelper.setCc(context.getCci().toArray(new String[0]));
    }

    private void addImages(List<Image> images, MimeMessageHelper mimeMessageHelper )  {
        //un peut de généric pour bite ou classpathRessource
        images.forEach(image -> {
            try {
                mimeMessageHelper.addInline(image.name,new ClassPathResource(image.path),image.type);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        });
    }

    private void addAttachments(List<Attachment> attachments, MimeMessageHelper mimeMessageHelper ) {//un peut de généric pour bite ou classpathRessource
            attachments.forEach(attachment -> {
                try {
                    mimeMessageHelper.addInline(attachment.filename, new ByteArrayResource(attachment.content),attachment.type);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
    }


    public static Context.ContextBuilder contextBuilder(){
        return  Context.builder();
    }

    public static Image.ImageBuilder imageBuilder(){return Image.builder();}

    public static Attachment.AttachmentBuilder attachmentBuilder(){return Attachment.builder();}


}
