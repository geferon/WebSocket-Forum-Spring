package net.geferon.foro.servicios;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import net.geferon.foro.excepciones.ResourceNotFoundException;

@Service
public class AlmacenamientoServicioImpl implements AlmacenamientoServicio {
	@Value("${geferon.app.uploads.location}")
	private String uploadsLocation;
	
	@Autowired
	private ServletContext context;
	
	@Override
	public String getUploadsLocation() {
		return this.uploadsLocation;
	}

	private Path getRoot() {
		return Paths.get(uploadsLocation, "uploads");
	}

	@Override
	public void init() {
		try {
			Path root = getRoot();
			if (Files.notExists(root) || FileUtils.isEmptyDirectory(root.toFile())) {
				//Files.createDirectory(root);
				FileUtils.copyDirectory(new File(context.getRealPath("uploads")), root.toFile());
			}
		} catch (IOException e) {
			//throw new RuntimeException("No se ha podido crear la carpeta de archivos subidos", e);
		}
	}

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.getRoot().resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido guardar el archivo. Error: " + e.getMessage(), e);
		}
	}

	@Override
	public void save(InputStream file, String destination) {
		try {
			Files.copy(file, this.getRoot().resolve(destination), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido guardar el archivo. Error: " + e.getMessage(), e);
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = getRoot().resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new ResourceNotFoundException("No se ha podido encontrar el archivo!");
			}
		} catch (MalformedURLException e) {
		  throw new RuntimeException("Error: " + e.getMessage(), e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			Path root = getRoot();
			return Files.walk(root, 1)
				.filter(path -> !path.equals(root))
				.map(root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Ha habido un error al cargar los archivos.", e);
		}
	}

	@Override
	public void delete(String filename) {
		try {
			Files.delete(getRoot().resolve(filename));
		} catch (IOException e) {
			throw new RuntimeException("Ha habido un error al borrar el archivo", e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(getRoot().toFile());
	}

}
