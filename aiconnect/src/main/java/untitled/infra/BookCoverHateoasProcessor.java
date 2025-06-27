package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class BookCoverHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<BookCover>> {

    @Override
    public EntityModel<BookCover> process(EntityModel<BookCover> model) {
        return model;
    }
}
