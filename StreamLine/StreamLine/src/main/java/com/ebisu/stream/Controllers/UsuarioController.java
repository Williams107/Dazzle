package com.ebisu.stream.Controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ebisu.stream.Models.UsuariosModel;
import com.ebisu.stream.POJOS.DatosUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")
@RequestMapping("Usuario")
public class UsuarioController {
	
	@Autowired
	private UsuariosModel usuario;
	
	@GetMapping("/panel")
	public String panel(Model model) {
		List<Map<String, Object>> tipo_usuarios = usuario.consultar_tipo_usuarios();
		List<Map<String, Object>> empresas = usuario.consultar_empresas();
		List<Map<String, Object>> modulos = usuario.consultar_modulos();
		
		model.addAttribute("tipo_usuarios", tipo_usuarios);
		model.addAttribute("empresas", empresas);
		model.addAttribute("modulos", modulos);
		return "usuarios/listaUsuarios";
	}
	
	@PostMapping(value="/listaUsuarios",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String lista_usuarios() {
		
		String json = "";
		HashMap<String, Object> map = new HashMap<>();			
		
		try {	
						
			List<Map<String, Object>> datos = usuario.consultar_lista_usuarios();
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
	
	@RequestMapping(value="/cambiarClave", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String cambiar_clave(@RequestParam("idUsuario_clave") String idUsuario,
			@RequestParam("clave_actual") String clave_actual,
			@RequestParam("nueva_clave") String nueva_clave) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!clave_actual.equals(nueva_clave)) {
				List<Map<String, Object>> datos = usuario.consultar_clave(idUsuario, getMD5(clave_actual));
				if(!datos.isEmpty()) {
					
					String nueva = getMD5(nueva_clave);
					Object[] params = { nueva, idUsuario};
					int resp = usuario.actualizar_clave(params);
					if(resp > 0) {
						map.put("code", 1);
						map.put("mensaje","Clave actualizada con exito");
						json = new ObjectMapper().writeValueAsString(map);
					}
					else {
						map.put("code", 0);
						map.put("mensaje","No se pudo actualizar su clave");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}
				else {
					map.put("code", 0);
					map.put("mensaje","Su clave actual es incorrecta");
					json = new ObjectMapper().writeValueAsString(map);
				}
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Su clave nueva debe ser diferente al actual");
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
	String actualizar_datos(@RequestParam("idusuario_actualizar") String idUsuario,
			@RequestParam("nombres_actualizar") String nombres,
			@RequestParam("apellido_actualizar") String apellido,
			@RequestParam("doc_actualizar") String nro_doc,
			@RequestParam("usuario_actualizar") String usuario_actualizar,
			@RequestParam("correo_actualizar") String correo,
			@RequestParam("empresa_actualizar") String empresa,
			@RequestParam("tipo_usuario") String tipo_usuario,
			@RequestParam("clave_actualizar") String clave_actualizar,
			@RequestParam("accesos") String accesos,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";		
		try {
			if(!idUsuario.equals("")) {
					
				Object[] params = { nombres, apellido, nro_doc, usuario_actualizar, correo, empresa, tipo_usuario, infoUser.getIdusuario(),accesos, idUsuario  };
				int resp = usuario.actualizar_datos_usuario(params);
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
				Object[] params = { nombres, apellido, nro_doc, usuario_actualizar, correo, empresa, tipo_usuario, infoUser.getIdusuario(), accesos, getMD5(clave_actualizar)  };
				int resp = usuario.guardar_datos_usuario(params);
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
			int resp  = usuario.actualizar_estado(params);
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
	
	public String getMD5(String texto) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(texto.getBytes());
		byte[] digest = messageDigest.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		
		return sb.toString();
	}
}
