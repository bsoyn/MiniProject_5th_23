package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class BookSummaryHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<BookSummary>> {

    @Override
    public EntityModel<BookSummary> process(EntityModel<BookSummary> model) {
        return model;
    }
}
