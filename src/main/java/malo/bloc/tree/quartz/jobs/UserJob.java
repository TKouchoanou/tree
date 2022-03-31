package malo.bloc.tree.quartz.jobs;

import malo.bloc.tree.search.repository.UserRepository;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class UserJob extends GenericJob{
    @Autowired
    private UserRepository userRepository;
    @Override
    void executeJob(JobExecutionContext context){
        userRepository.saveAll();
    }

    @Override
    String getJobDescription() {
        return "Indexation of user";
    }
}
