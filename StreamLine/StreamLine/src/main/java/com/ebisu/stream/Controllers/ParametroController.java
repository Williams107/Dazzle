package com.ebisu.stream.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Configparametro")
public class ParametroController {

	@GetMapping("/parametro")
	public String validacion() {
		return "configuracion/parametros";
	}
	
	
}
