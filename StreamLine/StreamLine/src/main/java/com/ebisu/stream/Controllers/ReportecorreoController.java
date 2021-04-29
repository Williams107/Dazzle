package com.ebisu.stream.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.ReporteModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("reporcorreo")
public class ReportecorreoController {
	
	@Autowired
	private ReporteModel reporte;
	
	@GetMapping("/reporteemail")
	public String validacion() {
		return "reportes/reportecorreo";
	}
	
	@RequestMapping(value="/reporteCorreos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String reporte_correos(@RequestParam("correo") String correo,
			@RequestParam("asunto") String asunto,
			@RequestParam("anio") String anio,
			@RequestParam("mes") String mes,
			@RequestParam("fecha_ini") String fecha_ini,
			@RequestParam("fecha_fin") String fecha_fin) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		String consulta = "";
		try {
			
			if(!correo.isEmpty()) {
				consulta += " and correo='"+correo+"' ";
			}
			if(!asunto.isEmpty()) {
				consulta += " and asunto  like'%"+asunto+"%' ";
			}
			
			if(!anio.isEmpty()) {
				consulta += " and YEAR(fecha)='"+anio+"' ";
			}
			if(!mes.isEmpty()) {
				consulta += " and MONTH(fecha)='"+mes+"' ";
			}
			
			if(!fecha_ini.isEmpty() && !fecha_fin.isEmpty()) {
				consulta += " and fecha BETWEEN '"+fecha_ini+"' and '"+fecha_fin+"' ";
			}
			
			List<Map<String, Object>> datos = reporte.consultar_correos(consulta);
			if(!datos.isEmpty()) {									
				map.put("code", 1);
				map.put("datos",datos);
				json = new ObjectMapper().writeValueAsString(map);
			}
			else {
				map.put("code", 0);
				map.put("mensaje","No se encontro ningún correo");
				json = new ObjectMapper().writeValueAsString(map);
			}
			
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;	
		
	}
}
