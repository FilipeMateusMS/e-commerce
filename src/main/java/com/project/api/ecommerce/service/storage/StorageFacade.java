package com.project.api.ecommerce.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageFacade {

    private final StorageService storageService;

    public StorageFacade(StorageService storageService) {
        this.storageService = storageService;
    }

    public String salvar(MultipartFile file) {
        return storageService.salvar(file);
    }
}
