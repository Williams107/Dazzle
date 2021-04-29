package com.ebisu.stream.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ebisu.stream.Models.ConfigModel;
import com.ebisu.stream.POJOS.DatosUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Config")
public class ConfigController {
	
	@Autowired
	private ConfigModel config;
	
	
	@GetMapping("/descorreo")
	public String descorreo(@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		String ruta = "";
		if(infoUser.getIdusuario() == 0 || infoUser == null)
		{
			ruta = "redirect:logout";
		}
		else {
			ruta = "configuracion/descorreo";
		}
		return ruta;
	}
	
	@PostMapping(value="/listaEmpresas",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String lista_empresas() {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {						
	       
			
			List<Map<String, Object>> empresas = config.consultar_empresas();						
	        
			if(!empresas.isEmpty()) {												        
				
				map.put("code", 1);
				map.put("datos", empresas);
			    json = new ObjectMapper().writeValueAsString(map);
				
			}else {
				map.put("code", 0);
				map.put("mensaje", "No se pudo guardar tipo de documento ");
			    json = new ObjectMapper().writeValueAsString(map);
			}		  
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
	@RequestMapping(value="/habilitarDesabilitar", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String habilitar_desabilitar(@RequestBody String data) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		String [] parametros = data.split(",");
		
		try {	
			Object[] params = { parametros[1], parametros[0]};
			int resp  = config.actualizar_estado(params);
			if(resp > 0) {									
				map.put("code", 1);
				map.put("mensaje","Se realizaron los cambios");
				json = new ObjectMapper().writeValueAsString(map);
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Nose pudo realizar los cambios");
				json = new ObjectMapper().writeValueAsString(map);
			}
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;	
		
	}
	
	@RequestMapping(value="/actualizarDatos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String actualizar_datos(@RequestParam("idEmp") String idEmp,
			@RequestParam("nombre") String nombre,
			@RequestParam("ruc") String ruc,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
			
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!idEmp.equals("")) {
					
				Object[] params = { nombre, ruc, infoUser.getIdusuario(), idEmp  };
				int resp = config.actualizar_datos_empresa(params);
				if (resp > 0) {
					map.put("code", 1);
					map.put("mensaje", "Datos actualizados con exito");
					json = new ObjectMapper().writeValueAsString(map);
				} else {
					map.put("code", 0);
					map.put("mensaje", "No se pudo actualizar sus datos");
					json = new ObjectMapper().writeValueAsString(map);
				}				
			}
			else {
				Object[] params = { nombre, ruc, infoUser.getIdusuario()  };
				int resp = config.guardar_datos_empresa(params);
				if (resp > 0) {
					map.put("code", 1);
					map.put("mensaje", "Datos guardado con exito");
					json = new ObjectMapper().writeValueAsString(map);
				} else {
					map.put("code", 0);
					map.put("mensaje", "No se pudo guardar sus datos");
					json = new ObjectMapper().writeValueAsString(map);
				}
			}			
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;		
	}
	
	@RequestMapping(value="/actualizarParametros", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String actualizar_parametros(@RequestParam("parametro_id_empresa") String idEmpresa,
			@RequestParam("ambiente") String ambiente,
			@RequestParam("mandante") String mandante,
			@RequestParam("sociedad") String sociedad,
			@RequestParam("servidor") String servidor,			
			@RequestParam("nro_ins") String nro_ins,
			@RequestParam("id_sis") String id_sis,
			@RequestParam("idioma") String idioma,
			@RequestParam("usuario_parametro") String usuario,
			@RequestParam("clave_parametro") String clave,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!idEmpresa.equals("")) {
				
				List<Map<String, Object>> datos_parametros = config.consultar_parametros(idEmpresa);
				if(!datos_parametros.isEmpty()) {
					Object[] params = { ambiente, mandante,sociedad,servidor,nro_ins,id_sis,idioma, infoUser.getIdusuario() , usuario, clave , idEmpresa  };
					int resp = config.actualizar_datos_parametros(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos actualizados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo actualizar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}				
				else {
					Object[] params = { ambiente, mandante,sociedad,servidor,nro_ins,id_sis,idioma, infoUser.getIdusuario(), idEmpresa, usuario, clave   };
					int resp = config.guardar_datos_parametros(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos guardados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo guardar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}
								
			}
			else {
				
				map.put("code", 0);
				map.put("mensaje", "No existe ID");
				json = new ObjectMapper().writeValueAsString(map);
				
			}
			
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;			
	}
	
	@RequestMapping(value="/actualizarSunat", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String actualizar_sunat(@RequestParam("sunat_id_empresa") String idEmpresa,
			@RequestParam("id_cliente") String id_cliente,
			@RequestParam("clave_sunat") String clave_sunat) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!idEmpresa.equals("")) {
				
				List<Map<String, Object>> datos_parametros = config.consultar_sunat(idEmpresa);
				if(!datos_parametros.isEmpty()) {
					Object[] params = { id_cliente, clave_sunat, idEmpresa  };
					int resp = config.actualizar_datos_sunat(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos actualizados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo actualizar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}				
				else {
					Object[] params = { id_cliente, clave_sunat, idEmpresa  };
					int resp = config.guardar_datos_sunat(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos guardados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo guardar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}
								
			}
			else {
				
				map.put("code", 0);
				map.put("mensaje", "No existe ID");
				json = new ObjectMapper().writeValueAsString(map);
				
			}
			
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;			
	}
	
	@RequestMapping(value="/actualizarCorreos", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String actualizar_correos(@RequestParam("correo_id_empresa") String idEmpresa,
			@RequestParam("correo") String correo,
			@RequestParam("carpeta") String carpeta,
			@RequestParam("clave_correo") String clave_correo,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!idEmpresa.equals("")) {
				
				List<Map<String, Object>> datos_parametros = config.consultar_correos(idEmpresa);
				if(!datos_parametros.isEmpty()) {
					Object[] params = { correo, carpeta, clave_correo, infoUser.getIdusuario(), idEmpresa  };
					int resp = config.actualizar_datos_correo(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos actualizados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo actualizar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}				
				else {
					Object[] params = { correo, carpeta, clave_correo, infoUser.getIdusuario(), idEmpresa };
					int resp = config.guardar_datos_correo(params);
					if (resp > 0) {
						map.put("code", 1);
						map.put("mensaje", "Datos guardados con exito");
						json = new ObjectMapper().writeValueAsString(map);
					} else {
						map.put("code", 0);
						map.put("mensaje", "No se pudo guardar sus datos");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}
								
			}
			else {
				
				map.put("code", 0);
				map.put("mensaje", "No existe ID");
				json = new ObjectMapper().writeValueAsString(map);
				
			}
			
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }		
			
		return json;			
	}
	
	@GetMapping("/entrenamiento")
	public String validacion(Model model) {
		List<Map<String, Object>> datos = config.consultar_tipo_documento();
		model.addAttribute("tipos", datos);
		return "configuracion/entrenamiento";
	}
	
	@GetMapping("/detalles/{id}")
	public String detalles(@PathVariable String id, Model model, @ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		String ruta = "";
		if(infoUser.getIdusuario() == 0 || infoUser == null)
		{
			ruta = "redirect:logout";
		}
		else {
			List<Map<String, Object>> datos_parametros = config.consultar_parametros(id);
			model.addAttribute("parametros", datos_parametros);
			List<Map<String, Object>> correos = config.consultar_correos(id);
			model.addAttribute("correos", correos);
			model.addAttribute("idEmpresa", id);
			List<Map<String, Object>> sunat = config.consultar_sunat(id);
			model.addAttribute("sunat", sunat);
			ruta = "configuracion/detalles";
		}
			
		return ruta;
	}
	
	@PostMapping("/guardarArchivo")
	@ResponseBody
	public String guardar_archivo(@RequestParam("tipo_documento") String tipo_documento,
			@RequestParam MultipartFile[] archivos) {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();    	
		
		try {	    	
			
	        String UPLOAD_DIR = "C:\\instaladores\\archivos_entrenar\\";
	        
	        int total_archivos = archivos.length;
	        int conteo_archivos = 0;
	        String archivo = "";
	        String ruta_local = "";
	        
	        for (MultipartFile f:archivos) {	        	
	        	int aleatorio = this.generarNumeroAleatorios(10);
	        	archivo = ""+aleatorio+"_"+f.getOriginalFilename();	        	
	        	ruta_local = UPLOAD_DIR+archivo;
	        	                       	        	
	        	Path path = Paths.get(UPLOAD_DIR, archivo);
		        Files.write(path, f.getBytes());		        
		        conteo_archivos += 1;	        
	        }
	        
	        if(total_archivos == conteo_archivos){
	        			        		
	        	map.put("code", 1);
				map.put("mensaje", "Archivos cargados con exito");					    
				json = new ObjectMapper().writeValueAsString(map);	        	             
            }
            else{
                                
                map.put("code", 0);
				map.put("mensaje", "No se pudo cargar todos los archivos, por favor verifique");
			    json = new ObjectMapper().writeValueAsString(map);                
            }
	        
	    }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }

	    return json;		
	}
	
	@PostMapping(value="/guardarTipoDocumento",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String guardar_tipo_documento(@RequestParam("documento") String documento) {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {						
	       
			Object[] parametros = { documento};
			
			int filas = config.guardar_tipo_documento(parametros);						
	        
			if(filas > 0) {												        
				
				map.put("code", 1);
				map.put("mensaje", "Datos guardados con exito ");
			    json = new ObjectMapper().writeValueAsString(map);
				
			}else {
				map.put("code", 0);
				map.put("mensaje", "No se pudo guardar tipo de documento ");
			    json = new ObjectMapper().writeValueAsString(map);
			}		  
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
	
	@PostMapping(value="/levantarProcesos",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String levantar_procesos(@RequestBody String idProceso) {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {	
				/*
			 	File file = new File("C:\\Users\\Usser\\Desktop\\MS_CONSULTA_SUNAT\\dist\\validarComprobante.exe");
			    ProcessBuilder processBuilder = new ProcessBuilder(file.getAbsolutePath());
			    processBuilder.directory(file.getParentFile());
			    Process process = processBuilder.start(); 	 
			    
			    int status = process.waitFor();
		        System.out.println("Status: " + status);
		        */
		        Runtime runTime = Runtime.getRuntime();
	            
	            String executablePath = "C:\\Users\\Usser\\Desktop\\MS_CONSULTA_SUNAT\\dist\\validarComprobante.exe";
	            
	            Process process = runTime.exec(executablePath);
	            int status = process.waitFor();
		        System.out.println("Status: " + status);
        }
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }						
		return json;
	}
	
	
	public int generarNumeroAleatorios(int n) {
	    int m = (int) Math.pow(10, n - 1);
	    return m + new Random().nextInt(9 * m);
	}
	
}
