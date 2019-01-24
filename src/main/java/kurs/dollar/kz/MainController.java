package kurs.dollar.kz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by BahaWood on 1/24/19.
 */
@RestController
public class MainController {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/schedulerCourseParse")
    public @ResponseBody String scheduleKurs(){
        try {
            JobDetail jobDetail = JobBuilder.newJob(CourseGettingJob.class)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("CronTrigger")
                    .withDescription("Dollar Course Parsing Trigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            return "Job started";
        } catch (SchedulerException e) {
            // schedule exception
            e.printStackTrace();
        }
        catch (Exception e){
            // other exception
            e.printStackTrace();
        }
        return "Error occured";
    }

    @PostMapping("/schedulerStop")
    public @ResponseBody String schedulerStop(){
        try {
            scheduler.shutdown();
            return "Scheduler stopped!";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "Error occured";
    }
    @GetMapping("/getCourse")
    public @ResponseBody CourseModel getKurse(){
        return courseRepository.getLastCourse();
    }

}
