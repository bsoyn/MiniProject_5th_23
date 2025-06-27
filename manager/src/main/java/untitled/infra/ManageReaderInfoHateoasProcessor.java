package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class ManageReaderInfoHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<ManageReaderInfo>> {

    @Override
    public EntityModel<ManageReaderInfo> process(
        EntityModel<ManageReaderInfo> model
    ) {
        return model;
    }
}
