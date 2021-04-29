data_empresa = [];

ruta_sistema = "/Dazzle";

$(document).ready(function(){
	
	tabla_empresas = $('#tabla_empresas').DataTable( {
	    dom: 'Bfrtip',
	    "language": {
	     "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
	    },
		columns: [	    					
			{data:"nombre"},
	        {data:"ruc"},
	        {data:"estado_mensaje"},			
			{
		  		data: null,
			  	render: function ( data, type, row, meta ) {
					return '<div class="dropdown">'+
  							'<a class="btn btn-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
							' Realizar Cambios '+
							'  </a>	'+						
							'  <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">'+
							'    <a class="dropdown-item" href="/Dazzle/Config/detalles/'+data['idEmpresa']+'" >Ver Detalles</a>'+
							'    <a class="dropdown-item" href="#" data-toggle="modal" data-target="#modal-actualizar" onClick="actualizar_datos('+(meta.row + meta.settings._iDisplayStart)+')">Actualizar</a>'+
							'    <a class="dropdown-item" href="#" onClick="activar_desactivar('+data['idEmpresa']+','+data['estado']+')">Habilitar/Desabilitar</a>'+
							'  </div>'+
							'</div>'
			 	}
			}
	    ],  		
	    buttons: [{text: "Descargar Excel",extend:'excel',className: 'btn-excel'},
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
	                customize: function (doc) {
	                    doc.content.splice(0,1);
	                    var now = new Date();
	                    var jsDate = now.getDate()+'-'+(now.getMonth()+1)+'-'+now.getFullYear();
	
	                    var logo = '';
	                    doc.pageMargins = [20,60,20,30];
	                    doc.defaultStyle.fontSize = 9;
	                    doc.styles.tableHeader.fontSize = 10;
	                    doc['header']=(function() {
	                        return {
	                            columns: [
	
	                                {
	                                    alignment: 'left',
	                                    italics: true,
	                                    text: 'EXPEDIENTES',
	                                    fontSize: 18,
	                                    margin: [10,0]
	                                },
	                                {
	                                    alignment: 'right',
	                                    fontSize: 14,
	                                    text: 'UGEL 06'
	                                }
	                            ],
	                            margin: 20
	                        }
	                    });
	                    doc['footer']=(function(page, pages) {
	                        return {
	                            columns: [
	                                {
	                                    alignment: 'left',
	                                    text: ['Descargado el: ', { text: jsDate.toString() }]
	                                },
	                                {
	                                    alignment: 'right',
	                                    text: ['pagina ', { text: page.toString() },  ' of ', { text: pages.toString() }]
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
	} );
	
	buscar_empresas();
	
	$("#form-actualizar-datos").submit(function(e){
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Config/actualizarDatos",
			method: "post",
			data: new FormData(this),
			cache: false,
	 		processData: false,
	 		beforeSend: function(){
	 			$("#fondo").css("display","block");
	 		},
	 		error: function(){
	 			$("#fondo").css("display","none");
	 		},
	 		success: function(data){	
				$("#fondo").css("display","none"); 			
				alert(data['mensaje']);
				if(data['code']==1){
					tabla_empresas.clear().draw();
					buscar_empresas();
					$("#form-actualizar-datos")[0].reset();
					$("#modal-actualizar").modal('hide');
					$("#idEmp").val("");
				}				 
	 		}
		});
	});	
	
	$("#form-actualizar-parametros").submit(function(e){
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Config/actualizarParametros",
			method: "post",
			data: new FormData(this),
			cache: false,
	 		processData: false,
	 		beforeSend: function(){
	 			$("#fondo").css("display","block");
	 		},
	 		error: function(){
	 			$("#fondo").css("display","none");
	 		},
	 		success: function(data){	
				$("#fondo").css("display","none"); 			
				alert(data['mensaje']);
								 
	 		}
		});
	});

	$("#form-actualizar-sunat").submit(function(e){
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Config/actualizarSunat",
			method: "post",
			data: new FormData(this),
			cache: false,
	 		processData: false,
	 		beforeSend: function(){
	 			$("#fondo").css("display","block");
	 		},
	 		error: function(){
	 			$("#fondo").css("display","none");
	 		},
	 		success: function(data){	
				$("#fondo").css("display","none"); 			
				alert(data['mensaje']);
				if(data['code']==1){
					$(this)[0].reset();
				}				 
	 		}
		});
	});

	$("#form-actualizar-correo").submit(function(e){
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: ruta_sistema+"/Config/actualizarCorreos",
			method: "post",
			data: new FormData(this),
			cache: false,
	 		processData: false,
	 		beforeSend: function(){
	 			$("#fondo").css("display","block");
	 		},
	 		error: function(){
	 			$("#fondo").css("display","none");
	 		},
	 		success: function(data){	
				$("#fondo").css("display","none"); 			
				alert(data['mensaje']);
				if(data['code']==1){
					$(this)[0].reset();
				}				 
	 		}
		});
	});

	$("#check_sunat").change(function() {
	    if(this.checked) {
	        $("#clave_sunat").attr("type","text");
	    }
		else
		{
			$("#clave_sunat").attr("type","password");
		}
	});
	
	$("#check_correo").change(function() {
	    if(this.checked) {
	        $("#clave_correo").attr("type","text");
	    }
		else
		{
			$("#clave_correo").attr("type","password");
		}
	});
	
	$("#check_parametro").change(function() {
	    if(this.checked) {
	        $("#clave_parametro").attr("type","text");
	    }
		else
		{
			$("#clave_parametro").attr("type","password");
		}
	});
						
});

function buscar_empresas(){
	$.ajax({
      	url: ruta_sistema+"/Config/listaEmpresas/",
	  	type: "POST",
		contentType: "text/plain",
		dataType : 'json',
      beforeSend: function(){
        $("#fondo").css("display","block");
      },
      error: function(){
        $("#fondo").css("display","none");
        alert("Hubo un problema, Intentelo de nuevo");
      },
      success: function(data){
        $("#fondo").css("display","none"); 
		if(data['code'] == 1){
			data_empresa = data['datos'];
			tabla_empresas.rows.add(data['datos']).draw();
		}else{
			alert(data['mensaje']);
		}      		            
      }
    });	
}

function activar_desactivar(idUsuario, estado){
	estado_activar = 0;
	var resp = false;
	if(estado == 1){
		estado_activar = 0;
		resp = confirm("¿Estas seguro que quieres desabilitar esta Empresa?");
	}
	else{
		estado_activar = 1;
		resp = confirm("¿Estas seguro que quieres habilitar este Empresa?");
	}
	
	if(resp){
		$.ajax({
	      	url: ruta_sistema+"/Config/habilitarDesabilitar/",
		  	type: "POST",
			data: ''+idUsuario+','+estado_activar,
			contentType: "text/plain",
			dataType : 'json',
	        beforeSend: function(){
	        	$("#fondo").css("display","block");
	        },
	        error: function(){
	        	$("#fondo").css("display","none");
	        	alert("Hubo un problema, Intentelo de nuevo");
	        },
	        success: function(data){
	        	$("#fondo").css("display","none"); 
				tabla_empresas.clear().draw();
				buscar_empresas();
				alert(data['mensaje']);
	        }
	    });
	}
}

function actualizar_datos(id){
	
	$("#idEmp").val(data_empresa[id].idEmpresa);
	$("#nombre").val(data_empresa[id].nombre);
	$("#ruc").val(data_empresa[id].ruc);
	
}

function limpiar_form(){
	$("#form-actualizar-datos")[0].reset();
}
		