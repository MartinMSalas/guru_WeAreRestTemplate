package guru.springframework.spring7resttemplate.client;


import guru.springframework.spring7resttemplate.config.BeerClientProperties;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


/*
 * Author: M
 * Date: 16-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */
/*
@RestClientTest(BeerClientImpl.class)
@EnableConfigurationProperties(BeerClientProperties.class)
@TestPropertySource(properties = {
        "rest.template.rootUrl=http://localhost:8080/api/v1",
        "rest.template.beer-path=/beer"
})
class BeerClientRestClientTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final String BEER_PATH = "/beer";

    @Autowired
    BeerClient beerClient;

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;

    BeerDTO dto;
    String dtoJson;

    @BeforeEach
    void setUp() throws Exception {
        dto = buildBeerDto();
        dtoJson = objectMapper.writeValueAsString(dto);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void givenBeerToCreate_whenCreateBeer_thenReturnCreatedBeer() {

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(BASE_URL + BEER_PATH))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.createBeer(dto);

        assertThat(result.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Test
    void givenBeerExists_whenGetBeerById_thenReturnBeer() {

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(BASE_URL + BEER_PATH + "/" + dto.getBeerId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.getBeerById(dto.getBeerId());

        assertThat(result.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    void givenBeerExists_whenDeleteBeer_thenReturnDeletedBeer() {

        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestTo(BASE_URL + BEER_PATH + "/" + dto.getBeerId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.deleteBeer(dto.getBeerId());

        assertThat(result.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // =====================================================
    // LIST
    // =====================================================

    @Test
    void givenBeersExist_whenListBeers_thenReturnPage() throws Exception {

        String payload = objectMapper.writeValueAsString(
                new BeerDTOPageImpl(Arrays.asList(dto), 1, 25, 1)
        );

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(BASE_URL + BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> page = beerClient.listBeers();

        assertThat(page.getContent()).hasSize(1);

        server.verify();
    }

    // =====================================================
    // LIST FILTERED
    // =====================================================

    @Test
    void givenBeerExists_whenListBeersFilteredByName_thenReturnMatchingBeer() throws Exception {

        String payload = objectMapper.writeValueAsString(
                new BeerDTOPageImpl(Arrays.asList(dto), 1, 25, 1)
        );

        server.expect(method(HttpMethod.GET))
                .andExpect(queryParam("beerName", "Mango"))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> page = beerClient.listBeers("Mango", null, 0, 25);

        assertThat(page.getContent()).hasSize(1);

        server.verify();
    }

    // =====================================================
    // HELPER
    // =====================================================

    private BeerDTO buildBeerDto() {
        return BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .beerName("Mango BLOBS")
                .beerStyle(BeerStyle.IPA)
                .price(new BigDecimal("10.99"))
                .quantityOnHand(100)
                .upc("12345")
                .build();
    }
}

 */