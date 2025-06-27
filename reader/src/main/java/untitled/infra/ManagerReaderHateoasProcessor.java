package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class ManagerReaderHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<ManagerReader>> {

    @Override
    public EntityModel<ManagerReader> process(
        EntityModel<ManagerReader> model
    ) {
        return model;
    }
}
