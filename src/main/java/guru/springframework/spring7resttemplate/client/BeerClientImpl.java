package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
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
@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;


    private static final String BEER = "/beer";

    @Override
    public BeerDTO createBeer(BeerDTO beerDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(BEER);

        ResponseEntity<BeerDTO> responseEntity =
                restTemplate.postForEntity(uriComponentsBuilder.toUriString(), beerDTO, BeerDTO.class);

        return responseEntity.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        if(beerId == null){
            throw new IllegalArgumentException("Beer ID must not be null");
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(BEER + "/{beerId}")
                .uriVariables(Map.of("beerId", beerId));

        ResponseEntity<BeerDTO> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), BeerDTO.class);
        return responseEntity.getBody();
    }

    @Override
    public BeerDTO updateBeer(UUID beerId, BeerDTO beerDTO) {
        return null;
    }

    @Override
    public BeerDTO deleteBeer(UUID beerId) {
        return null;
    }

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(BEER);

        ResponseEntity<BeerDTOPageImpl> stringResponsePage =
                restTemplate.getForEntity(uriComponentsBuilder.toUriString(), BeerDTOPageImpl.class);
        BeerDTOPageImpl responsePageBodypage = stringResponsePage.getBody();
        responsePageBodypage.getContent().forEach(beer ->
                System.out.println(beer.getBeerName()));
        return responsePageBodypage;
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, String beerStyle, Integer page, Integer size) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(BEER);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }
        if (page != null) {
            uriComponentsBuilder.queryParam("page", page);
        }
        if (size != null) {
            uriComponentsBuilder.queryParam("size", size);
        }

        ResponseEntity<BeerDTOPageImpl> stringResponsePage =
                restTemplate.getForEntity( uriComponentsBuilder.toUriString() , BeerDTOPageImpl.class);

        BeerDTOPageImpl responsePageBodypage = stringResponsePage.getBody();

        responsePageBodypage.getContent().forEach(beer ->
                System.out.println(beer.getBeerName()));

        return responsePageBodypage;
    }
}

