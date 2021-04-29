package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginModel {

	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> consultar_usuario(String usuario, String clave){
		
		String sql = "SELECT u.idUsuario, u.nombres, u.apellidos, u.nro_documento, u.usuario, u.correo, u.idEmpresa"
				+ ",u.accesos, u.idTipoUsuario, t.nombre as tipoUser FROM usuarios as u, tipo_usuario as t WHERE u.idTipoUsuario=t.idTipo and u.estado=1 and u.usuario='"+usuario+"' and u.clave='"+clave+"'";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_tipo_usuario(String usuario){
			
		String sql = "  SELECT e.serv, e.num_inst, e.mandt, e.idioma, u.idUsuario, u.nombres, u.apellidos, u.nro_documento, u.usuario, u.correo, u.idEmpresa "
				+ ",u.accesos, u.idTipoUsuario, t.nombre as tipoUser FROM usuarios as u, tipo_usuario as t, parametro_empresa as e "
				+ "WHERE u.idTipoUsuario=t.idTipo and u.idEmpresa=e.idEmpresa and u.estado=1 and u.usuario='"+usuario+"'";
			List<Map<String, Object>> filas = jdbc.queryForList(sql);
			return filas;
	}
}
