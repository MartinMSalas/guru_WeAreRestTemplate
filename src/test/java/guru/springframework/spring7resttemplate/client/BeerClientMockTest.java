package guru.springframework.spring7resttemplate.client;


import guru.springframework.spring7resttemplate.config.BeerClientProperties;
import guru.springframework.spring7resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring7resttemplate.model.BeerStyle;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/*
 * Author: M
 * Date: 16-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 */

@RestClientTest(BeerClientImpl.class)
@EnableConfigurationProperties(BeerClientProperties.class)
class BeerClientMockTest {

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
    void setUp() throws JsonProcessingException {
        dto = buildBeerDto();
        dtoJson = objectMapper.writeValueAsString(dto);
    }

    // ========================================================
    // CREATE
    // ========================================================

    @Test
    void givenBeerCreated_whenCreateBeer_thenReturnBeer() throws Exception {

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(BASE_URL + BEER_PATH))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO response = beerClient.createBeer(dto);

        assertThat(response.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // ========================================================
    // GET BY ID
    // ========================================================

    @Test
    void givenBeerCreated_whenGetBeerById_thenReturnBeer() {

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(BASE_URL + BEER_PATH + "/" + dto.getBeerId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO response = beerClient.getBeerById(dto.getBeerId());

        assertThat(response.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // ========================================================
    // DELETE
    // ========================================================

    @Test
    void givenBeerExists_whenDeleteBeer_thenReturnDeletedBeer() {

        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestTo(BASE_URL + BEER_PATH + "/" + dto.getBeerId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));

        BeerDTO response = beerClient.deleteBeer(dto.getBeerId());

        assertThat(response.getBeerId()).isEqualTo(dto.getBeerId());

        server.verify();
    }

    // ========================================================
    // LIST
    // ========================================================

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

    // ========================================================
    // LIST WITH FILTER
    // ========================================================

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

    // ========================================================
    // HELPER
    // ========================================================

    private BeerDTO buildBeerDto() {
        return BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango BLOBS")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
    }
}