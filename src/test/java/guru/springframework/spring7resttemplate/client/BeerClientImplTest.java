package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;


    private BeerDTO buildUniqueBeerDto() {
        String unique = String.valueOf(System.nanoTime());
        return BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango BLOBS " + unique)
                .beerStyle(BeerStyle.AMERICAN_IPA)
                .quantityOnHand(500)
                .upc("123245" + unique.substring(Math.max(0, unique.length() - 4))) // keeps it short-ish
                .build();
    }


    private void safeDelete(UUID beerId) {
        if (beerId == null) return;
        try {
            beerClient.deleteBeer(beerId);
        } catch (Exception ignored) {
            // best-effort cleanup (already deleted or server error)
        }
    }

    private void safeDelete(BeerDTO created) {
        if (created == null || created.getBeerId() == null) return;
        try {
            beerClient.deleteBeer(created.getBeerId());
        } catch (Exception ignored) {
            // best-effort cleanup
        }
    }

    @Test
    void givenBeerCreated_whenGetBeerById_thenReturnBeer_andCleanup() {
        BeerDTO created = beerClient.createBeer(buildUniqueBeerDto());

        try {
            // GIVEN
            assertNotNull(created);
            assertNotNull(created.getBeerId());

            // WHEN
            BeerDTO byId = beerClient.getBeerById(created.getBeerId());

            // THEN
            assertNotNull(byId);
            assertEquals(created.getBeerId(), byId.getBeerId());
            assertNotNull(byId.getBeerName());
            assertNotNull(byId.getBeerStyle());

        } finally {
            safeDelete(created);
        }
    }

    @Test
    void givenBeerCreated_whenUpdateBeer_thenReturnUpdatedBeer_andCleanup() {
        BeerDTO created = beerClient.createBeer(buildUniqueBeerDto());

        try {
            // GIVEN
            assertNotNull(created);
            assertNotNull(created.getBeerId());

            String newName = created.getBeerName() + " UPDATED";

            // WHEN
            created.setBeerName(newName);
            BeerDTO updated = beerClient.updateBeer(created.getBeerId(), created);

            // THEN
            assertNotNull(updated);
            assertEquals(created.getBeerId(), updated.getBeerId());
            assertEquals(newName, updated.getBeerName());

        } finally {
            safeDelete(created);
        }
    }

    @Test
    void givenBeerCreated_whenDeleteBeer_thenSubsequentGetByIdThrows4xx() {
        BeerDTO created = beerClient.createBeer(buildUniqueBeerDto());

        try {
            // GIVEN
            assertNotNull(created);
            assertNotNull(created.getBeerId());

            // WHEN
            BeerDTO deleted = beerClient.deleteBeer(created.getBeerId());

            // THEN
            assertNotNull(deleted);
            assertEquals(created.getBeerId(), deleted.getBeerId());

            HttpClientErrorException ex =
                    assertThrows(HttpClientErrorException.class, () -> beerClient.getBeerById(created.getBeerId()));
            assertTrue(ex.getStatusCode().is4xxClientError());

        } finally {
            // best effort: if already deleted, safeDelete ignores errors
            safeDelete(created);
        }
    }

    @Test
    void givenBeersExist_whenListBeersWithDefaults_thenReturnValidPageAndBeerShape() {
        // WHEN
        Page<BeerDTO> page = beerClient.listBeers(null, null, null, null);

        // THEN
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getNumber() >= 0);
        assertTrue(page.getSize() > 0);
        assertTrue(page.getTotalElements() >= 0);

        assertFalse(page.getContent().isEmpty(), "Expected at least 1 beer in the system");

        BeerDTO first = page.getContent().getFirst();
        assertNotNull(first.getBeerId());
        assertNotNull(first.getBeerName());
        assertNotNull(first.getBeerStyle());
    }

    @Test
    void givenBeersExist_whenListBeersFilteredByName_thenEveryResultMatchesFilter() {
        // GIVEN
        String filter = "ALE";
        Integer pageNumber = 0;
        Integer pageSize = 25;

        // WHEN
        Page<BeerDTO> page = beerClient.listBeers(filter, null, pageNumber, pageSize);

        // THEN
        assertNotNull(page);
        assertNotNull(page.getContent());

        assertEquals(pageNumber, page.getNumber());
        assertEquals(pageSize, page.getSize());

        page.getContent().forEach(b ->
                assertTrue(
                        b.getBeerName() != null && b.getBeerName().toUpperCase().contains(filter),
                        () -> "Beer name did not match filter. filter=" + filter + ", beerName=" + b.getBeerName()
                )
        );
    }

    @Test
    void givenBeerCreated_whenListBeersFilteredByExactCreatedName_thenCreatedBeerIsReturned_andCleanup() {
        BeerDTO created = beerClient.createBeer(buildUniqueBeerDto());

        try {
            // GIVEN
            assertNotNull(created);
            assertNotNull(created.getBeerId());
            String exactName = created.getBeerName();

            // WHEN
            Page<BeerDTO> page = beerClient.listBeers(exactName, null, 0, 50);

            // THEN
            assertNotNull(page);
            assertNotNull(page.getContent());

            boolean found = page.getContent().stream()
                    .anyMatch(b -> exactName.equals(b.getBeerName()) && created.getBeerId().equals(b.getBeerId()));

            assertTrue(found, "Expected to find newly created beer by filtering with its exact name");

        } finally {
            safeDelete(created);
        }
    }
}