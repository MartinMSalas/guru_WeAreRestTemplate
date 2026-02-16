package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void getBeerById(){

        Page<BeerDTO> beerDTOPage = beerClient.listBeers();
        assertNotNull(beerDTOPage.getContent().getFirst());
        BeerDTO beerDTO = beerDTOPage.getContent().getFirst();
        assertNotNull(beerDTO.getBeerId());
        System.out.println(beerDTO.getBeerId());

        org.springframework.http.ResponseEntity<BeerDTO> response = beerClient.getBeerById(beerDTO.getBeerId());
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        BeerDTO beerById = response.getBody();
        assertNotNull(beerById);
        assertEquals(beerDTO.getBeerId(), beerById.getBeerId());
    }

    @Test
    void listBeersNoBeerName() {
        beerClient.listBeers();

    }

    @Test
    void listBeers() {
        beerClient.listBeers("IPA", String.valueOf(BeerStyle.AMERICAN_IPA),2,100);

    }
}