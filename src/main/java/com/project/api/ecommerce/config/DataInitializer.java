package com.project.api.ecommerce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

// Fazer operações quando a aplicação subir o contexto
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger log = LoggerFactory.getLogger( DataInitializer.class );

    @Override
    public void onApplicationEvent( ApplicationReadyEvent event ) {
        log.info( "Aplicação está rodando" );
    }
}
