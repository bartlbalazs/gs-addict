package hu.bartl.gsAddict;

import hu.bartl.gsAddict.service.ScheduledService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GsAddictApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(GsAddictApplication.class, args);
        context.getBean(ScheduledService.class).lookForChanges();
    }
}
