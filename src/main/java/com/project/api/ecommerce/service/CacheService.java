package com.project.api.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public void clear( String cacheName ){
        // Limpa o valor do nome da chave fornecida, quando existente
        Objects.requireNonNull( cacheManager.getCache( cacheName ) ).clear();
    }
}
