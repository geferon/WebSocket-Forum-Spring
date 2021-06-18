package net.geferon.foro;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.geferon.foro.repositorios.servicios.ServicioCategoria;
import net.geferon.foro.repositorios.servicios.ServicioHilo;
import net.geferon.foro.repositorios.servicios.ServicioPublicacion;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ForoApplicationTests {
	@Autowired
	ServicioCategoria scat;
	@Autowired
	ServicioHilo shilo;
	@Autowired
	ServicioPublicacion spub;
	@Autowired
	ServicioSubscripcion ssub;
	@Autowired
	ServicioUsuario susu;

	@Test
	void contextLoads() {
	}

}
