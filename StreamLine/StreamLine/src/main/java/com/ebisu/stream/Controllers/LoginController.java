package com.ebisu.stream.Controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ebisu.stream.Models.LoginModel;
import com.ebisu.stream.POJOS.DatosUsuario;
import com.ebisu.stream.Services.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("DatosUsuario")

public class LoginController {
	
	@Autowired
	private LoginModel login;
	
	@Autowired
    private ApiService api;

	@ModelAttribute("DatosUsuario")
	public DatosUsuario getDatosUsuario() {
		return new DatosUsuario();
	}
	
	@GetMapping(value= {"/","/login","/home",""})
	public String login() {
		
		return "login";
	}
	
	@RequestMapping(value="/validarCredenciales", method = RequestMethod.POST, headers="Accept=application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	public @ResponseBody
	String validar_credenciales(@RequestParam("usuario") String usuario,
			@RequestParam("clave") String clave,
			@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		
		HashMap<String, Object> map = new HashMap<>();
		String json = "";
		
		try {
			if(!usuario.equals("") && !clave.equals("")) {
				
				List<Map<String, Object>> tipo_user = login.consultar_tipo_usuario(usuario);
				
				if(!tipo_user.isEmpty()) {
				
				if(((int) tipo_user.get(0).get("idTipoUsuario")) == 3) {
					try {
						String datos = "{\"host\":'"+((String) tipo_user.get(0).get("serv"))+
								"',\"cliente\":'"+((String) tipo_user.get(0).get("mandt"))+
								"',\"sysnr\":'"+((String) tipo_user.get(0).get("num_inst"))+
								"',\"user\":'"+usuario+
								"',\"pass\":"+clave+
								",\"lang\":'"+((String) tipo_user.get(0).get("idioma"))+"'}";
						
						System.out.println(datos);
						
						String resp = api.consumir_api("http://localhost:8080/cronSap/api/dazzle/validar/usuario", datos);
						
						JSONObject jsonObject = new JSONObject(resp);	
						if(jsonObject.getInt("code")==1) {
							
							infoUser.setIdusuario((int) tipo_user.get(0).get("idUsuario"));
							infoUser.setNombres((String) tipo_user.get(0).get("nombres"));
							infoUser.setApellidos((String) tipo_user.get(0).get("apellidos"));
							infoUser.setNroDocumentos((String) tipo_user.get(0).get("nro_documento"));
							infoUser.setUsuario((String) tipo_user.get(0).get("usuario"));
							infoUser.setCorreo((String) tipo_user.get(0).get("correo"));
							infoUser.setIdEmpresa((int) tipo_user.get(0).get("idEmpresa"));
							infoUser.setAccesos((String) tipo_user.get(0).get("accesos"));
							infoUser.setTipoUsuario((int) tipo_user.get(0).get("idTipoUsuario"));
							infoUser.setNombreTipoUsuario((String) tipo_user.get(0).get("tipoUser"));
							infoUser.setClave(clave);
							map.put("code", 1);
							map.put("datos",datos);
							json = new ObjectMapper().writeValueAsString(map);
						}
						else {
							map.put("code", 0);
							map.put("mensaje","Ingrese su clave correcta");
							json = new ObjectMapper().writeValueAsString(map);
						}
			        }
			        catch(Exception ex) {
			        	
			        	System.out.println(ex.getMessage());
			        }	
					
				}else{
					List<Map<String, Object>> datos = login.consultar_usuario(usuario, getMD5(clave));
					if(!datos.isEmpty()) {
						
						infoUser.setIdusuario((int) datos.get(0).get("idUsuario"));
						infoUser.setNombres((String) datos.get(0).get("nombres"));
						infoUser.setApellidos((String) datos.get(0).get("apellidos"));
						infoUser.setNroDocumentos((String) datos.get(0).get("nro_documento"));
						infoUser.setUsuario((String) datos.get(0).get("usuario"));
						infoUser.setCorreo((String) datos.get(0).get("correo"));
						infoUser.setIdEmpresa((int) datos.get(0).get("idEmpresa"));
						infoUser.setAccesos((String) datos.get(0).get("accesos"));
						infoUser.setTipoUsuario((int) datos.get(0).get("idTipoUsuario"));
						infoUser.setNombreTipoUsuario((String) datos.get(0).get("tipoUser"));
						infoUser.setClave("");
						map.put("code", 1);
						map.put("datos",datos);
						json = new ObjectMapper().writeValueAsString(map);
					}
					else {
						map.put("code", 0);
						map.put("mensaje","Nose encontro ningún usuario");
						json = new ObjectMapper().writeValueAsString(map);
					}
				}				
								
				}else {
					map.put("code", 0);
					map.put("mensaje","Ingrese su usuario y clave correctos");
					json = new ObjectMapper().writeValueAsString(map);
				}	
			}
			else {
				map.put("code", 0);
				map.put("mensaje","Ingrese su usuario y clave");
				json = new ObjectMapper().writeValueAsString(map);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			//System.out.println(ex.getMessage());
		}
		return json;	
	}
	
	@GetMapping("dashboard")
	public String dashboard(@ModelAttribute("DatosUsuario") DatosUsuario infoUser) {
		String ruta = "";
		if(infoUser.getIdusuario() == 0 || infoUser == null)
		{
			ruta = "redirect:logout";
		}
		else {
			ruta = "dashboard";
			
		}
		return ruta;
	}
	
	@GetMapping("/logout")
	public String cerrar_sesion(HttpServletRequest request,
            SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:login";
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
