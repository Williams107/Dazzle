package com.ebisu.stream.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.BandejaModel;
import com.ebisu.stream.Models.ReporteModel;
import com.ebisu.stream.Models.UsuariosModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
//#MG
@RequestMapping("RDcontenido")
public class ReportedoscontController {

	@Autowired
	private UsuariosModel usuario;
	
	@GetMapping("/contenidoD")
	public String contar(Model model) {
		List<Map<String, Object>> empresas = usuario.consultar_empresas();
		model.addAttribute("empresas", empresas);
		return "reportes/reporte2_contenido";
	}
	
		
	@Autowired
	private ReporteModel reporte;
	
	@Autowired
	private BandejaModel bandeja;

	
	//#F1
	@PostMapping(value="/obtenerReporte",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String obtener_reporte(
			@RequestParam("sociedad") String sociedad,			
			@RequestParam("estado") String estado,
			@RequestParam("ruc") String ruc,
			@RequestParam("fecha_fi") String fecha_fi,
			@RequestParam("fecha_ini") String fecha_ini) {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		String consulta = "";
		System.out.println(sociedad);
		try {	
			
			if(!sociedad.isEmpty()) {
				consulta += " and e.nombre='"+sociedad+"' ";
			}
			
			if(!estado.isEmpty()) {
				consulta += " and vc.numero_mensaje='"+estado+"' ";
			}
			
			if(!fecha_ini.isEmpty()) {
				consulta += " and convert(date,vp.fecha_emision) >= '"+fecha_ini+"' and convert(date,vp.fecha_emision) <= '"+fecha_fi+"' ";
			}
			
			if(!ruc.isEmpty()) {
				consulta += " and vp.ruc='"+ruc+"' ";
			}
			
			List<Map<String, Object>> datos = reporte.obtener_reporte(consulta);
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
	
	
	@PostMapping(value="/obtenerDetalles",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String obtener_reporte(@RequestBody String id) {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		try {	
					
			String[] variables = id.split(";");
			
			List<Map<String, Object>> datos_correos = reporte.obtener_detalle_correo(id);
			//List<Map<String, Object>> datos_sunat = reporte.obtener_detalle_sunat(id);
		//	List<Map<String, Object>> datos_etiquetas_migo= reporte.obtener_detalle_etiquetas_migo(id);
	//		List<Map<String, Object>> datos_etiquetas_ord= reporte.obtener_detalle_etiquetas_ord(id);
			
			
			List<Map<String, Object>> ruta_factura = reporte.obtener_ruta_factura(variables[0]);
			
			//List<Map<String, Object>> datos_factura = reporte.obtener_detalle_factura(variables[2],variables[1]);			
			//List<Map<String, Object>> datos_hes_migo = reporte.obtener_detalle_hes_migo(variables[2],variables[1]);
			
			
			
			List<Map<String, Object>> datos_doc = reporte.obtener_detalle_documentos(variables[2],variables[1]);
			
			if(!datos_correos.isEmpty()) {									
				map.put("code", 1);
				map.put("datosCorreo",datos_correos);
				//map.put("datosSunat",datos_sunat);
				//map.put("datosEtiqueta_migo",datos_etiquetas_migo);
				//map.put("datosEtiqueta_ord",datos_etiquetas_ord);
				
				map.put("rutaFactura",ruta_factura);
				
				//map.put("datosFactura",datos_factura);
				//map.put("datosHesMigo",datos_hes_migo);
				
				map.put("datosDoc",datos_doc);
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
	
	
	
	
	
	
	@RequestMapping(value="/buscarCorreo", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String buscar_correo(@RequestBody String id) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			if(!id.equals("")) {
					
				List<Map<String, Object>> datos = bandeja.consultar_correo_id(id);
				List<Map<String, Object>> datos_archivos = bandeja.consultar_archivos_email(id);
				List<Map<String, Object>> datos_documentos_formales = bandeja.consultar_documentos_formales(id);
				
				if(!datos_documentos_formales.isEmpty())
				{
					List<Map<String, Object>> datos_validacion_xml = bandeja.consultar_validacion_xml(id);
					map.put("datos_xml", datos_validacion_xml);
				}
				
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					map.put("documentos_formales", datos_documentos_formales);
					map.put("archivos", datos_archivos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ningún usuario");
					json = new ObjectMapper().writeValueAsString(map);
				}
			}
			else {
				map.put("code", 0);
				map.put("mensaje","No existe id");
				json = new ObjectMapper().writeValueAsString(map);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			//System.out.println(ex.getMessage());
		}			
		return json;		
	}
	
	
}
