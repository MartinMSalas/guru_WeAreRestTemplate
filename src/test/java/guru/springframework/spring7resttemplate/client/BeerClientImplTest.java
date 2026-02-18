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
    void createBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.AMERICAN_IPA)
                .upc("123456789012")
                .price(java.math.BigDecimal.valueOf(11.22))
                .quantityOnHand(111)
                .build();

        ResponseEntity<BeerDTO> response = beerClient.createNewBeer(beerDTO);
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        BeerDTO createdBeer = response.getBody();
        assertNotNull(createdBeer);
        assertNotNull(createdBeer.getBeerId());
        assertEquals(beerDTO.getBeerName(), createdBeer.getBeerName());
    }

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

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testDeleteBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        beerClient.deleteBeer(beerDto.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            //should error
            beerClient.getBeerById(beerDto.getId());
        });
    }

    @Test
    void testUpdateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        final String newName = "Mango Bobs 3";
        beerDto.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(beerDto);

        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void testCreateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO savedDto = beerClient.createBeer(newDto);
        assertNotNull(savedDto);
    }

    @Test
    void getBeerById() {

        Page<BeerDTO> beerDTOS = beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void listBeersNoBeerName() {

        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void listBeers() {

        beerClient.listBeers("ALE", null, null, null, null);
    }
}