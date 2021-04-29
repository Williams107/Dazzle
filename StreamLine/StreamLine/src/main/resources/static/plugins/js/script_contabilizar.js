ruta_sistema = "/Dazzle";

$(document).ready(function() {

	tabla_contabilizar = $('#tabla_contabilizar').DataTable({
		dom: 'Bfrtip',
		"language": {
			"url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
		},
		columns: [
			/*{
				data: null,
				render: function(data, type, row, meta) {
					return '<button class="btn btn-info" data-toggle="modal" data-target="#modal_detalle" onClick="mostrar_correo(' + data["idEmail"] + ')"><i class="fa fa-eye"></i></button>';
				}
			},*/
			{
                "className":      'details-control',
                "orderable":      false,
                "data":           null,
                "defaultContent": ''
            },
			{
				data:"tipo_documento"
			},
			{ data: "sociedad" },
			{ 
				data: null,
				render: function(){
					return ""
				}
			},
			{ 
				data: null,
				render: function(){
					return ""
				}	
			},
			{ data: "NumFact" },
			{ data:"fecha_emision"},
			{ 
				data: null,
				render: function(){
					return ""
				}	
			},
			{ 
				data: null,
				render: function(data, type, row, meta){
					total = (parseFloat(data['monto']) - parseFloat(data['igv']));
					return total.toFixed(2);
				}
			},			
			{ data:"igv"},
			{ 
				data: "monto"
			},
			{ data:"indicador_iva"},
			{ 
				data: null,
				render: function(){
					return ""
				}
			}
		],
		rowId: function(a) {
			return 'S'+a.idEmail;
		},
		buttons: [{ text: "Descargar Excel", extend: 'excel', className: 'btn-excel' },
		{
			text: 'Descargar PDF',
			className: 'btn-pdf',
			extend: 'pdfHtml5',
			filename: 'CORREOS',
			orientation: 'landscape', //portrait
			pageSize: 'A4', //A3 , A5 , A6 , legal , letter
			exportOptions: {
				columns: ':visible',
				search: 'applied',
				order: 'applied'
			},
			customize: function(doc) {
				doc.content.splice(0, 1);
				var now = new Date();
				var jsDate = now.getDate() + '-' + (now.getMonth() + 1) + '-' + now.getFullYear();

				var logo = '';
				doc.pageMargins = [20, 60, 20, 30];
				doc.defaultStyle.fontSize = 9;
				doc.styles.tableHeader.fontSize = 10;
				doc['header'] = (function() {
					return {
						columns: [

							{
								alignment: 'left',
								italics: true,
								text: 'EXPEDIENTES',
								fontSize: 18,
								margin: [10, 0]
							},
							{
								alignment: 'right',
								fontSize: 14,
								text: 'DAZZLE'
							}
						],
						margin: 20
					}
				});
				doc['footer'] = (function(page, pages) {
					return {
						columns: [
							{
								alignment: 'left',
								text: ['Descargado el: ', { text: jsDate.toString() }]
							},
							{
								alignment: 'right',
								text: ['pagina ', { text: page.toString() }, ' of ', { text: pages.toString() }]
							}
						],
						margin: 20
					}
				});
				var objLayout = {};
				objLayout['hLineWidth'] = function(i) { return .5; };
				objLayout['vLineWidth'] = function(i) { return .5; };
				objLayout['hLineColor'] = function(i) { return '#aaa'; };
				objLayout['vLineColor'] = function(i) { return '#aaa'; };
				objLayout['paddingLeft'] = function(i) { return 4; };
				objLayout['paddingRight'] = function(i) { return 4; };
				doc.content[0].layout = objLayout;
			}
		}
		]
	});
	
    $('#tabla_contabilizar tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = tabla_contabilizar.row( tr );
		
		$("#tabla_contabilizar tbody tr").removeClass('shown');
		//tabla_contabilizar.row().child.hide();
				
		$(".no-padding").remove();
 
        if ( row.child.isShown() ) {
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            row.child( format(row.data()),'no-padding' ).show();
            tr.addClass('shown');
			obtener_datos(row.data());
        }
			
    } );

});
//---------------------------------------------//



