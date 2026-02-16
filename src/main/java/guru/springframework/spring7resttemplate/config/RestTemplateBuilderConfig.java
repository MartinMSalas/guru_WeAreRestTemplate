package guru.springframework.spring7resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.autoconfigure.RestTemplateBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/*
 * Author: M
 * Date: 15-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.rootUrl:http://localhost:8080}")
    private String rootUrl;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){

        return configurer.configure(new RestTemplateBuilder())
                .rootUri(rootUrl)
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(2));
    }
}
