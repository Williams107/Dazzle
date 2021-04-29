package com.ebisu.stream.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.LogModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Errorcorreo")
public class ErrorcorreoController {
	
	@Autowired
	private LogModel log;

	@GetMapping("/erroremail")
	public String validacion() {
		return "errorcorreo/errorcorreos";
	}
	
	@PostMapping(value="/correosRechazados",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String correos_rechazados() {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {	
			List<Map<String, Object>> datos = log.consultar_correos_rechazados();
			if(!datos.isEmpty()) {									
				map.put("code", 1);
				map.put("datos",datos);
				json = new ObjectMapper().writeValueAsString(map);
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Nose encontro ningún resultado");
				json = new ObjectMapper().writeValueAsString(map);
			}
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
	@PostMapping(value="/logDescarga",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String log_descarga() {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {	
			List<Map<String, Object>> datos = log.consultar_log_descarga();
			if(!datos.isEmpty()) {									
				map.put("code", 1);
				map.put("datos",datos);
				json = new ObjectMapper().writeValueAsString(map);
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Nose encontro ningún resultado");
				json = new ObjectMapper().writeValueAsString(map);
			}
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
	@PostMapping(value="/logValidacion",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String log_validacion() {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {	
			List<Map<String, Object>> datos = log.consultar_log_validacion();
			if(!datos.isEmpty()) {									
				map.put("code", 1);
				map.put("datos",datos);
				json = new ObjectMapper().writeValueAsString(map);
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Nose encontro ningún resultado");
				json = new ObjectMapper().writeValueAsString(map);
			}
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
}