function contabilizar_factura() {

	var values_iva = $("input[name='indicador_iva_hes[]']")
		.map(function() { return $(this).val(); }).get();

	var conteo_iva = 0;

	if (values_iva.length > 0) {

		for (var i = 0; i < values_iva.length; i++) {
			if (values_iva[i] != "") {
				conteo_iva += 1;
			}
		}
		
		if (conteo_iva == values_iva.length) {
			if ($("#moneda_conta").val() != "" && $("#fecha_vencimiento").val() != "" && $("#fecha_conta").val() != "") {

				var values_hes = $("input[name='n_doc[]']")
					.map(function() { return $(this).val(); }).get();

				var values_ped = $("input[name='n_ped[]']")
					.map(function() { return $(this).val(); }).get();

				var values_doc = $("input[name='pos_doc[]']")
					.map(function() { return $(this).val(); }).get();
				
				let formData = [];
				
				for (var i = 0; i < values_hes.length; i++) {
										
					var variables = {
						usuario: $("#usuario").val(),
						clave: $("#clave").val(),
						tipDoc: $("#tipDoc").val(),
						nro_doc_sunat: $("#nro_doc_sunat").val(),
						tip_doc_sunat: $("#tip_doc_sunat").val(),
						nro_doc: values_hes[i],
						pos_doc: values_doc[i],
						ejercicio: $("#ejercicio").val(),
						num_pedido: values_ped[i],
						fecha_fact: $("#fecha_fact").val(),
						fecha_conta: moment($("#fecha_conta").val()).format('YYYYMMDD'),
						sociedad: $("#sociedad").val(),
						clave_moneda: $("#clave_moneda").val(),
						importe_fact: $("#importe_fact").val(),
						importe_imp: $("#importe_imp").val(),
						indicador_imp: values_iva[i],
						cod_proveedor: $("#cod_proveedor").val(),
						indicador_tipo_ret: $("#indicador_tipo_ret").val(),
						indicador_ret: $("#indicador_ret").val(),
						fecha_vencimiento: moment($("#fecha_vencimiento").val()).format('YYYYMMDD'),
						clave_condicion_pago: $("#cond_pago_conta").val(),
						texto_cabecera: $("#texto_cabecera").val()
					};											
					formData.push(variables);			
				}
				
				$.ajax({
						contentType: "application/json",
						url: "http://192.168.160.40:8080/cronSap/api/dazzle/set/document",
						dataType: 'json',
						method: "post",			
						data: JSON.stringify(formData),
						beforeSend: function() {
							$("#fondo").css("display", "block");
						},
						error: function() {
							$("#fondo").css("display", "none");
						},
						success: function(data) {
							$("#fondo").css("display", "none");
							alert(data['mensaje']);
							if(data['code'] == 1){
								$("#btn_contabilizar").css('display','none');
								color = "#AEEB95";
								$("#tabla_input1, #tabla_input2").css('background',color);
								$("#S"+$("#id_correo").val()).css('background',color);
								$("#doc_sap_conta").val(data['doc_sap']);
							}							
						}
					});				
			}
			else {
				alert("INGRESE LOS CAMPOS OBLIGATORIOS");
			}
		}
		else {
			alert("INGRESE INDICADOR");
		}
	}
	else {
		alert("INGRESE INDICADOR");
	}
}


/* Formatting function for row details - modify as you need */
function format (  ) {
	html = $("#formulario_contabilizar").html();
	return html;
}


//----rellenar--///

function reporte() {
	$.ajax({
		url: ruta_sistema+"/Contabilizar/obtenerReporte/",
		type: "POST",
		contentType: "text/plain",
		dataType: 'json',
		beforeSend: function() {
			$("#fondo").css("display", "block");
		},
		error: function() {
			$("#fondo").css("display", "none");
			alert("Hubo un problema, Intentelo de nuevo");
		},
		success: function(data) {
			$("#fondo").css("display", "none");
			if (data['code'] == 1) {
				tabla_contabilizar.clear().draw();
				tabla_contabilizar.rows.add(data['datos']).draw();
			}
		}
	});
}

function poblar_retenciones() {
	$.ajax({
		url: ruta_sistema+"/Contabilizar/obtenerRetenciones/",
		type: "POST",
		data: $("#indicador_tipo_ret").val(),
		contentType: "text/plain",
		dataType: 'json',
		beforeSend: function() {
			$("#fondo").css("display", "block");
			$("#indicador_ret").html("");
		},
		error: function() {
			$("#fondo").css("display", "none");
			alert("Hubo un problema, Intentelo de nuevo");
		},
		success: function(data) {
			$("#fondo").css("display", "none");
			if (data['code'] == 1) {
				
				var tam = data['datos'].length;
				
				if(tam>0){
					
					for(var i=0;i<tam;i++){
						$("#indicador_ret").append('<option value="'+data['datos'][i].indicador_retencion+'" >'+data['datos'][i].descripcion+'</option>');
					}
				}
			}
			else{
				alert("No se encontro ningún resultado");
			}
		}
	});
}

