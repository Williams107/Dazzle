$(document).ready(function(){
	
	tabla_correos = $('#tabla_correo_rechazados').DataTable( {
	    dom: 'Bfrtip',
	    "language": {
	     "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
	    },
		columns: [	    					
			{data:"correo"},
	        {data:"asunto"},
	        {data:"mensaje"},
	        {data:"fecha"}
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
	                                    text: 'DAZZLE'
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
	
	tabla_log_validacion = $('#tabla_log_validacion').DataTable( {
	    dom: 'Bfrtip',
	    "language": {
	     "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
	    },
		columns: [	    					
			{data:"correo"},
	        {data:"asunto"},
	        {data:"mensaje"},
			{data:"archivo"},
	        {data:"fecha"}
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
	
	tabla_log_descarga = $('#tabla_log_descarga').DataTable( {
	    dom: 'Bfrtip',
	    "language": {
	     "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
	    },
		columns: [	    					
			{data:"correo"},
	        {data:"asunto"},
	        {data:"mensaje"},
			{data:"funcion"},
	        {data:"fecha"}
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
	
})


function buscar_correos(){
	$.ajax({
      	url: "/Dazzle/Errorcorreo/correosRechazados/",
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
			tabla_correos.rows.add(data['datos']).draw();
		}else{
			alert(data['mensaje']);
		}      		            
      }
    });	
}

function buscar_log_descarga(){
	$.ajax({
      	url: "/Dazzle/Errorcorreo/logDescarga/",
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
			tabla_log_descarga.rows.add(data['datos']).draw();
		}else{
			alert(data['mensaje']);
		}      		            
      }
    });	
}

function buscar_log_validacion(){
	$.ajax({
      	url: "/Dazzle/Errorcorreo/logValidacion/",
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
			tabla_log_validacion.rows.add(data['datos']).draw();
		}else{
			alert(data['mensaje']);
		}      		            
      }
    });	
}