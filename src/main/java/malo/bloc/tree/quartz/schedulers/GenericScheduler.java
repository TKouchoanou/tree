package malo.bloc.tree.quartz.schedulers;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public  abstract class GenericScheduler {
    @Autowired
    private Scheduler scheduler;
    abstract protected Class <? extends QuartzJobBean> getQuartzJobClass();
    abstract protected void setTriggerRecurrence(TriggerBuilder<? extends Trigger> triggerBuilder);
    abstract protected long secondBetweenTwoFire();

    /**
     * initialise la plannification des tâches
     */
    @EventListener()
    public void initJobScheduling(ApplicationReadyEvent applicationReadyEvent) throws SchedulerException {
        // ajoute le job au store du scheduleur
        storeJob();
        if(needOneShotInit())
            scheduleOneShotNow(); //planifie une exécution pour maintenant
        if(needRecurrentShot())
            scheduleRecurrent(this.getRecurrentTriggerKey(),this.getJobKey());//planifie une exécution réccurente
    }

    protected boolean needRecurrentShot(){
        return false;
    }
    protected boolean needOneShotInit(){
        return false;
    }
     protected TriggerKey getRecurrentTriggerKey() {
        return new TriggerKey(getQuartzJobClass().getSimpleName());
    }

    protected JobKey getJobKey(){
        return new JobKey(this.getQuartzJobClass().getSimpleName());
    }

    protected JobDetail getJobDetail(JobKey jobKey){
        return JobBuilder.newJob(this.getQuartzJobClass())
                .withIdentity(jobKey)
                .storeDurably()
                .build();
    }

    public void scheduleOneShotNow() throws SchedulerException {
        JobKey jobKey=this.getJobKey();
        Optional<? extends Trigger> nextJobTrigger= this.nextTrigger(jobKey);
        if(isNotCurrentlyRunning(jobKey)){
            if(nextJobTrigger.isPresent() && isRespectDelayBetweenTwoFire(nextJobTrigger.get(),Instant.now(),this.secondBetweenTwoFire()))
                this.scheduleOneShotNow(jobKey);
            else if(!nextJobTrigger.isPresent())
                this.scheduleOneShotNow(jobKey);
        }
    }
    public void scheduleOneShotIn(long delay, ChronoUnit unit) throws SchedulerException {
        JobKey jobKey=this.getJobKey();
        Optional<? extends Trigger> nextJobTrigger= this.nextTrigger(jobKey);
        if(isNotCurrentlyRunning(jobKey)){
            if(nextJobTrigger.isPresent() && isRespectDelayBetweenTwoFire(nextJobTrigger.get(),Instant.now(),this.secondBetweenTwoFire()))
                this.scheduleOneShotIn(delay,unit,jobKey);
            else if(!nextJobTrigger.isPresent())
                this.scheduleOneShotIn(delay,unit,jobKey);
        }
    }


    private void scheduleOneShotNow(JobKey jobKey) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .forJob(jobKey)
                .build();
        scheduler.scheduleJob(trigger);
    }

    private void scheduleOneShotIn(long delay, ChronoUnit unit,JobKey jobKey) throws SchedulerException {
        Date startTime= Date.from(Instant.now().plus(delay,unit));
        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .forJob(jobKey)
                .build();
        scheduler.scheduleJob(trigger);
    }

    /**
     * planifie une execution recurrent dont la récurrence doit être renseingné
     * dans setTriggerRecurrence par la classe qui étend cette classe à qui on envoie
     * le builder déja initialisé et il a juste à renseigné la récurrence
     */
    private void scheduleRecurrent(TriggerKey triggerKey, JobKey jobKey) throws SchedulerException {
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        if (oldTrigger != null) {
            TriggerBuilder<? extends Trigger> triggerBuilder = oldTrigger.getTriggerBuilder();
            this.setTriggerRecurrence(triggerBuilder);
            scheduler.rescheduleJob(triggerKey, triggerBuilder.build());
        }else {
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey);
            this.setTriggerRecurrence(triggerBuilder);
            scheduler.scheduleJob(triggerBuilder.build());
        }
    }


    private boolean isCurrentlyRunning(JobKey jobKey) throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs()
                .stream().
                anyMatch(jobContext->jobContext.getJobDetail().getKey().equals(jobKey));
    }
    private boolean isNotCurrentlyRunning(JobKey jobKey) throws SchedulerException {
        return !isCurrentlyRunning(jobKey);
    }

    /**
     *
     * si on veut planifier un job pour 14 h 30 et que le délais entre deux planification de ce job est de 30 minutes entre il faut vérifier
     *  que rien ne se passera entre 14h et 15 h et donc que la futur exécution du job sera 30 minutes avant notre exécution ou 30 minutes après.
     */
    private boolean isRespectDelayBetweenTwoFire(Trigger nextTrigger, Instant excepted, long delayInSecond)  {
        return !( nextTrigger.getNextFireTime().toInstant().minusSeconds(delayInSecond).isBefore(excepted)
                && nextTrigger.getNextFireTime().toInstant().plusSeconds(delayInSecond).isAfter(excepted));
    }

    /**
     * le comparateur par défaut tri par getNextFireTime, par priorité et par clé
     */
    private Optional<? extends Trigger> nextTrigger(JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey).stream().sorted().findFirst();
    }

    private void storeJob() throws SchedulerException {
        JobDetail job = this.getJobDetail(this.getJobKey());
        scheduler.addJob(job,true);
    }
}