function mostrar_correo(id) {

	$.ajax({
		url: ruta_sistema+"/Rcontenido/buscarCorreo/",
		type: "POST",
		data: '' + id,
		contentType: "text/plain",
		dataType: 'json',
		beforeSend: function() {
			$("#fondo").css("display", "block");
		},
		error: function() {
			$("#fondo").css("display", "none");
			alert("Hubo un problema, Intentelo de nuevo");
		},
		success: function(data) {
			$("#fondo").css("display", "none");
			if (data['code'] == 1) {

				$("#asunto").html(data['datos'][0].asunto);
				$("#fecha_email").html(data['datos'][0].dia);
				$("#hora_email").html(data['datos'][0].hora);

				$("#sender").text(data['datos'][0].envia);
				$("#archivos_email").html("");

				for (var i = 0; i < data['archivos'].length; i++) {
					var xmls = "";

					if (data["archivos"][i].extension != ".pdf" && data["archivos"][i].extension != ".PDF") {
						xmls = data["archivos"][i].extension;
					}


					$("#archivos_email").append('<li class="estilo-archivo">' +
						'<a href="/Dazzle/Bandeja/descargar/' + data["archivos"][i].carpeta + '/' + data["archivos"][i].nombreArchivo + '" target="_blank" title="' + data['archivos'][i].nombre_original + '">' +
						'<img src="/images/otros.png" style="width: 70%;" />' +
						'<span class="file-name recortar">' + data['archivos'][i].nombre_original + '</span>' +
						'<div class="buttons">' +
						'<span class="file-size">' + data['archivos'][i].peso + 'Kb</span><br>' +
						'<span class="label label-info">' + (data['archivos'][i].etiqueta == null ? xmls : data['archivos'][i].etiqueta) + '</span>' +
						'</div>' +
						'</a>' +
						'</li>');
				}

				if (data['documentos_formales'].length > 0) {
					if (data['documentos_formales'][0].estadoValidacion == 1) {
						$("#detalle_verificacion_xml").css("display", "block");
						$("#es_com").text(data['datos_xml'][0].desEstado);
						$("#es_con").text(data['datos_xml'][0].nombreEstado);
						$("#cond_con").text(data['datos_xml'][0].estado_domicilio);
						$("#detalle_validaciones").css("display", "none");
					}
					else {
						$("#detalle_verificacion_xml").css("display", "none");
					}
				}
				else {
					$("#mensaje_estado_validacion").text("No se pudo procesar archivo XML");
				}
			}
		}
	});
}

