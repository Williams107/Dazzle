package com.ebisu.stream.Controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.ReporteModel;
import com.ebisu.stream.POJOS.DatosUsuario;
import com.ebisu.stream.Services.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Contabilizar")

public class ContabilizarController {
		@GetMapping("/contar")
		public String contar(@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
			
			String ruta = "";
			if(infoUser.getIdusuario() == 0 || infoUser == null)
			{
				ruta = "redirect:logout";
			}
			else {
				
				ruta = "contabilizar/contar";
			}			
			
			return ruta;
		}
	/////////-------------------//////////////////	
		@Autowired
		private ReporteModel Reportecontabilizacion;
		
		@Autowired
	    private ApiService api;

		
		//#F1
		@PostMapping(value="/obtenerReporte",produces = "text/plain;charset=UTF-8")
		@ResponseBody
		public String obtener_reporte() {
			
			String json = "";
			HashMap<String, Object> map = new HashMap<>();			
			
			try {	
				List<Map<String, Object>> datos = Reportecontabilizacion.obtener_reporte_nocontabilizados();
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
		
		// #F1
		@PostMapping(value = "/obtenerRetenciones", produces = "text/plain;charset=UTF-8")
		@ResponseBody
		public String obtener_retenciones(@RequestBody String idTipo) {

			String json = "";
			HashMap<String, Object> map = new HashMap<>();
			try {
				List<Map<String, Object>> retencion = Reportecontabilizacion.obtener_ind_retencion(idTipo);
				if (!retencion.isEmpty()) {
					map.put("code", 1);
					map.put("datos", retencion);
					json = new ObjectMapper().writeValueAsString(map);
				} else {
					map.put("code", 0);
					map.put("mensaje", "Nose encontro ningún resultado");
					json = new ObjectMapper().writeValueAsString(map);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			return json;
		}
		
		
		@PostMapping(value="/obtenerDatosContabilizar",produces = "text/plain;charset=UTF-8")
		@ResponseBody
		public String obtener_datos_contabilizar(@RequestBody String idEmail) {
			
			String json = "";
			HashMap<String, Object> map = new HashMap<>();			
			
			try {	
				List<Map<String, Object>> datos = Reportecontabilizacion.consultar_xml_sap(idEmail);
				List<Map<String, Object>> datos_facturas = Reportecontabilizacion.consultar_montos_sap(idEmail);
				List<Map<String, Object>> ruta_factura = Reportecontabilizacion.obtener_ruta_factura(idEmail);
				
				List<Map<String, Object>> variables_consulta_Sap = Reportecontabilizacion.obtener_datos_consulta_Sap(idEmail);
				
				String datos_sap = "{Data:[{\"LIFNR\":'"+((String) variables_consulta_Sap.get(0).get("ncuenta_proveedor_acreedor"))+										
						"',\"BUKRS\":'"+((String) variables_consulta_Sap.get(0).get("sociedad"))+"'}]}";
				
				String resp = api.consumir_api("http://localhost:8080/cronSap/api/dazzle/get/retenciones", datos_sap);
				
				JSONObject jsonObject = new JSONObject(resp);
				
				String filtro_ret = " and indicador_tipo_retencion in (";
				
				if(jsonObject.getInt("code")==1) {
					for(int i=0;i<(jsonObject.length()-1);i++) {
						JSONArray jsons_arreglo = jsonObject.getJSONArray("ret"+i);
						filtro_ret += "'"+jsons_arreglo.getString(2)+"',";						
					}
					
					filtro_ret += "'') ";					
				}
				
				
				List<Map<String, Object>> retencion = Reportecontabilizacion.obtener_retencion(filtro_ret);
				
				List<Map<String, Object>> variables_Sap = Reportecontabilizacion.obtener_datos_consulta(idEmail);
				String json_data_sap = new ObjectMapper().writeValueAsString(variables_Sap);
				
				json_data_sap = "{Data:"+json_data_sap+"}";
						
				String resp_sap = api.consumir_api("http://localhost:8080/cronSap/api/dazzle/consult/document", json_data_sap);
				
				JSONObject jsonObject_sap = new JSONObject(resp_sap);							
				JSONArray jsonArray_Sap = jsonObject_sap.getJSONArray("datos");
				System.out.println(jsonArray_Sap.get(23));
				
				String mensaje_conta = "";
				int estatus_conta = 0;
				String num_doc_Sap = "";
				
				if(jsonArray_Sap.get(23).equals("002")) {
					mensaje_conta = (String) jsonArray_Sap.get(24);
					estatus_conta = 1;
					num_doc_Sap = (String) jsonArray_Sap.get(19);
				}
				
				
				if(!datos.isEmpty() && !datos_facturas.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					map.put("sap", datos_facturas);
					map.put("rutaFactura",ruta_factura);
					map.put("retenciones", retencion);
					map.put("mensaje_sap", mensaje_conta);
					map.put("estatus", estatus_conta);
					map.put("num_doc_sap", num_doc_Sap);
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
		
//-------------------------------------/////		
		
		@GetMapping("/descargar/{num_doc}/{id}")
		public ResponseEntity<Resource> buscar_archivo(@PathVariable(value = "num_doc") String num_doc,
		        @PathVariable(value="id") String id, HttpServletRequest request){
			// Load file as Resource
		    Resource resource;
		    
		    List<Map<String, Object>> datos = Reportecontabilizacion.buscar_archivo(num_doc, id);
		    
		  
		    Path path = Paths.get((String) datos.get(0).get("archivo"));
		    try {
		        resource = new UrlResource(path.toUri());
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		        return null;
		    }

		    // Try to determine file's content type
		    String contentType = null;
		    try {
		        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		    } catch (IOException ex) {
		        System.out.println("Could not determine file type.");
		    }

		    // Fallback to the default content type if type could not be determined
		    if (contentType == null) {
		        contentType = "application/octet-stream";
		    }
		    
		    // inline para mostrar en buscador
		    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
		            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
		            .body(resource);
		}

}


