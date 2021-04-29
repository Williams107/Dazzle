package com.ebisu.stream.Controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.BandejaModel;
import com.ebisu.stream.POJOS.DatosUsuario;
import com.ebisu.stream.Services.ApiService;
import com.ebisu.stream.Services.CorreosService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Bandeja")
public class BandejaController {

	@Autowired
	private BandejaModel bandeja;
	
	@Autowired
    private CorreosService correoService;
	
	@Autowired
    private ApiService api;
	
	@GetMapping(value= {"/","/pendientes"})
	public String pendientes(@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		String ruta = "";
		if(infoUser.getIdusuario() == 0 || infoUser == null)
		{
			ruta = "redirect:logout";
		}
		else {
			ruta = "bandeja/pendientes";
		}
		return ruta;
	}
	
	@RequestMapping(value="/obtenerCorreos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String validar_credenciales(@RequestBody String num_correo) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		String[] parametros = num_correo.split(",");
		
		try {
			if(!parametros[0].equals("")) {
				String consulta = "";
				
				if(parametros[1].equals("1")) {
					consulta += " and verificado=0 ";
				}
				if(parametros[1].equals("2")) {
					consulta += " and verificado=1 ";
				}
				if(parametros[1].equals("3")) {
					consulta += " and leido=0 ";
				}
								
				if(!parametros[0].equals("-1") && !parametros[0].equals("0")) {
					consulta += " and numeroEmail<='"+parametros[0]+"' ";
				}
				
				
				List<Map<String, Object>> datos = bandeja.consultar_correos(consulta);
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n usuario");
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
	
	@RequestMapping(value="/buscarCorreosAsunto", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String buscar_correo_asunto(@RequestBody String asunto) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			if(!asunto.equals("")) {
				
				List<Map<String, Object>> datos = bandeja.consultar_correos_asunto(asunto);
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n correo");
					json = new ObjectMapper().writeValueAsString(map);
				}
			}
			else {
				map.put("code", 0);
				map.put("mensaje","No ingreso texto");
				json = new ObjectMapper().writeValueAsString(map);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			//System.out.println(ex.getMessage());
		}			
		return json;		
	}
	
	@RequestMapping(value="/buscarFiltroCorreo", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String buscar_filtro_correo(@RequestBody String estado) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		String consulta = "";
		
		try {
			if(!estado.equals("")) {
				
				if(estado.equals("1")) {
					consulta = " and verificado=0";
				}
				else if(estado.equals("2")) {
					consulta = " and verificado=1";
				}
				else {
					consulta = " and leido=0";
				}
				
				List<Map<String, Object>> datos = bandeja.consultar_filtro_correo(consulta);
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n correo");
					json = new ObjectMapper().writeValueAsString(map);
				}
			}
			else {
				map.put("code", 0);
				map.put("mensaje","No ingreso texto");
				json = new ObjectMapper().writeValueAsString(map);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}			
		return json;		
	}
	
	@RequestMapping(value="/totalCorreos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String total_correos() {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {					
				List<Map<String, Object>> datos = bandeja.consultar_total_correos();
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n usuario");
					json = new ObjectMapper().writeValueAsString(map);
				}			
		}
		catch(Exception ex) {
			ex.printStackTrace();
			//System.out.println(ex.getMessage());
		}			
		return json;		
	}
	
