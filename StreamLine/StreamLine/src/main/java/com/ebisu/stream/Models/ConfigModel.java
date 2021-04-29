package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigModel {
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> consultar_tipo_documento(){
		
		String sql = "SELECT idTipo, nombre FROM tipo_archivo WHERE estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_correos(String idEmpresa){
		
		String sql = "select idCorreo, usuario , carpeta, clave, idEmpresa from correos where idEmpresa='"+idEmpresa+"' and estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_empresas(){
		
		String sql = "select idEmpresa, nombre, ruc, IIF(estado=1,'Activo','Inactivo') as estado_mensaje, estado from empresa ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_sunat(String id){
		
		String sql = "select idCredencial, client_id,client_secret, idEmpresa from credenciales_sunat where estado=1 and idEmpresa='"+id+"' ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_parametros(String idEmpresa){
		
		String sql = "select idParametro, ambiente, mandt, sociedad, serv, num_inst, id_sist, idioma, idEmpresa, usuario, clave from parametro_empresa where idEmpresa='"+idEmpresa+"' and estado=1  ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public int guardar_tipo_documento(Object[] parametros){
		String sql = "INSERT INTO tipo_archivo (nombre) VALUES (?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}	
	
	public int actualizar_estado(Object[] parametros) {

		String sql = "UPDATE empresa SET estado=? WHERE idEmpresa=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int actualizar_datos_empresa(Object[] parametros) {

		String sql = "UPDATE empresa SET nombre=?, ruc=?, idUsuario=? WHERE estado=1 AND idEmpresa=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int guardar_datos_empresa(Object[] parametros) {

		String sql = "INSERT INTO empresa (nombre, ruc, idUsuario) VALUES (?,?,?)";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int actualizar_datos_parametros(Object[] parametros) {

		String sql = "UPDATE parametro_empresa SET ambiente=?, mandt=?, sociedad=?, serv=?, num_inst=?, id_sist=?, idioma=?, idUsuario=?, usuario=?, clave=? WHERE estado=1 AND idEmpresa=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int guardar_datos_parametros(Object[] parametros){
		String sql = "INSERT INTO tipo_archivo (ambiente, mandt, sociedad, serv, num_inst, id_sist, idioma, idUsuario, idEmpresa, usuario, clave) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
	
	public int actualizar_datos_sunat(Object[] parametros) {

		String sql = "UPDATE credenciales_sunat SET client_id=?, client_secret=? WHERE estado=1 AND idEmpresa=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int guardar_datos_sunat(Object[] parametros){
		String sql = "INSERT INTO credenciales_sunat (client_id, client_secret, idEmpresa) VALUES (?,?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
	
	public int actualizar_datos_correo(Object[] parametros) {

		String sql = "UPDATE correos SET usuario=?, carpeta=?, clave=?, idUsuario=? WHERE estado=1 AND idEmpresa=?";
		int resp = jdbc.update(sql, parametros);
		return resp;
	}
	
	public int guardar_datos_correo(Object[] parametros){
		String sql = "INSERT INTO correos (usuario, carpeta, clave, idUsuario, idEmpresa) VALUES (?,?,?,?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
}
