package net.geferon.foro.servicios;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AlmacenamientoServicio {
	void init();

	void save(MultipartFile file);
	void save(InputStream file, String destination);

	Resource load(String filename);

	Stream<Path> loadAll();

	void delete(String filename);
	void deleteAll();

	String getUploadsLocation();
}
