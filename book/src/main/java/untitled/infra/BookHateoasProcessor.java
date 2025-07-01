package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;
import untitled.domain.Book.Book;

@Component
public class BookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Book>> {

    @Override
    public EntityModel<Book> process(EntityModel<Book> model) {
        model.add(
            Link
                .of("books/authority")
                .withRel("authority")
        );

        return model;
    }
}
