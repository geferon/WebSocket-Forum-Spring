package net.geferon.foro.configuracion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.geferon.foro.servicios.AlmacenamientoServicio;

@Configuration
public class AlmacenamientoConfig {
	@Bean
	CommandLineRunner init(AlmacenamientoServicio almacenamientoServicio) {
		return (args) -> {
			almacenamientoServicio.init();
		};
	}
}
