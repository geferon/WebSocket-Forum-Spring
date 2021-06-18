package net.geferon.foro.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.geferon.foro.servicios.AlmacenamientoServicio;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	AlmacenamientoServicio almacenamiento;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/uploads/**")
			.addResourceLocations(almacenamiento.getUploadsLocation());
	}
}
