package com.hotelgalicia.proyectohotelgalicia.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path rootLocation = Paths.get("images");

    public String store(MultipartFile file, String name) throws RuntimeException {
        // Validaciones
        if (file.isEmpty())
            throw new RuntimeException("Error al leer la imagen: Fichero vacío");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new RuntimeException("Error al leer la imagen: Fichero incorrecto");
        }
        // Valida si el formato es de imagen
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new RuntimeException("Error al leer la imagen: Formato incorrecto");
        // Valida si se puede leer como imagen
        try {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new RuntimeException("El fichero no es una imagen válida.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la imagen: " + e.getMessage());
        }
        // Codigo. Obtiene y valida la extension
        String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null || extension.isBlank()) {
            throw new RuntimeException("Error al leer la imagen: El fichero no tiene extensión.");
        }
        // Cambia los espacios en blanco
        String storedFilename = name.replaceAll("\\s+", "_") +UUID.randomUUID()+ "." + extension.toLowerCase();
        // Alamacena la imagen con el nombre
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ioe) {
            throw new RuntimeException("Error al guardar el fichero: " + ioe.getMessage());
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