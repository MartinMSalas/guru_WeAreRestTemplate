package guru.springframework.spring7resttemplate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Author: M
 * Date: 19-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
@ConfigurationProperties(prefix="rest.template")
public class BeerClientProperties {

    @Getter
    @Setter
    private String beerPath = "/beer";
    private String rootUrl;

}
