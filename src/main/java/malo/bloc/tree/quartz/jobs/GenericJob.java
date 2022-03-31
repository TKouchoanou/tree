package malo.bloc.tree.quartz.jobs;

import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.Instant;
import java.time.LocalDateTime;

@Log4j2
public abstract class GenericJob extends  org.springframework.scheduling.quartz.QuartzJobBean{
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Instant start= Instant.now();
            log.info(" \n Started {} at {}",getJobDescription(), LocalDateTime.now());
            this.executeJob(context);
            Instant end= Instant.now();
            long nbSecond=(end.getEpochSecond()- start.getEpochSecond());
            log.info(" Ended to process  {} in {} seconds \n",getJobDescription(),nbSecond);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     abstract void executeJob (JobExecutionContext context) throws Exception;
     abstract String getJobDescription ();

}