function obtener_datos(datos) {
	var html = "";
	$.ajax({
		url: ruta_sistema+"/Contabilizar/obtenerDatosContabilizar/",
		type: "POST",
		data: ''+datos['idEmail'],
		contentType: "text/plain",
		dataType: 'json',
		beforeSend: function() {
			$("#fondo").css("display", "block");
			$("#tabla_detalle").html("");
		},
		error: function() {
			$("#fondo").css("display", "none");
			alert("Hubo un problema, Intentelo de nuevo");
		},
		success: function(data) {
			
			$("#fondo").css("display", "none");
			$(".no-padding").css("padding","0px");
			if (data['code'] == 1) {
				
				var color = "#ced4da";
				
				if(data['estatus'] == 1){
					alert(data['mensaje_sap']);
					$("#btn_contabilizar").css('display','none');
					color = "#AEEB95";
					$("#tabla_input1, #tabla_input2").css('background',color);
					$("#S"+datos['idEmail']).css('background',color);
				}
				else{
					$("#tabla_input1, #tabla_input2").css('background',color);
				}
									
				var tam = data['sap'].length;
				var total = 0;
				if(tam>0){
					for(var i=0;i<tam;i++){
						var ruta_hes = '/Dazzle/Contabilizar/descargar/'+data["sap"][i].numero_documento+'/'+data["sap"][i].idEmail;
						total += parseFloat(data['sap'][i].valor_neto);
						var tr = '<tr style="border: 0px; background: '+color+';">'+
							'<td style="width: 7.1%;border: 0px;"></td>'+
							'<td style="width: 4.5%;border: 0px;"></td>'+
							'<td style="width: 8.1%;border: 0px;"><img src="/Dazzle/images/right-arrow.png" style="height: 10px;"></td>'+
							'<td style="width: 8.8%;border: 0px;"><input type="text" style="width: 85%;" value="'+data['sap'][i].numero_documento+'" name="n_doc[]" readonly></td>'+
							'<td style="width: 8.5%;border: 0px;"><input type="text" style="width: 85%;" value="'+data['sap'][i].numero_documento_compra+'" name="n_ped[]" readonly></td>'+	
							'<td style="width: 9.5%;border: 0px;"><input type="hidden"  value="'+data['sap'][i].posicion_documento+'" name="pos_doc[]" ></td>'+
							'<td style="width: 8.4%;border: 0px;"></td>'+		
							'<td style="width: 7.4%;border: 0px;">'+data['sap'][i].valor_neto.toFixed(2)+'</td>'+	
							'<td style="width: 11.5%;border: 0px;"></td>'+
							'<td style="width: 4.2%;border: 0px;"></td>'+
							'<td style="width: 6%;border: 0px;"></td>'+
							'<td style="width: 9%;border: 0px;"><input type="text" style="width: 55%;" value="'+data['sap'][i].indicador_iva+'" name="indicador_iva_hes[]"></td>'+
							'<td style="text-align:center;border: 0px;"><a href="'+ruta_hes+'" target="_blank"><i class="fa fa-file"></i></a></td>'+
						'</tr>';
						$("#tabla_detalle").append(tr);
					}
					
						var tr = '<tr style="background: #AEEB95;">'+
							'<td style="width: 7.1%;border: 0px;"></td>'+
							'<td style="width: 4.5%;border: 0px;"></td>'+
							'<td style="width: 8.1%;border: 0px;"></td>'+
							'<td style="width: 8.8%;border: 0px;"></td>'+
							'<td style="width: 8.5%;border: 0px;"></td>'+	
							'<td style="width: 9.5%;border: 0px;"></td>'+
							'<td style="width: 8.4%;border: 0px;"></td>'+		
							'<td style="width: 7.4%;border: 0px;">'+total.toFixed(2)+'</td>'+	
							'<td style="width: 11.5%;border: 0px;">'+total.toFixed(2)+'</td>'+
							'<td style="width: 4.2%;border: 0px;"></td>'+
							'<td style="width: 6%;border: 0px;"></td>'+
							'<td style="width: 9%;border: 0px;"></td>'+
							'<td style="border: 0px;"></td>'+
						'</tr>';
						$("#tabla_detalle").append(tr);
				}
				
				var tam_ret = data['retenciones'].length;
				if(tam_ret>0){
					for(var i=0;i<tam_ret;i++){
						$("#indicador_tipo_ret").append('<option value="'+data['retenciones'][i]['indicador_tipo_retencion']+'">'+data['retenciones'][i]['descripcion']+'</option>')
					}	
				}
				
				var ruta_factura = '/Dazzle/Bandeja/descargar/'+data["rutaFactura"][0].carpeta.split("\\")[1]+'/'+data["rutaFactura"][0].nombreArchivo;
				$("#moneda_conta").val(data['datos'][0].WAERS);
				$("#proveedor_conta").val(data['datos'][0].nombre_empresa);
				$("#texto_cabecera").val(data['datos'][0].descripcion);
				
				$("#doc_sap_conta").val(data['num_doc_sap']);
				$("#id_correo").val(datos['idEmail']);
				
				
				$("#cond_pago_conta").val(data['datos'][0].ZTERM);
				
				$("#fecha_contabilizacion").val(data['datos'][0].BLDAT);			
				
				$("#ruta_factura").attr('href',ruta_factura);
								
				$("#tipDoc").val(data['datos'][0].TIPDO);
				$("#nro_doc_sunat").val(data['datos'][0].XBLNR);
				$("#tip_doc_sunat").val(data['datos'][0].BLART);				
				$("#nro_doc").val(data['datos'][0].BELNR);
				
				$("#pos_doc").val(data['datos'][0].BUZEI);
				$("#ejercicio").val(data['datos'][0].GJAHR);
				$("#num_pedido").val(data['datos'][0].EBELN);	
				$("#fecha_fact").val(data['datos'][0].BLDAT);
						
				$("#sociedad").val(data['datos'][0].BUKRS);				
				$("#clave_moneda").val(data['datos'][0].WAERS);	
				$("#importe_fact").val(data['datos'][0].RMWWR);				
				$("#importe_imp").val(data['datos'][0].WMWST1);
				
				$("#indicador_imp").val(data['datos'][0].MWSKZ);
				$("#cod_proveedor").val(data['datos'][0].LIFNR);
				$("#indicador_tipo_ret").val(data['datos'][0].WITHT);
				$("#indicador_ret").val(data['datos'][0].WT_WITHCD);
				
				$("#fecha_vencimiento").val(data['datos'][0].fecha_doc);
				
				var today = new Date();
				var mm = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0');
				
				if(mm == moment(data['datos'][0].fecha_doc).format('YYYYMM')){
					$("#fecha_conta").val(data['datos'][0].fecha_doc);	
				}
				else{					
					$("#fecha_conta").val(new Date().toISOString().slice(0, 10));
				}			
			}		
		}
	});
	
	return html;
}