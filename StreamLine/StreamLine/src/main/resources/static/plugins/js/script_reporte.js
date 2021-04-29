		
$(document).ready(function(){
	
	tabla_correos = $('#tabla_correo').DataTable( {
	    dom: 'Bfrtip',
	    "language": {
	     "url": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json"
	    },
		columns: [	    					
			{data:"envia"},
	        {data:"asunto"},
	        {data:"archivos"},
			{data:"archivos_descargados"},
			{data:"estado_validacion"},
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
	
	$("#form-correo").submit(function(e){
		e.preventDefault();
		$.ajax({
			contentType: false,
			dataType: "json",
			url: "/Dazzle/reporcorreo/reporteCorreos",
			method: "post",
			data: new FormData(this),
			cache: false,
	 		processData: false,
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
	});						
});
		