package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuariosModel {
	@Autowired
	private JdbcTemplate jdbc;

	public List<Map<String, Object>> consultar_lista_usuarios() {

		String sql = "select u.idUsuario, u.idEmpresa, u.idTipoUsuario, u.estado, u.nombres, u.apellidos, u.nro_documento, u.usuario, u.correo, u.accesos, "
				+ "t.nombre as tipo_usuario, e.nombre as empresa, IIF(u.estado=1,'Activo','Inactivo') as estado_usuario from usuarios as u, empresa as e "
				+ " ,tipo_usuario as t "
				+ "where u.idEmpresa=e.idEmpresa and u.idTipoUsuario=t.idTipo and e.estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}

	public List<Map<String, Object>> consultar_clave(String idUsuario, String clave) {

		String sql = "select idUsuario from usuarios where idUsuario=" + idUsuario + " and clave='" + clave + "'";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_tipo_usuarios() {

		String sql = "select idTipo, nombre from tipo_usuario where estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_empresas() {

		String sql = "select idEmpresa, nombre from empresa where estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_modulos() {

		String sql = "select idModulo, nombre from modulos where estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}

	public int actualizar_clave(Object[] parametros) {

		String sql = "UPDATE usuarios SET clave=? WHERE estado=1 AND idUsuario=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int actualizar_datos_usuario(Object[] parametros) {

		String sql = "UPDATE usuarios SET nombres=?, apellidos=?, nro_documento=?, usuario=?, correo=?, idEmpresa=?, idTipoUsuario=?, idUsuarioRegistrador=?, accesos=? WHERE estado=1 AND idUsuario=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int guardar_datos_usuario(Object[] parametros){
			
		String sql = "INSERT INTO usuarios (nombres, apellidos, nro_documento, usuario, correo, idEmpresa, idTipoUsuario, idUsuarioRegistrador, accesos, clave) VALUES (?,?,?,?,?,?,?,?,?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}

	public int actualizar_estado(Object[] parametros) {

		String sql = "UPDATE usuarios SET estado=? WHERE idUsuario=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}

}
