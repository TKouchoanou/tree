package malo.bloc.tree.quartz.schedulers;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.quartz.jobs.UserJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import java.time.temporal.ChronoUnit;

@Component
@Log4j2
public class UserScheduler extends GenericScheduler {
    @Override
    protected Class<? extends QuartzJobBean> getQuartzJobClass() {
        return UserJob.class;
    }

    @SneakyThrows
    @PostPersist
    @PostUpdate
    public void onUserPersist(User user){
        this.scheduleOneShotIn(10,ChronoUnit.SECONDS);
        log.info(" \n [USER PERSIST OR UPDATE LISTENER ] About update of user: " + user.getId());
    }
    
    @Override
    protected void setTriggerRecurrence(TriggerBuilder triggerBuilder) {
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 21-23 ? * THU-FRI"));
    }
    protected boolean needRecurrentShot(){
        return true;
    }
    protected boolean needOneShotInit(){
        return true;
    }



    @Override
    protected long secondBetweenTwoFire() {
        return 30;
    }
    //https://blog.zenika.com/2013/09/04/un-peu-plus-loin-avec-quartz/
    //https://stackabuse.com/guide-to-quartz-with-spring-boot-job-scheduling-and-automation/
    //https://www.baeldung.com/spring-quartz-schedule
    //https://www.baeldung.com/spring-quartz-schedule
    //https://github.com/eugenp/tutorials/tree/master/spring-quartz
    //cron trigger http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html

    //resolver d'argument générique '
    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/GenericTypeResolver.html
    //https://www.tabnine.com/code/java/methods/org.springframework.core.GenericTypeResolver/resolveTypeArguments
    //https://www.geeksforgeeks.org/class-getcomponenttype-method-in-java-with-examples/
   // http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html

    /**
     * resolution d'injection de dépendance quand  on a deux beans qui implemente la même interface
     * https://www.baeldung.com/spring-primary
     * https://freecontent.manning.com/choosing-from-multiple-beans-in-the-context/
     * https://stackoverflow.com/questions/47262363/consider-marking-one-of-the-beans-as-primary
     * https://stackoverflow.com/questions/53139244/spring-boot-2-1-bean-override-vs-primary
     * Appronfondire l(injection de dependance spring , manipuler le contexte configurer pour
     * injecter les bon bean avec le bon token d'unjection. J'ai eu un soucis d'identifiant d'injection avec
     * en nommant deux deux interfaces par le même nom alors qu'ils sont dans différent package. Pourquoi ?
     * L'Erreur est : defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration,
     * could not be registered. A bean with that name has already been defined
     */

    /**
     * pagging
     * https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/
     * https://www.baeldung.com/spring-data-jpa-pagination-sorting
     * https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html
     */
    /**
     * SPRING event
     * https://www.baeldung.com/spring-events
     * https://www.baeldung.com/spring-async#enable-async-support
     * https://reflectoring.io/spring-boot-application-events-explained/
     * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationEventPublisher.html#publishEvent-java.lang.Object-
     * https://www.baeldung.com/spring-expression-language
     * https://www.baeldung.com/spring-context-events
     */

    /**
     * load properties
     * https://www.baeldung.com/spring-boot-json-properties
     */
    /**
     * reading file
     * https://www.baeldung.com/spring-classpath-file-access
     * https://www.baeldung.com/reading-file-in-java
     */

}
