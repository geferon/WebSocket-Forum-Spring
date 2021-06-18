package net.geferon.foro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {
	@RequestMapping({"/{path:^(?!api|public|ws)[^\\.]*}", "/**/{path:^(?!api|public|ws).*}/{path:[^\\.]*}"})
	public String forward() {
        return "forward:/";
    }
}
