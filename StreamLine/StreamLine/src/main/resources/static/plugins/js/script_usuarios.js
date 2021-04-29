
data_user = []
ruta_sistema = "/Dazzle";

$(document).ready(function() {

	$('#accesos').selectize({
		plugins: ['remove_button'],
		delimiter: ',',
		persist: false,
		create: function(input) {
			return {
				value: input,
				text: input
			}
		}
	});

	tabla_usuarios = $('#tabla_usuarios').DataTable({
		dom: 'Bfrtip',
		"language": {
			"url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
		},
		columns: [
			{ data: "nombres" },
			{ data: "nro_documento" },
			{ data: "usuario" },
			{ data: "correo" },
			{ data: "empresa" },
			{ data: "tipo_usuario" },
			{ data: "accesos" },
			{ data: "estado_usuario" },
			{
				data: null,
				render: function(data, type, row, meta) {
					return '<div class="dropdown">' +
						'<a class="btn btn-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
						' Realizar Cambios ' +
						'  </a>	' +
						'  <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">' +
						'    <a class="dropdown-item" href="#" data-toggle="modal" data-target="#modal-cambiar-clave" onClick="set_id(' + data['idUsuario'] + ')">Cambiar Clave</a>' +
						'    <a class="dropdown-item" href="#" onClick="activar_desactivar(' + data['idUsuario'] + ',' + data['estado'] + ')">Habilitar/Desabilitar</a>' +
						'    <a class="dropdown-item" href="#" data-toggle="modal" data-target="#modal-actualizar-datos" onClick="actualizar_usuarios(' + (meta.row + meta.settings._iDisplayStart) + ')">Actualizar Datos</a>' +
						'  </div>' +
						'</div>'
				}
			}
		],
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

	buscar_usuarios();

	$("#form-cambiar-clave").submit(function(e) {
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Usuario/cambiarClave",
			method: "post",
			data: new FormData(this),
			cache: false,
			processData: false,
			beforeSend: function() {
				$("#fondo").css("display", "block");
			},
			error: function() {
				$("#fondo").css("display", "none");
			},
			success: function(data) {
				$("#fondo").css("display", "none");
				alert(data['mensaje']);
				if (data['code'] == 1) {
					tabla_usuarios.clear().draw();
					buscar_usuarios();
					$("#form-cambiar-clave")[0].reset();
					$("#modal-cambiar-clave").modal('hide');
				}
			}
		});
	});

	$("#form-actualizar-usuarios").submit(function(e) {
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Usuario/actualizarDatos",
			method: "post",
			data: new FormData(this),
			cache: false,
			processData: false,
			beforeSend: function() {
				$("#fondo").css("display", "block");
			},
			error: function() {
				$("#fondo").css("display", "none");
			},
			success: function(data) {
				$("#fondo").css("display", "none");
				alert(data['mensaje']);
				if (data['code'] == 1) {
					tabla_usuarios.clear().draw();
					buscar_usuarios();
					$("#form-actualizar-usuarios")[0].reset();
					$("#modal-actualizar-datos").modal('hide');
					$("#idusuario_actualizar").val("");
					$("#div-clave").css("display", "block");
				}
			}
		});
	});

});

function buscar_usuarios() {
	$.ajax({
		url: ruta_sistema+"/Usuario/listaUsuarios/",
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
				data_user = data['datos'];
				tabla_usuarios.rows.add(data['datos']).draw();
			} else {
				alert(data['mensaje']);
			}
		}
	});
}

function activar_desactivar(idUsuario, estado) {
	estado_activar = 0;
	var resp = false;
	if (estado == 1) {
		estado_activar = 0;
		resp = confirm("¿Estas seguro que quieres desabilitar este usuario?");
	}
	else {
		estado_activar = 1;
		resp = confirm("¿Estas seguro que quieres habilitar este usuario?");
	}

	if (resp) {
		$.ajax({
			url: ruta_sistema+"/Usuario/habilitarDesabilitar/",
			type: "POST",
			data: '' + idUsuario + ',' + estado_activar,
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
				tabla_usuarios.clear().draw();
				buscar_usuarios();
				alert(data['mensaje']);
			}
		});
	}
}

function set_id(id) {
	$("#idUsuario_clave").val(id);
}


function validar_campo(){
	if($("#tipo_usuario option:selected").text() == 'SAP'){
		$("#clave_actualizar").prop('readonly',true);
		$("#clave_actualizar").val('SAP');
	}
	else{
		$("#clave_actualizar").prop('readonly',false);
		$("#clave_actualizar").val('');
	}
}

function limpiar_formulario(){
	$("#form-actualizar-usuarios")[0].reset();
	 var $select = $('#accesos').selectize();
	 var control = $select[0].selectize;
	 control.clear();
}

function actualizar_usuarios(id) {
	$("#div-clave").css("display", "none");

	$("#nombres_actualizar").val(data_user[id].nombres);
	$("#apellido_actualizar").val(data_user[id].apellidos);
	$("#idusuario_actualizar").val(data_user[id].idUsuario);
	$("#doc_actualizar").val(data_user[id].nro_documento);
	$("#usuario_actualizar").val(data_user[id].usuario);
	$("#correo_actualizar").val(data_user[id].correo);
	
	var str_array_skills = data_user[id].accesos.split(',');
	var $select =   $("#accesos").selectize();
	var selectize = $select[0].selectize;
	selectize.setValue(str_array_skills);
	//selectize.refreshOptions();

	$("#empresa_actualizar").val(data_user[id].idEmpresa);
	$("#tipo_usuario").val(data_user[id].idTipoUsuario);
}
