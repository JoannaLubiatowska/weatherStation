package notifier;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class NotifierJob implements Job {

    private final NotifierService notifierService = NotifierService.getInstance();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Notifier job executed at {}", context.getFireTime());
            notifierService.notifyLastConditionBefore(context.getFireTime());
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
