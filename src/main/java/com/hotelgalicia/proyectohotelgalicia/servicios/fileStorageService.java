package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class fileStorageService {
    private final Path rootLocation = Paths.get("images");

    public String store(MultipartFile file, String title) throws RuntimeException {
        if (file.isEmpty())
            throw new RuntimeException("Fichero vacío");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new RuntimeException("Fichero incorrecto");
        }
        String extension = StringUtils.getFilenameExtension(filename);
        String storedFilename = title.replace(" ", "_") + "." + extension;
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ioe) {
            throw new RuntimeException("Error en escritura");
        }
    }

    public void delete(String filename) throws RuntimeException {
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file))
                throw new RuntimeException("No existe el fichero");
            Files.delete(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Error en borrado");
        }
    }

    public Resource loadAsResource(String filename) throws RuntimeException {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else if (!resource.exists()) {
                throw new RuntimeException("No se pudo encontrar el archivo.");
            } else {
                throw new RuntimeException("No se pudo leer el archivo.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}