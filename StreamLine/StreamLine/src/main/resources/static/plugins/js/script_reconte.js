/* Formatting function for row details - modify as you need */
function format(d) {
	var html = "";
	$.ajax({
		url: "/Dazzle/Rcontenido/obtenerDetalles/",
		type: "POST",
		data: '' + d['idEmail']+';'+d['ruc']+';'+d['NumFact'],
		contentType: "text/plain",
		dataType: 'json',
		async: false,
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

				var tam_correo = data['datosSunat'].length;
				if(tam_correo > 0){
					for(var a=0;a<tam_correo;a++){
						var idCorreo = data['datosSunat'][a].idEmail;
						html += '<table class="table table-bordered table-hover table-striped" cellpadding="5" style="padding-left:50px;text-align: left;width:100%">'+
						'<tr style="background: white;">' +
							'<td style="width: 20%;" >' +
							'<strong Style="font-size: 15px;color: #5288c1;">' + 'Datos:' + '</strong>' + '<br>' +
							'Id Correo: ' + data['datosSunat'][a].numeroEmail + '<br>' +
							'Fecha / Hora:' + data['datosSunat'][a].fecha + '<br>' +
							'Correo: ' + data['datosSunat'][a].correo + '<br>' +
							'</td>';
							
							html += '<td style="width: 22%;" >' +
							'<strong Style="font-size: 15px;color: #5288c1;">' + 'Recepción de Documentos:' + '</strong>' + '<br>' +
							'RUC:  ' + d['ruc']  +' / ' + d['NumFact'] + '<br>' +
							' XML: <span class="badge badge-success">OK</span> <br>' +
							'  PDF Factura: ' + (data['datosSunat'][a].FACT != null ? '<span class="badge badge-success">OK</span>' : 'NO') + '<br>' 
							
							if(data['datosSunat'][a].HES != null && data['datosSunat'][a].MIGO != null){
									html += '  PDF HES: ' + '<span class="badge badge-success">OK</span>'+ '  Inconsistencia'+'<br>'+	
									        '  PDF MIGO:  ' + '<span class="badge badge-success">OK</span>' +'  Inconsistencia'+ '<br>' 
							}else{
									html +='  PDF HES: ' + (data['datosSunat'][a].HES != null ? '<span class="badge badge-success">OK</span>' : 'NO') + '<br>' +
										   '  PDF MIGO:  ' + (data['datosSunat'][a].MIGO != null ? '<span class="badge badge-success">OK</span>' : 'NO') + '<br>'
							}
		
							html+= '<I> Documentos: </I> <br>';
							
							var tam_eti = data['datosEtiqueta']['E'+idCorreo].length;
					
							if(tam_eti > 0){
								for(var i=0;i<tam_eti;i++){
									ruta_eti = '/Dazzle/Bandeja/descargar/' + data["datosEtiqueta"]['E'+idCorreo][i].carpeta.split("\\")[1] + '/' + data["datosEtiqueta"]['E'+idCorreo][i].nombreArchivo;
									html += '<a href="'+ruta_eti+'" target="_blank"><i class="far fa-file-pdf fa-lg"> </i></a> '+ data['datosEtiqueta']['E'+idCorreo][i].etiqueta+'<br>';
								}	
							}					
					
					
						    html += '</td>';
							
							if(data['datosSunat'][a].est_comp == 'No existe' || data['datosSunat'][a].est_comp == 'Anulado'){
								html += '<td style="width: 18%;">' +
								'<strong Style="font-size: 15px;color: #5288c1;">' + 'Validación SUNAT:' + '</strong>' + '<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">' + 'Estado del contribuyente:' + '</strong>' + '<br>'+ 
								'<span class="badge badge-danger">ERROR</span>'+' '+data['datosSunat'][a].est_cont+'<br>' +'<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">'+' ' + 'Condición de Domicilio del Contribuyente:' + '</strong>' + '<br>'+ 
								'<span class="badge badge-danger">ERROR</span>'+' '+data['datosSunat'][a].est_dom+ '<br>' +'<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">'+' ' + 'Estado del comprobante:' + '</strong>' + '<br>'+  
								'<span class="badge badge-danger" >ERROR</span>'+' '+'<span style="text-transform: uppercase;">'+data['datosSunat'][a].est_comp+'</span>' + '<br>' +'<br>' +
								'</td>' 
								
							}else
							{
								html += '<td style="width: 18%;">' +
								'<strong Style="font-size: 15px;color: #5288c1;">' + 'Validación SUNAT:' + '</strong>' + '<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">' + 'Estado del contribuyente:' + '</strong>' + '<br>'+ 
								'<span class="badge badge-success">OK</span>'+' '+data['datosSunat'][a].est_cont+'<br>' +'<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">'+' ' + 'Condición de Domicilio del Contribuyente:' + '</strong>' + '<br>'+ 
								'<span class="badge badge-success">OK</span>'+' '+data['datosSunat'][a].est_dom+ '<br>' +'<br>' +
								'<strong Style="font-size: 13px;font-weight: bold;">'+' ' + 'Estado del comprobante:' + '</strong>' + '<br>'+  
								'<span class="badge badge-success" >OK</span>'+' '+'<span style="text-transform: uppercase;">'+data['datosSunat'][a].est_comp+'</span>' + '<br>' +'<br>' +
								'</td>' 
							}
									
							
							html +='<td style="width: 45%;">' +
							'<strong Style="font-size: 15px;color: #5288c1;">' + 'Validación SAP:' + '</strong>' + '<br>';
							
							if(data['datosCorreo']['C'+idCorreo].length > 0){
								html += '<span class="badge badge-pill badge-info">CORREO ENVIADO EL '+data['datosCorreo']['C'+idCorreo][0].fecha+' </span><br>';	
							}					
							
							
							html += 'Archivos:' + '<br>'+						
							'<table>';
								
								var importe_factura = 0;
								var tam = data['datosFactura']['F'+idCorreo].length;
								if (tam > 0) {
									for (var i = 0; i < tam; i++) {
										
										var ruta_archivo = "";
										if(data['rutaFactura']['R'+idCorreo].length > 0){
											ruta_archivo = '/Dazzle/Bandeja/descargar/' + data["rutaFactura"]['R'+idCorreo][0].carpeta.split("\\")[1] + '/' + data["rutaFactura"]['R'+idCorreo][0].nombreArchivo;
										}
										
										var mensaje="";
											
											if (d['contabilizado_sap'] == 0 || d['contabilizado_sap'] == null) {
												if (data['datosFactura']['F'+idCorreo][i]['mensaje'] != null) {
													arreglo_mensaje = data['datosFactura']['F'+idCorreo][i]['mensaje'].split('-');
				
													if (arreglo_mensaje[0] == '001') {
														mensaje = '<span class="badge badge-success">OK</span> - APTO PARA CONTABILIZAR</span>';
													}
													else if (arreglo_mensaje[0] == '017') {
														mensaje = '<span class="badge badge-success">FACTURA CONTABILIZADA</span>';
													}
													else {
														mensaje = '<span class="badge badge-danger">ERROR: </span>' + arreglo_mensaje[1];
													}
												}
												else {
													mensaje = '<span class="badge badge-orange">PENDIENTE</span>';
												}
												
											}
											else{
												mensaje = '<span class="badge badge-success">FACTURA CONTABILIZADA</span>';
											}			
															
										importe_factura += data['datosFactura']['F'+idCorreo][i]['monto'];
										if(ruta_archivo == ""){
											html += '<tr style="background: white;"><td></td><td> FACTURA: ' + data['datosFactura']['F'+idCorreo][i]['NumFact'] + '<br></td><td>'+data['datosFactura']['F'+idCorreo][i]['monto']+'</td><td>'+mensaje+'</td></tr>';
										}
										else{
										    html += '<tr style="background: white;"><td><a class="btn " href="' + ruta_archivo + '" target="_blank"><i class="far fa-file-archive fa-lg"> </i></a></td><td> FACTURA: ' + data['datosFactura']['F'+idCorreo][i]['NumFact'] + '<br></td><td>'+data['datosFactura']['F'+idCorreo][i]['monto']+'</td><td>'+mensaje+'</td></tr>';
										}
									}
								}	
								
							var tam = data['datosHesMigo']['H'+idCorreo].length;
							var suma_importe = 0;
							if (tam > 0) {
								for (var i = 0; i < tam; i++) {
									var ruta_archivo = '/Dazzle/Contabilizar/descargar/' + data['datosHesMigo']['H'+idCorreo][i]['numero_documento'] + '/' + data['datosHesMigo']['H'+idCorreo][i]['idEmail'];
									var mensaje="";
									if (d['contabilizado_sap'] == 0 || d['contabilizado_sap'] == null) {
										if (data['datosHesMigo']['H'+idCorreo].length > 0) {
			
										if (data['datosHesMigo']['H'+idCorreo][i]['numero_mensaje'] == '001') {
											mensaje = '<span class="badge badge-success">OK</span> - APTO PARA CONTABILIZAR<span>';
										}
										else if (data['datosHesMigo']['H'+idCorreo][i]['numero_mensaje'] == '002') {
											mensaje = '<span class="badge badge-success"> CONTABILIZADO</span>'  + data['datosHesMigo']['H'+idCorreo][i]['texto_mensaje'];
										}
										else {
											mensaje = '<span class="badge badge-danger">ERROR</span>:' + data['datosHesMigo']['H'+idCorreo][i]['texto_mensaje'];
										}
									}	
										
									}else{
										mensaje = '<span class="badge badge-success"> CONTABILIZADO</span>';
									}					
									suma_importe += data['datosHesMigo']['H'+idCorreo][i]['neto'];
									html += '<tr style="background: white;"><td><a class="btn o" href="' + ruta_archivo + '" target="_blank"><i class="far fa-file-archive fa-lg"> </i></a></td><td> '+data['datosHesMigo']['H'+idCorreo][i]['etiqueta'] +': ' + data['datosHesMigo']['H'+idCorreo][i]['numero_documento'] + '<br></td><td>'+data['datosHesMigo']['H'+idCorreo][i]['neto'].toFixed(2)+'</td><td>'+mensaje+'</td></tr>';
								}
								sin_igv = importe_factura.toFixed(2) - suma_importe;
								html += '<tr><td colspan="2">TOLERANCIA: </td><td>'+(  sin_igv.toFixed(2) )+'</td></tr>';
								
						
							}										
							
							html += '</table>';
							if(data['numSap'][0].num_sap != null){
								html += '<span class="badge badge-info pull-right"> N° DOC. SAP: '+data['numSap'][0].num_sap+'</span>';
							}
							html += '</td></tr>' +
							'</table>';
								
					}	
				}
				

			}
		}
	});


	console.log(html);
	return html;

}

