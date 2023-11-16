package co.latelier.TennisApplication.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {

    @Value("${json.file.name:headtohead.json}")
    private String jsonFileName;

    @Bean
    public ClassPathResource classPathResource() {
        return new ClassPathResource(jsonFileName);
    }
}