	@RequestMapping(value="/obtenerEtiquetas", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String obtener_etiquetas(@RequestBody String id) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			if(!id.equals("")) {
				
				List<Map<String, Object>> datos = bandeja.consultar_etiquetas(id);
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n usuario");
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
	
	@RequestMapping(value="/buscarCorreo", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String buscar_correo(@RequestBody String variables,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		String[] parametros = variables.split(",");
		
		try {
			if(!variables.equals("")) {
				if(parametros[1].equals("0")) {
					Object[] params = { parametros[0], infoUser.getIdusuario()};
					bandeja.guardar_leido(params);
					
					Object[] params_leido = { parametros[0]};
					bandeja.actualizar_estado_leido(params_leido);
				}
				
				List<Map<String, Object>> datos = bandeja.consultar_correo_id(parametros[0]);
				List<Map<String, Object>> datos_archivos = bandeja.consultar_archivos_email(parametros[0]);
				List<Map<String, Object>> datos_documentos_formales = bandeja.consultar_documentos_formales(parametros[0]);
				
				List<Map<String, Object>> datos_ruc_factura = bandeja.consultar_ruc_factura(parametros[0]);
				
				if(!datos_documentos_formales.isEmpty())
				{
					List<Map<String, Object>> datos_validacion_xml = bandeja.consultar_validacion_xml(parametros[0]);
					map.put("datos_xml", datos_validacion_xml);
				}
				
				if(!datos.isEmpty()) {									
					map.put("code", 1);
					map.put("datos",datos);
					map.put("documentos_formales", datos_documentos_formales);
					map.put("archivos", datos_archivos);
					map.put("ruc", datos_ruc_factura);
					json = new ObjectMapper().writeValueAsString(map);
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Nose encontro ning�n usuario");
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
	
	@GetMapping("/descargar/{carpeta}/{nombre}")
	public ResponseEntity<Resource> descargar_archivo(@PathVariable(value = "carpeta") String carpeta,
	        @PathVariable(value="nombre") String nombre, HttpServletRequest request){
		// Load file as Resource
	    Resource resource;

	    String fileBasePath = "C:\\" + carpeta  + "\\";
	    Path path = Paths.get(fileBasePath + nombre);
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
	
	@RequestMapping(value="/enviarObservacion", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String enviar_observacion(@RequestParam("correoRechazo") String correo,
			@RequestParam("idEmailRechazado") String idEmail,
			@RequestParam("asuntoCorreoRechazo") String asunto_correo,
			@RequestParam("observacion") String observacion,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			
			Object[] parametros = { idEmail, observacion, correo,infoUser.getIdusuario()};
			
			int filas = bandeja.guardar_observacion(parametros);						
	        
			if(filas > 0) {
				
				HashMap<String, String> datos_correo =  new HashMap<>();			
				datos_correo.put("observacion", observacion);
				datos_correo.put("asunto", asunto_correo);			
									
			    correoService.sendEmail("w_tucto@hotmail.com","Solictud observada","correos/correo_rechazo", datos_correo,"");
				
				map.put("code", 1);
				map.put("mensaje", "Su observación fue notificado al usuario ");
			    json = new ObjectMapper().writeValueAsString(map);
				
			}else {
				map.put("code", 0);
				map.put("mensaje", "No se pudo observar la solicitud ");
			    json = new ObjectMapper().writeValueAsString(map);
			}		  
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;	
		
	}
	
	/// Llama a API'S
	
	@RequestMapping(value="/clasificarDocumentos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String clasificar_documentos(@RequestBody String idEmail) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			String datos = "{\"idEmail\":"+idEmail+"}";
			
			String resp = api.consumir_api("http://192.168.0.170:5000/clasificarDocumentos", datos);
			json = resp;
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;	
		
	}
	
	@RequestMapping(value="/clasificarXML", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String clasificar_xml(@RequestBody String idEmail) {
		
		String json = "";
		
		try {
			String datos = "{\"idEmail\":"+idEmail+"}";			
			String resp = api.consumir_api("http://192.168.0.170:5000/clasificarXML", datos);
			json = resp;
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;	
		
	}
	
	@RequestMapping(value="/validadorDocumentos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String validador_documentos(@RequestBody String idEmail) {
		
		String json = "";
		
		try {
			String datos = "{\"idEmail\":"+idEmail+"}";			
			String resp = api.consumir_api("http://192.168.0.170:5000/validadorServicio", datos);
			json = resp;
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;	
		
	}
}
