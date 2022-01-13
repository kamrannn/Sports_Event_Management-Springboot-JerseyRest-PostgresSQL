package org.management.SportEvent.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.management.SportEvent.resource.AthleteResource;
import org.management.SportEvent.resource.ChipResource;
import org.management.SportEvent.resource.EventResource;
import org.management.SportEvent.resource.ResultResource;
import org.springframework.stereotype.Component;

@Component
public class RestConfig extends ResourceConfig {

    public RestConfig(Class<?>... classes) {
        register(AthleteResource.class);
        register(ChipResource.class);
        register(EventResource.class);
        register(ResultResource.class);
    }
}
