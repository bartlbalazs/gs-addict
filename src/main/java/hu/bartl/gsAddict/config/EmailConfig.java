package hu.bartl.gsAddict.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.email")
public class EmailConfig {

    private String receiver;
}
