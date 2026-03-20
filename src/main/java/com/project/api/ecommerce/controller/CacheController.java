package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/v1/cache" )
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @PostMapping( "/clear" )
    @PreAuthorize( "hasRole('ADMIN')" )
    public void clear( @RequestParam( "cacheName" ) String cacheName ){
        cacheService.clear( cacheName );
    }
}
