package guru.springframework.spring7resttemplate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.autoconfigure.RestTemplateBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;

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
    /*
    // Passing properties directly to RestTemplateBuilder
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

     */

    // Using a properties class to encapsulate configuration
    @Bean
    public RestTemplateBuilder restTemplate(RestTemplateBuilderConfigurer configurer,
                                            BeerClientProperties properties,
                                            OAuthClientInterceptor oauthClientInterceptor) {
        String rootUrl = properties.getRootUrl();
        assert rootUrl != null;
        log.info("Configuring RestTemplate with root URL: {}", rootUrl);
        //log.debug("Username: {}, Password: {}", username, password != null ? "******" : null);

        return configurer.configure(new RestTemplateBuilder())
                .additionalInterceptors(oauthClientInterceptor)
                .uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
    }

    @Bean
    OAuth2AuthorizedClientManager auth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientService oAuth2AuthorizedClientService){
        // Configure and return an OAuth2AuthorizedClientManager instance

        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials()
                .build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager
                (clientRegistrationRepository, oAuth2AuthorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

}