$(document).ready(function() {

	//busqueda();


	table = $("#tabla_contenido").DataTable({
		dom: 'Bfirti',
		
		paging:false,
		"order": [[1, "ASC"]],
		buttons: [
			{
				extend: 'excelHtml5',
				text: '<i class="far fa-cloud-download-alt" title="Exportar .xlsx"> </i>', /* Poner icono */
				filename: 'Reporte de Contenido de correo', /* Nombre del archivo */
				exportOptions: {

					/*columns: [1,2,3,4,5,6,7,8,9,10]/* solo exporta las opciones seleccionadas por colvis */
					columns: ':visible'
				}
			},
			{
				extend: 'collection',
				text: '<i class="fad fa-layer-plus title="Agregar/Quitar Columnas""></i>',
				autoClose: true,
				buttons: [
					{
						text: 'Sociedad',
						action: function(e, dt, node, config) {
							dt.column(-10).visible(!dt.column(-10).visible());
						}
					},					
					{
						text: 'Razón Social',
						action: function(e, dt, node, config) {
							dt.column(-8).visible(!dt.column(-8).visible());
						}
					},
					{
						text: 'Ruc',
						action: function(e, dt, node, config) {
							dt.column(-7).visible(!dt.column(-7).visible());
						}
					},
					{
						text: 'Tipo',
						action: function(e, dt, node, config) {
							dt.column(-6).visible(!dt.column(-6).visible());
						}
					},
					{
						text: 'N° Comprob.',
						action: function(e, dt, node, config) {
							dt.column(-5).visible(!dt.column(-5).visible());
						}
					},
					{
						text: 'Recep. Doc.',
						action: function(e, dt, node, config) {
							dt.column(-4).visible(!dt.column(-4).visible());
						}
					},
					{
						text: 'Validación SAP',
						action: function(e, dt, node, config) {
							dt.column(-3).visible(!dt.column(-3).visible());
						}
					},
					{
						text: 'Validación SUNAT',
						action: function(e, dt, node, config) {
							dt.column(-2).visible(!dt.column(-2).visible());
						}
					},
					{
						text: 'Estado',
						action: function(e, dt, node, config) {
							dt.column(-1).visible(!dt.column(-1).visible());
						}
					},
				]
			}


		],
		"lengthMenu": [10, 25, 50, 100, 200, 400, 800, 1600],

		"iDisplayLength": 10,
		"language": {
			"url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json",
			buttons: {
				colvis: 'Selecionar Columnas',
				excel: 'Exportar  Excel',
				pdf: 'Exportar Pdf',

			}
		},
		"columns": [
			{
				"className": 'details-control',
				"orderable": false,
				"data": null,
				"defaultContent": ''
			},
			{ "data": "nombre" },
			{ "mData": "nombre_empresa" },
			{ "mData": "ruc" },
			{ "mData": "tipo_xml" },
			{ "mData": "NumFact" },
			{ "mData": "fecha" },
			{
				data: null,
				render: function(data, type, row, meta) {
					var word = ""
					if (data['documentos'] == 1) {
						word = '<span class="badge badge-success">OK</span>';
					}
					else {
						
						word = '<span class="badge badge-danger">ERROR</span>';
						
					}
					return word;
				}
			},
			
			{
				"mData": null,
				render: function(data, type, row, meta) {
					if (data['validacion_sunat'] == 1) {
						word = '<span class="badge badge-success">0K</span>';
					}
					else {
						word = '<span class="badge badge-danger">ERROR</span>';
					}
					return word;
				}
			},
			{
				"mData": null,
				render: function(data, type, row, meta) {
					if (data['SAP'] == 1) {
						word = '<span class="badge badge-success">0K</span>';
					}
					else {
						word = '<span class="badge badge-danger">PENDIENTE DE VALIDACION</span>';
					}

					return word;
				}
			},
			{
				data: null,
				render: function(data, type, row, meta) {
					
					var word = ""
					
					if(data["contabilizado_sap"] == 0 || data["contabilizado_sap"] == null){
						if (data['num_mensaje'] == '001') {
								word = '<span class="badge badge-warning">POR CONTABILIZAR</span>';							
						}
						else if(data['num_mensaje'] == '017') {
								word = '<span class="badge badge-success">CONTABILIZADO</span>';
						}
						else if(data['num_mensaje'] != '001' && data['num_mensaje'] != '017'){
							word = '<span class="badge badge-danger">ERROR</span>';
						}
						else{
							word = '<span class="badge badge-danger">ERROR</span>';
						}
					}
					else{
						word = '<span class="badge badge-success">CONTABILIZADO</span>';	
					}
					
					return word;
					
				}
			}
		],
	});

	$("#form-conte").submit(function(e) {
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: "/Dazzle/Rcontenido/obtenerReporte/",
			method: "post",
			data: new FormData(this),
			cache: false,
			processData: false,
			beforeSend: function() {
				$("#fondo").css("display", "block");
				$('#tabla_contenido').DataTable().clear().draw();
			},
			error: function() {
				$("#fondo").css("display", "none");
				alert("Hubo un problema, Intentelo de nuevo");
			},
			success: function(data) {
				$("#fondo").css("display", "none");
				if (data['code'] == 1) {
					table.rows.add(data['datos']).draw();
				}
			}
		});
	});

	$('#tabla_contenido tbody').on('click', 'td.details-control', function() {
		var tr = $(this).closest('tr');
		var row = table.row(tr);

		if (row.child.isShown()) {
			// This row is already open - close it
			row.child.hide();
			tr.removeClass('shown');
		}
		else {
			// Open this row
			row.child(format(row.data())).show();
			tr.addClass('shown');
		}
	});


});

//----rellenar--///

function reporte() {
	$.ajax({
		url: "/Dazzle/Rcontenido/obtenerDetalles/",
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
				table.rows.add(data['datos']).draw();
			}
		}
	});

/*	window.onload = function() {
		var fecha = new Date(); //Fecha actual
		var mes = fecha.getMonth() + 1; //obteniendo mes
		var dia = fecha.getDate(); //obteniendo dia
		var ano = fecha.getFullYear(); //obteniendo año
		if (dia < 10)
			dia   '0'   dia; //agrega cero si el menor de 10
		if (mes < 10)
			mes = '0' + mes //agrega cero si el menor de 10
		document.getElementById('fecha_ini').value = ano + "-" + mes + "-" + dia;
	}*/
}


