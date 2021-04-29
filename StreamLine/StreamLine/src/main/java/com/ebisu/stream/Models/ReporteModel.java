package com.ebisu.stream.Models;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReporteModel {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> consultar_correos(String consulta){
		
		String sql = "SELECT idEmail, asunto,leido, envia, archivos,numeroEmail, convert(varchar, fecha, 20) as fecha, "
				+ "archivos_descargados,  "
				+ "IIF(verificado<>0,1,0) as validado, "
				+ "IIF(verificado=0,'PENDIENTE VALIDACIÓN',IIF(verificado=1,'ARCHIVOS COMPLETOS','FALTA ARCHIVOS') ) as estado_validacion "
				+ "FROM email WHERE estado=1 "+consulta+" order by numeroEmail desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
public List<Map<String, Object>> consultar_filtro(String consulta){
		
		String sql = "SELECT idEmail, asunto,leido, envia, archivos,numeroEmail, convert(varchar, fecha, 20) as fecha, "
				+ "archivos_descargados,  "
				+ "IIF(verificado<>0,1,0) as validado, "
				+ "IIF(verificado=0,'PENDIENTE VALIDACIÓN',IIF(verificado=1,'ARCHIVOS COMPLETOS','FALTA ARCHIVOS') ) as estado_validacion "
				+ "FROM email WHERE estado=1 "+consulta+" order by numeroEmail desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> obtener_ind_retencion(String id){
		
		String sql = "SELECT indicador_retencion, concat(indicador_retencion,' - ', descripcion) as descripcion from indretenout where estado=1 and indicador_tipo_retencion='"+id+"'";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	
	
	public List<Map<String, Object>> obtener_retencion(String consulta){
		
		String sql = "SELECT indicador_tipo_retencion, concat(indicador_tipo_retencion,' - ',descripcion) as descripcion from retenout where estado=1 "+consulta;
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> obtener_datos_consulta_Sap(String idEmail){
		String sql = " select distinct ncuenta_proveedor_acreedor, sociedad from verificacion_contabilizacion where idEmail='"+idEmail+"' ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> obtener_datos_consulta(String idEmail){
		String sql = " select distinct  convert(varchar, vx.idEmail) as IDDAZ, iif(ve.etiqueta='HES','HES','EM') AS TIPDO, vx.ruc as STCD1, vx.moneda as WAERS, vx.cod_compra as BLART, vx.NumFact as XBLNR, "
				+ "convert(varchar,vx.fecha_emision,112) as BLDAT,convert(varchar,( convert(real,vx.monto) - convert(real,vx.igv))) as WRBTR, pe.sociedad as BUKRS, "
				+ "RIGHT('0000000000'+SUBSTRING(ve.contenido, PATINDEX('%[0-9]%', ve.contenido),PATINDEX('%[0-9][^0-9]%', ve.contenido + 't') - PATINDEX('%[0-9]%', ve.contenido) + 1 ),10) as BELNR "
				+ "from validacion_xml_pdf as vx, empresa as e, parametro_empresa as pe, validacion_email as ve  "
				+ "where vx.ruc_receptor=e.ruc and e.idEmpresa=pe.idEmpresa and vx.idEmail=ve.idEmail "
				+ " and vx.tipo_xml='FACTURA' and ve.etiqueta in ('HES','MIGO') and vx.idEmail='"+idEmail+"' ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	public List<Map<String, Object>> consultar_xml_sap(String idEmpresa){
		String sql = "select vx.igv as WMWST1, vc.idEmail, vc.tipo_documento AS TIPDO, vc.ruc_identificacion_fiscal as STCD1, "
				+ "vc.clave_moneda as WAERS, vx.cod_compra as BLART, vc.numero_documento_sunat as XBLNR, "
				+ "convert(varchar,CAST(vx.fecha_emision as date),112) as BLDAT, convert(varchar,CAST(vx.fecha_emision as date),23) as fecha_doc, convert(varchar,( convert(real,vx.monto) - convert(real,vx.igv))) as RMWWR, vc.sociedad as BUKRS, "
				+ "vc.numero_documento as BELNR, vx.nombre_empresa, vx.descripcion, "
				+ "vc.posicion_documento as BUZEI, VC.ejercicio_documento AS GJAHR, VC.numero_documento_compra AS EBELN, "
				+ " IIF(vc.fecha_contabilizacion='0000-00-00','', convert(varchar,CAST(vc.fecha_contabilizacion as date),112) ) as BUDAT, vc.indicador_iva as MWSKZ, vc.ncuenta_proveedor_acreedor AS LIFNR, "
				+ "vc.indicador_tipo_retencion as WITHT, vc.indicador_retencion as WT_WITHCD, vc.fecha_base_cal_venc AS ZFBDT, "
				+ "vc.condicion_pago AS ZTERM "
				+ "from validacion_xml_pdf as vx, verificacion_contabilizacion as vc "
				+ "where vx.idEmail=vc.idEmail and vc.estado=1  AND vx.validacion_sap=1 and vx.idEmail='"+idEmpresa+"'";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	
	public List<Map<String, Object>> consultar_montos_sap(String idEmail){
		String sql = " select distinct ve.etiqueta, vf.tipo_documento, vf.numero_documento, vf.numero_documento_compra, "
				+ "sum(distinct convert(real,vf.valor_neto)) as valor_neto,  vf.sociedad, vf.idEmail, vf.indicador_iva, vf.posicion_documento  "
				+ "from verificacion_contabilizacion as vf, validacion_email as ve where vf.idEmail=ve.idEmail and "
				+ "ve.etiqueta in ('HES','MIGO') and vf.estado=1 and vf.numero_mensaje='001' and vf.idEmail='"+idEmail+"' "
				+ "group by ve.etiqueta, vf.tipo_documento, vf.numero_documento, vf.numero_documento_compra, vf.sociedad,vf.indicador_iva, vf.idEmail, vf.posicion_documento ";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	
	
	/*public List<Map<String, Object>> obtener_reporte(String consulta){
		
		String sql = "  select distinct em.fecha, vp.idEmail, vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
				+ "IIF(tipo_xml='GUIA','XML ES DE GUIA',IIF(vp.idFactura>0,'VALIDO','XML NO COINCIDE CON PDF')) AS estado_validacion, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='HES' and estado=1) as HES, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='MIGO' and estado=1) as MIGO, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='FACTURA' and estado=1) as FACTURA, "
				+ "(select top 1 IIF(estadoValidacion=1,1,0) as estado from validacion_xml where estado=1 and idArchivo=vp.idArchivo) as XMLS, "
				+ "(select top 1 estadoCp from respuesta_sunat where idEmail=vp.idEmail) as validacion_sunat, "
				+ "(select top 1 sociedad from parametro_empresa where idEmpresa=e.idEmpresa) as nombre, "
				+ "vp.validacion_sap as SAP "
				+ " from validacion_xml_pdf as vp  "
				+ " inner join empresa as e on vp.ruc_receptor=e.ruc "
				+ " inner join email as em on vp.idEmail=em.idEmail "
				+ " where vp.estado=1 and "
				+ " vp.tipo_xml='FACTURA' "+consulta+ " order by vp.idEmail desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
}*/
	
public List<Map<String, Object>> obtener_reporte(String consulta){
		
		String sql = " select distinct vp.fecha_emision, vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
				+ "e.nombre, vc.numero_mensaje, vc.contabilizado_sap, "
				+ "max(vp.idEmail) as idEmail, max(vdf.estadoValidacion) as documentos,max(rs.estadoCp) as validacion_sunat, "
				+ "max(vp.validacion_sap) as SAP "
				+ " from validacion_xml_pdf as vp  "
				+ " inner join empresa as e on vp.ruc_receptor=e.ruc "
				+ " left join validacion_documentos_formales as vdf on vp.idEmail=vdf.idEmail "
				+ " left join respuesta_sunat as rs on vp.idEmail=rs.idEmail "
				+ " left join verificacion_contabilizacion as vc on (vp.NumFact=vc.numero_documento_sunat and vp.ruc=vc.ruc_identificacion_fiscal) "
				+ "where vp.estado=1 and "
				+ " vp.tipo_xml='FACTURA' "+consulta
				+ " group by vp.fecha_emision, vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
				+ "e.nombre, vc.numero_mensaje, vc.contabilizado_sap  "
				+ " order by vp.fecha_emision desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
}

public List<Map<String, Object>> obtener_reporte2(String consulta){
	
	String sql = " select distinct em.fecha, vp.idEmail, vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
			+ "IIF(tipo_xml='GUIA','XML ES DE GUIA',IIF(vp.idFactura>0,'VALIDO','XML NO COINCIDE CON PDF')) AS estado_validacion, "
			+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='HES' and estado=1) as HES, "
			+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='MIGO' and estado=1) as MIGO, "
			+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='FACTURA' and estado=1) as FACTURA, "
			+ "(select top 1 IIF(estadoValidacion=1,1,0) as estado from validacion_xml where estado=1 and idArchivo=vp.idArchivo) as XMLS, "
			+ "(select top 1 estadoCp from respuesta_sunat where idEmail=vp.idEmail) as validacion_sunat, "
			+ "(select top 1 sociedad from parametro_empresa where idEmpresa=e.idEmpresa) as nombre, "
			+ "vp.validacion_sap as SAP, "
			+ "(select top 1 numero_mensaje from verificacion_contabilizacion where numero_documento_sunat=vp.NumFact and idEmail=vp.idEmail ) as mensaje_sap "
			+ " from validacion_xml_pdf as vp  "
			+ " inner join empresa as e on vp.ruc_receptor=e.ruc "
			+ " inner join email as em on vp.idEmail=em.idEmail "
			+ " where vp.estado=1 and "
			+ " vp.tipo_xml='FACTURA' "+consulta+" order by vp.idEmail desc ";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}




public List<Map<String, Object>> obtener_detalle_correo(String idEmail){
	
	String sql = " select numeroEmail, convert(varchar, fecha, 22) as fecha, correo from email where idEmail='"+idEmail+"' ";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}

public List<Map<String, Object>> obtener_detalle_sunat(String idEmail){
	
	String sql = " select ec.nombreEstado as est_comp, dc.nombreEstado as est_dom, et.nombreEstado as est_cont "
			+ "  from respuesta_sunat as rs, estadocomprobante as ec, "
			+ "  domicilio_contribuyente dc, "
			+ "  estado_contribuyente as et where rs.estadoCp=ec.codigo and "
			+ "  rs.estadoRuc=et.codigo and rs.condDomiRuc=dc.codigo and "
			+ "  rs.idEmail='"+idEmail+"'";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}

public List<Map<String, Object>> obtener_detalle_etiquetas(String idEmail){
	
	String sql = "  select ae.carpeta, ae.nombreArchivo, ve.etiqueta "
			+ "  from validacion_email as ve, archivos_email as ae "
			+ "  where ve.idArchivo=ae.idArchivo and ve.idEmail='"+idEmail+"'";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}


public List<Map<String, Object>> buscar_archivo(String numeroDocumento, String id){
	String sql = " select top 1 archivo from vista_miro where  "
			+ " num_doc='"+numeroDocumento+"' and idEmail='"+id+"' ";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}

public List<Map<String, Object>> obtener_ruta_factura(String idEmail){
	
	String sql = " select top 1 ae.carpeta, ae.nombreArchivo from validacion_email as ve, archivos_email as ae where ve.idArchivo=ae.idArchivo and ae.idEmail='"+idEmail+"' "
			+ "and ve.etiqueta='FACTURA' ";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}



public List<Map<String, Object>> obtener_detalle_factura(String idEmail){
	
	String sql = "  select vx.idEmail, vx.NumFact, ae.carpeta, ae.nombreArchivo, (convert(real,vx.monto) - convert(real,vx.igv)) as monto, "
			+ "(select top 1 concat(numero_mensaje,'-', texto_mensaje) from verificacion_contabilizacion where idEmail=vx.idEmail) as mensaje "
			+ "from validacion_xml_pdf as vx, "
			+ "archivos_email as ae "
			+ "where vx.idArchivo=ae.idArchivo and vx.NumFact<>'NN' and vx.idEmail='"+idEmail+"'";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}

public List<Map<String, Object>> obtener_detalle_hes_migo(String idEmail){
	
	String sql = "select distinct ve.etiqueta, vf.tipo_documento, vf.numero_documento, sum(distinct convert(real, vf.valor_neto)) as neto,  vf.sociedad, vf.idEmail, vf.numero_mensaje, vf.texto_mensaje "
			+ "from verificacion_contabilizacion as vf, validacion_email as ve where "
			+ "vf.idEmail=ve.idEmail  and ve.etiqueta in ('HES','MIGO') and vf.estado=1 and vf.idEmail='"+idEmail+"' "
			+ "group by vf.numero_documento, vf.numero_documento_compra, vf.idEmail, vf.tipo_documento, vf.sociedad,"
			+ "ve.etiqueta, vf.numero_mensaje, vf.texto_mensaje ";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}

public List<Map<String, Object>> obtener_detalle_documentos(String idEmail){
	
	String sql = "select top 1 "
			+ "(select top 1 ae.idArchivo from archivos_email as ae, validacion_email ve "
			+ "where ae.idArchivo=ve.idArchivo and ae.estado=1 and ve.estado=1 and "
			+ "ae.idEmail=e.idEmail and ve.etiqueta='HES' ) as HES, "
			+ "(select TOP 1 ae.idArchivo from archivos_email as ae, validacion_email ve "
			+ "where ae.idArchivo=ve.idArchivo and ae.estado=1 and ve.estado=1 and ae.idEmail=e.idEmail and "
			+ "ve.etiqueta='MIGO') as MIGO, "
			+ "(select TOP 1 ae.idArchivo from archivos_email as ae, validacion_email ve "
			+ "where ae.idArchivo=ve.idArchivo and ae.estado=1 and ve.estado=1 and ae.idEmail=e.idEmail and "
			+ "ve.etiqueta='FACTURA' ) as FACT, "
			+ "(select TOP 1 ae.idArchivo from archivos_email as ae, validacion_email ve "
			+ "where ae.idArchivo=ve.idArchivo and ae.estado=1 and ve.estado=1 and ae.idEmail=e.idEmail and "
			+ "ve.etiqueta='CONFORMIDAD' ) as CONF, "
			+ "(select TOP 1 ae.idArchivo from archivos_email as ae, validacion_email ve "
			+ "where ae.idArchivo=ve.idArchivo and ae.estado=1 and ve.estado=1 and ae.idEmail=e.idEmail and "
			+ "ve.etiqueta='ORDEN_COMPRA' ) as OC, "
			+ "(select TOP 1 ae.idArchivo from archivos_email as ae, validacion_xml vx where "
			+ "ae.idArchivo=vx.idArchivo and ae.estado=1 and vx.estado=1 and ae.idEmail=e.idEmail ) as XMLS , "
			+ "e.idEmail, "
			+ "vx.ruc, vx.NumFact "
			+ "from email as e, validacion_xml_pdf as vx where e.estado = 1 and e.idEmail=vx.idEmail AND vx.tipo_xml='FACTURA' "
			+ "and e.idEmail='"+idEmail+"'";
	List<Map<String, Object>> filas = jdbc.queryForList(sql);
	return filas;
}
	
	

	
	public List<Map<String, Object>> obtener_reporte_nocontabilizados(){
		
		String sql = " select distinct max(vp.idEmail) as idEmail, vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
				+ "c.tipo_documento, c.sociedad, vp.igv, vp.monto, vp.fecha_emision as emision , convert(varchar, vp.fecha_emision, 103) as fecha_emision, c.indicador_iva, c.tipo_documento "
				+ "from validacion_xml_pdf as vp "
				+ "inner join verificacion_contabilizacion as c on vp.idEmail=c.idEmail "
				+ "where vp.estado=1 and "
				+ "(select top 1 estadoValidacion from validacion_xml where estado=1 and idArchivo=vp.idArchivo)<>0 and "
				+ "(select top 1 estadoValidacion from validacion_documentos_formales where estado=1 and idEmail=vp.idEmail)=1 and "
				+ "vp.tipo_xml='FACTURA' and vp.validacion_sap=1 and c.estado=1 and c.numero_mensaje='001' and c.contabilizado_sap=0 "
				+ "group by vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, "
				+ " c.tipo_documento, c.sociedad, vp.igv, vp.monto, vp.fecha_emision,  c.indicador_iva, c.tipo_documento order by emision desc";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	
	//-------------------------------------///
	public List<Map<String, Object>> verificar_contabilizacion(){
		
		String sql = "select vp.tipo_xml, vp.NumFact, vp.ruc, vp.nombre_empresa, vp.ruc_receptor, e.nombre, "
				+ "IIF(tipo_xml='GUIA','XML ES DE GUIA',IIF(vp.idFactura>0,'VALIDO','XML NO COINCIDE CON PDF')) AS estado_validacion, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='HES' and estado=1) as HES, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='MIGO' and estado=1) as MIGO, "
				+ "(select top 1 estado from validacion_email where idEmail=vp.idEmail and etiqueta='FACTURA' and estado=1) as FACTURA, "
				+ "'1' as XMLS, "
				+ "'0' as SAP "
				+ "from validacion_xml_pdf as vp full outer join empresa as e on vp.ruc_receptor=e.ruc where vp.estado=1";
		List<Map<String, Object>> filas = jdbc.queryForList(sql);
		return filas;
	}
	//-------------------------------------///
}
