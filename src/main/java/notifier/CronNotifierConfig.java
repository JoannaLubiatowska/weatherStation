package notifier;

import controller.DataController;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@SuppressWarnings("unused")
@Slf4j
public class CronNotifierConfig implements ServletContextListener {

    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("contextInitialized() invoked");
        scheduleJob();
        connectWithDatabase();
    }

    private void connectWithDatabase() {
        DataController.init();
    }

    private void scheduleJob() throws SchedulerException {
        JobKey jobKeyA = new JobKey("weatherNotifier1", "weatherNotifier");
        JobDetail jobA = JobBuilder.newJob(NotifierJob.class)
                .withIdentity(jobKeyA).build();

        Trigger trigger1 = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName1", "weatherNotifier")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();

        scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.start();
        scheduler.scheduleJob(jobA, trigger1);
    }

    @SneakyThrows
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("contextDestroyed() invoked");
        scheduler.shutdown();
        DataController.destroyEntityManager();
    }
}
