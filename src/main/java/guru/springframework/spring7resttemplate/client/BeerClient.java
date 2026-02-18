package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/*
 * Author: M
 * Date: 15-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
public interface BeerClient {

    Page<BeerDTO> listBeers();
    Page<BeerDTO> listBeers(String beerName, String beerStyle, Integer page, Integer size);

    BeerDTO createBeer(BeerDTO beerDTO);
    BeerDTO getBeerById(UUID beerId);

    BeerDTO updateBeer(UUID beerId, BeerDTO beerDTO);
    BeerDTO deleteBeer(UUID beerId);
}
