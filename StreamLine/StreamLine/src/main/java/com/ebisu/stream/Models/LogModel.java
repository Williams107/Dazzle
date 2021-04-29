package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LogModel {
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> consultar_correos_rechazados(){
		
		String sql = "SELECT e.idEmail, e.correo,e.asunto, r.mensaje, convert(varchar, r.fechaRegistro, 20) as fecha FROM email as e, email_rechazados as r "
				+ "WHERE e.idEmail=r.idEmail and e.estado=1 and r.estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_log_descarga(){
		
		String sql = "SELECT e.idEmail, e.correo,e.asunto, r.mensaje,r.funcion, convert(varchar, r.fechaRegistro, 20) as fecha FROM email as e, error_descarga as r "
				+ "WHERE e.idEmail=r.idEmail and e.estado=1 and r.estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_log_validacion(){
		
		String sql = "SELECT e.idEmail, e.correo,e.asunto, r.mensaje, convert(varchar, r.fechaRegistro, 20) as fecha, a.nombre_original as archivo "
				+ "FROM email as e, archivos_email as a, error_validacion as r "
				+ "WHERE a.idArchivo=r.idArchivo and a.idEmail=e.idEmail and a.estado=1 and e.estado=1 and r.estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
}
