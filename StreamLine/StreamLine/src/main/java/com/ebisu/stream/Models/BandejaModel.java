package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BandejaModel {

	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> consultar_correos(String consulta){
		
		String sql = "SELECT TOP 10 idEmail, asunto,leido, envia, cuerpo, archivos,fecha,numeroEmail, CAST(fecha AS time(7)) AS hora,  convert(nvarchar(10), fecha, 101) as dia, "
				+ "(select count(estado) FROM archivos_email WHERE estado=1 and idEmail=email.idEmail ) as cantidad_archivos,IIF(leido=0,'unread','read') as estado_leido,  "
				+ "IIF(verificado<>0,1,0) as validado, "
				+ "IIF(verificado=0,'PENDIENTE VALIDACIÓN',IIF(verificado=1,'ARCHIVOS COMPLETOS','FALTA ARCHIVOS') ) as estado_validacion, "
				+ "IIF(verificado=0,'warning', IIF(verificado=1,'success','danger') ) as label "
				+ "FROM email WHERE estado=1 "+consulta+" order by numeroEmail desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_correos_asunto(String asunto){
		
		String sql = "SELECT em.idEmail, em.asunto,em.leido, em.envia, em.cuerpo, em.archivos,fecha,numeroEmail, CAST(em.fecha AS time(7)) AS hora,  convert(nvarchar(10), em.fecha, 101) as dia, "
				+ "(select count(estado) FROM archivos_email WHERE estado=1 and idEmail=em.idEmail ) as cantidad_archivos,IIF(em.leido=0,'unread','read') as estado_leido,  "
				+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=em.idEmail )>0,1,0) as validado, "
				+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=em.idEmail )>0,'VALIDADO','PENDIENTE' ) as estado_validacion, "
				+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=em.idEmail )>0,'success','warning' ) as label "
				+ "FROM email as em full outer join validacion_xml_pdf as vx on em.idEmail=vx.idEmail "
				+ "WHERE em.estado=1 AND (coalesce(convert(nvarchar(max), vx.NumFact),'') + coalesce(convert(nvarchar(max), vx.ruc),'') + "
				+ "coalesce(convert(nvarchar(max), em.asunto),'') + coalesce(convert(nvarchar(max),em.cuerpo),'') "
				+ "+ coalesce(convert(nvarchar(max),em.envia),'')) like '%"+asunto+"%' order by em.numeroEmail desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_ruc_factura(String id) {

		String sql = "SELECT NumFact, ruc, nombre_empresa FROM validacion_xml_pdf WHERE estado=1 and ruc<>'NN' and idEmail=" + id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_filtro_correo(String consulta){
			
			String sql = "SELECT TOP 10 idEmail, asunto,leido, envia, cuerpo, archivos,fecha,numeroEmail, CAST(fecha AS time(7)) AS hora,  convert(nvarchar(10), fecha, 101) as dia, "
					+ "(select count(estado) FROM archivos_email WHERE estado=1 and idEmail=email.idEmail ) as cantidad_archivos,IIF(leido=0,'unread','read') as estado_leido,  "
					+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=email.idEmail )>0,1,0) as validado, "
					+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=email.idEmail )>0,'VALIDADO','PENDIENTE' ) as estado_validacion, "
					+ "IIF((select count(estado) FROM validacion_email WHERE estado=1 and idEmail=email.idEmail )>0,'success','warning' ) as label "
					+ "FROM email WHERE estado=1 "+consulta+" order by numeroEmail desc";
			List<Map<String, Object>> filas = jdbc.queryForList(sql);
			return filas;
	}
	
	public List<Map<String, Object>> consultar_correo_id(String id){
		
		String sql = "SELECT idEmail, asunto, envia, cuerpo, archivos,fecha,verificado,numeroEmail, CAST(fecha AS time(7)) AS hora,  convert(nvarchar(10), fecha, 101) as dia "
					+ "FROM email WHERE estado=1 and idEmail="+id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_total_correos(){
		
		String sql = "SELECT count(idEmail) as total_email FROM email WHERE estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_archivos_email(String id){
		
		String sql = "SELECT a.idArchivo, a.peso, a.nombreArchivo, a.carpeta, a.nombre_original, a.estado_descarga, "
				+ "a.validado, a.extension , v.etiqueta, v.estadoValidacion "
				+ "FROM archivos_email as a "
				+ "LEFT JOIN validacion_email as v ON a.idArchivo=v.idArchivo "
				+ "WHERE a.estado=1 and a.idEmail="+id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_documentos_formales(String id){
		
		String sql = "SELECT idEmail, estadoValidacion, observacion FROM validacion_documentos_formales "
				+ "WHERE estado=1 and idEmail="+id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_validacion_xml(String id){
		
		String sql = "select ec.desEstado, rs.idEmail, et.nombreEstado, dc.nombreEstado as estado_domicilio "
				+ "from respuesta_sunat as rs left join estadocomprobante as ec on rs.estadoCP=ec.codigo "
				+ "left join estado_contribuyente as et on rs.estadoRuc=et.codigo "
				+ "left join domicilio_contribuyente as dc on rs.condDomiRuc=dc.codigo "
				+ "WHERE rs.estado=1 and rs.idEmail="+id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	
	public List<Map<String, Object>> consultar_etiquetas(String id){
		
		String sql = "SELECT DISTINCT etiqueta FROM validacion_email WHERE estado=1 AND idEmail="+id;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public int guardar_leido(Object[] parametros){
		
		String sql = "INSERT INTO email_leido (idEmail, idUsuario) VALUES (?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
	
	public int guardar_observacion(Object[] parametros){
		
		String sql = "INSERT INTO email_rechazados (idEmail,mensaje,correo, idUsuario) VALUES (?,?,?,?)";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
	
	public int actualizar_estado_leido(Object[] parametros){
		
		String sql = "UPDATE email SET leido=1 WHERE estado=1 AND idEmail=?";
		int filas = jdbc.update(sql, parametros);
		return filas;
	}
}
