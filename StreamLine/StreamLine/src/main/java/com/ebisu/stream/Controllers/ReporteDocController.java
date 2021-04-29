package com.ebisu.stream.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("repordocumento")
public class ReporteDocController {
	@GetMapping("/repordocumen")
	public String validacion() {
		return "reportes/reportedocumen";
	}
}
