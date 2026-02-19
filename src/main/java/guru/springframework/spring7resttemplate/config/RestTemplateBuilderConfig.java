package guru.springframework.spring7resttemplate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.autoconfigure.RestTemplateBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

/*
 * Author: M
 * Date: 15-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
@Slf4j
@Configuration
public class RestTemplateBuilderConfig {

/*
    //@Value("${rest.template.rootUrl}")
    //String rootUrl;

    @Value("${rest.template.username}")
    String username;

    @Value("${rest.template.password}")
    String password;

 */
/*
    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){
        log.info("Configuring RestTemplateBuilder with root URL: {}", rootUrl);
        log.debug("Username: {}, Password: {}", username, password != null ? "******" : null);
        assert rootUrl != null;

        RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
        DefaultUriBuilderFactory uriBuilderFactory = new
                DefaultUriBuilderFactory(rootUrl);

        RestTemplateBuilder builderWithAuth = builder.basicAuthentication(username,password);

        return builderWithAuth.uriTemplateHandler(uriBuilderFactory);
    }

 */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     @Value("${rest.template.rootUrl}") String rootUrl,
                                     @Value("${rest.template.username}") String username,
                                     @Value("${rest.template.password}") String password) {
        log.info("ROOT URL IS: {}", rootUrl);
        return builder
                .rootUri(rootUrl)
                .basicAuthentication(username, password)
                .build();
    }
}
