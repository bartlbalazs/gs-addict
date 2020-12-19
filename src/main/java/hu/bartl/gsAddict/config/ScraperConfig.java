package hu.bartl.gsAddict.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "application.scraper")
public class ScraperConfig {

    private String url;
    private Set<String> include;
    private Set<String> exclude;
}
