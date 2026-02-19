package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.config.BeerClientProperties;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

/*
 * Author: M
 * Date: 15-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
@EnableConfigurationProperties(BeerClientProperties.class)
@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplate restTemplate;

    private final BeerClientProperties properties;

    //private static final String BEER = "/beer";

    @Override
    public BeerDTO createBeer(BeerDTO beerDTO) {

        ResponseEntity<BeerDTO> response =
                restTemplate.postForEntity(properties.getBeerPath(), beerDTO, BeerDTO.class);

        return response.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {

        if (beerId == null) {
            throw new IllegalArgumentException("Beer ID must not be null");
        }

        ResponseEntity<BeerDTO> response =
                restTemplate.getForEntity(properties.getBeerPath() + "/{beerId}", BeerDTO.class, beerId);

        return response.getBody();
    }

    @Override
    public BeerDTO updateBeer(UUID beerId, BeerDTO beerDTO) {

        if (beerId == null) {
            throw new IllegalArgumentException("Beer ID must not be null");
        }
        if (beerDTO == null) {
            throw new IllegalArgumentException("BeerDTO must not be null");
        }

        ResponseEntity<BeerDTO> response =
                restTemplate.exchange(
                        properties.getBeerPath() + "/{beerId}",
                        HttpMethod.PUT,
                        new HttpEntity<>(beerDTO),
                        BeerDTO.class,
                        beerId
                );

        return response.getBody();
    }

    @Override
    public BeerDTO deleteBeer(UUID beerId) {

        if (beerId == null) {
            throw new IllegalArgumentException("Beer ID must not be null");
        }

        ResponseEntity<BeerDTO> response =
                restTemplate.exchange(
                        properties.getBeerPath() + "/{beerId}",
                        HttpMethod.DELETE,
                        null,
                        BeerDTO.class,
                        beerId
                );

        return response.getBody();
    }

    @Override
    public Page<BeerDTO> listBeers() {

        ResponseEntity<BeerDTOPageImpl> response =
                restTemplate.getForEntity(properties.getBeerPath(), BeerDTOPageImpl.class);

        return response.getBody();
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, String beerStyle, Integer page, Integer size) {

        return restTemplate.getForObject(
                "/beer?beerName={beerName}&beerStyle={beerStyle}&page={page}&size={size}",
                BeerDTOPageImpl.class,
                beerName,
                beerStyle,
                page,
                size
        );
    }
}
