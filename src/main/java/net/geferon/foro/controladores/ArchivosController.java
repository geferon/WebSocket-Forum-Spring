package net.geferon.foro.controladores;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import net.geferon.foro.servicios.AlmacenamientoServicio;

@Controller
public class ArchivosController {
	@Autowired
	AlmacenamientoServicio almacenamiento;
	
	@GetMapping("/files/**")
	@ResponseBody
	public ResponseEntity<Resource> getFile(HttpServletRequest request) {
		String filename = (String) request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE );
		filename = filename.replaceFirst("^/files/", "");
		Resource file = almacenamiento.load(filename);
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
			.body(file);
	}
}
