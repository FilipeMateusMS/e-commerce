package com.project.api.ecommerce.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Não quebra se o JSON tiver campos que não existem no DTO
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        // DESATIVA o formato de array para timestamps
        mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );

        // Registra suporte para tipos modernos do Java (como LocalDateTime)
        mapper.registerModule( new JavaTimeModule() );
        return mapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // Desativa a escrita de datas como arrays/timestamps
            builder.featuresToDisable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
            // Define um formato padrão global
            builder.simpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
        };
    }
}