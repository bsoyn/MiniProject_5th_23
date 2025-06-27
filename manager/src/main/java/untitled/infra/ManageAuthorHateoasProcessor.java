package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class ManageAuthorHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<ManageAuthor>> {

    @Override
    public EntityModel<ManageAuthor> process(EntityModel<ManageAuthor> model) {
        return model;
    }
}
