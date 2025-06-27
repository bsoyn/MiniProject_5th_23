package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class PurchasedBookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<PurchasedBook>> {

    @Override
    public EntityModel<PurchasedBook> process(
        EntityModel<PurchasedBook> model
    ) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/purchasebook")
                .withRel("purchasebook")
        );

        return model;
    }
}
