package guru.springframework.spring7resttemplate.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: M
 * Date: 15-Feb-26
 * Project Name: We are Rest Template
 * Description: beExcellent
 * This class is used to deserialize the paginated response from the API into a PageImpl object
 */
// To ignore unknown properties in the JSON response
// @JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable", "last", "totalPages", "sort", "first", "numberOfElements"})
public class BeerDTOPageImpl extends PageImpl<BeerDTO>{

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerDTOPageImpl(@JsonProperty("content") List<BeerDTO> content,
                           @JsonProperty("number") int page,
                           @JsonProperty("size") int size,
                           @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

     public BeerDTOPageImpl(List<BeerDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

     public BeerDTOPageImpl() {
        super(new ArrayList<>());
    }
}
