ruta_sistema = "/Dazzle";

nume_correo = -1;
cortar_busqueda = 0;
filtro = "0";
		
		$(document).ready(function(){
			buscar_total_correos();
			buscar_correos();
			$('#contenedor-correo').on('scroll', function() {
			    let div = $(this).get(0);
			    if(div.scrollTop + div.clientHeight >= div.scrollHeight) {
			        buscar_correos();
			    }
			});
			
			$("#formulario-busqueda").submit(function(e){
				e.preventDefault();
				var asunto = $("#asunto-busqueda").val();
				buscar_correo_asunto(asunto);
			});
			
			$("#formulario-rechazo").submit(function(e){
				e.preventDefault();
				
				$.ajax({
				contentType: false,
				dataType: "json",
				url: ruta_sistema+"/Bandeja/enviarObservacion",
				method: "post",
				data: new FormData(this),
				cache: false,
		 		processData: false,
		 		beforeSend: function(){
		 			$("#gif_carga").css("display","block");
		 		},
		 		error: function(){
		 			$("#gif_carga").css("display","none");
		 		},
		 		success: function(data){
		 			$("#gif_carga").css("display","none");
		 			alert(data['mensaje']);
		 		}
			});
				
			});						
		});
		
		function mostrar_popover(){
		
			$(this).popover({
				  html: true,
				  content: function() {
				    var id = $(this).attr('idEmail');
				    var etiquetas = buscar_etiquetas(id);
				    //return $('#popover-content').html();
				    return '<h5>Documentos Detectados:</h5>'+etiquetas;
				  }
			});
		}
				
		function buscar_etiquetas(id){
			var etiqueta = "";
			if(id != "0" && id != ""){
				
				$.ajax({
		          	url: ruta_sistema+"/Bandeja/obtenerEtiquetas/",
				  	type: "POST",
					data:''+id,
					contentType: "text/plain",
					dataType : 'json',
					async:false,
		          beforeSend: function(){
		            $("#fondo").css("display","block");
		          },
		          error: function(){
		            $("#fondo").css("display","none");
		            alert("Hubo un problema, Intentelo de nuevo");
		          },
		          success: function(data){
		            $("#fondo").css("display","none");
		            
		            if(data['code'] == 1)
		            {
		              for(var i=0;i<data['datos'].length;i++){
		              	etiqueta += '<span class="label label-info">'+data['datos'][i].etiqueta+'</span>';
		              }             
		            }
		            else{
		            	etiqueta = '<span class="label label-info">PENDIENTE DE VALIDACIÓN</span>';
		            }		            
		          }
		        });			        
		        			
			}		
			
			console.log(etiqueta);
			return etiqueta;	
		}		
				
		function buscar_correos(){
			if(nume_correo != 0 && cortar_busqueda == 0){
				
				$.ajax({
		          	url: ruta_sistema+"/Bandeja/obtenerCorreos/",
				  	type: "POST",
					data:''+nume_correo+','+filtro,
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
		            if(data['code'] == 0)
		            {
		              alert("No se encontro ningún resultado");              
		            }
		            else{
		            	ultima_fila = data['datos'].length - 1;  
		            	nume_correo = data['datos'][ultima_fila].numeroEmail - 1;
		            	for(var i=0;i<data['datos'].length;i++){
		            	
		            		$("#cuerpo").append('<tr class="'+data['datos'][i].estado_leido+'" id="'+data['datos'][i].idEmail+'" onClick="mostrar_correo('+data['datos'][i].idEmail+','+data['datos'][i].leido+','+data['datos'][i].validado+')">'+	
		            			'<td class="view-message">'+data['datos'][i].numeroEmail+'</td>'+                       
		                       '<td class="view-message">'+escapeHtml(data['datos'][i].envia)+'<br><span class="label label-'+data['datos'][i].label+'">'+data['datos'][i].estado_validacion+'</span> </td>'+
		                       '<td class="view-message">'+data['datos'][i].asunto+'</td>'+
		                       '<td class="view-message inbox-small-cells"></td>'+
		                       '<td class="view-message inbox-small-cells">'+data['datos'][i].cantidad_archivos+'<i class="fa fa-paperclip" idEmail="'+data['datos'][i].idEmail+'" onClick="mostrar_popover.call(this)" data-toggle="popover" data-container="body" data-placement="right" data-html="true" ></i></td>'+
		                       '<td class="view-message text-right">'+data['datos'][i].hora+' <br> '+data['datos'][i].dia+' </td>'+
		                     '</tr>');	
		                     
		            	}          	              
		              
		            }
		          }
		        });				
			}			
	}
	
	function escapeHtml(str)
	{
	    var map =
	    {
	        '&': '&amp;',
	        '<': '&lt;',
	        '>': '&gt;',
	        '"': '&quot;',
	        "'": '&#039;'
	    };
	    return str.replace(/[&<>"']/g, function(m) {return map[m];});
	}
	
	function mostrar_correo(id, estado, validado){
		
		$("#sin-seleccion").css("display","none");
		$("#correo-seleccionado").addClass("d-md-block");
		
		if(estado==0){
			$("#"+id).removeClass("unread");
			$("#"+id).addClass("read");
		}		
		
			$.ajax({
		          	url: ruta_sistema+"/Bandeja/buscarCorreo/",
				  	type: "POST",
					data:''+id+','+estado,
					contentType: "text/plain",
					dataType : 'json',
		          beforeSend: function(){
		            $("#fondo").css("display","block");

					$("#detalle_validaciones").css("display","none");
					$("#detalle_verificacion_xml").css("display","none");
					$("#detalle_validaciones").css("display","none");
					
					
		          },
		          error: function(){
		            $("#fondo").css("display","none");
		            alert("Hubo un problema, Intentelo de nuevo");
		          },
		          success: function(data){
		            $("#fondo").css("display","none");
					
							
		            if(data['code'] == 0)
		            {
		              alert("No se encontro ningún resultado");              
		            }
		            else{
		            	
		            	$("#idEmailRechazado").val(data['datos'][0].idEmail);
		            	$("#asuntoCorreoRechazo").val(data['datos'][0].asunto);
		            	
		            	$("#asunto").html(data['datos'][0].asunto);
		            	$("#fecha_email").html(data['datos'][0].dia);
		            	$("#hora_email").html(data['datos'][0].hora); 
		            	
		            	$("#sender").text(data['datos'][0].envia);
		            	$("#cuerpo_email").html(data['datos'][0].cuerpo); 
		            	$("#archivos_email").html("");
		            	
		            	if(data['ruc'].length>0){
							$("#nom_prov").html('RAZÓN SOCIAL: '+data['ruc'][0].nombre_empresa + ' ');
							$("#ruc_sender").html('RUC: '+data['ruc'][0].ruc + ' ');
							$("#factura_sender").html('- FACTURA: '+data['ruc'][0].NumFact);
						}
						
						

		            	for(var i=0;i<data['archivos'].length;i++){
							var xmls = "";
							
							if(data["archivos"][i].extension != ".pdf" && data["archivos"][i].extension != ".PDF"){
								xmls = data["archivos"][i].extension;
							}		            		
							
							ruta_archivo = ruta_sistema+"/Bandeja/descargar/"+data["archivos"][i].carpeta.split('\\')[1]+"/"+data["archivos"][i].nombreArchivo;

		            		if(validado==1){
		            			$("#archivos_email").append('<li class="estilo-archivo">'+
		            			'<a href="'+ruta_archivo+'" target="_blank" title="'+data['archivos'][i].nombre_original+'">'+
		                        '<img src="/Dazzle/images/otros.png" style="width: 70%;" />'+
		                          '<span class="file-name recortar">'+data['archivos'][i].nombre_original+'</span>'+
		                          '<div class="buttons">'+
		                            '<span class="file-size">'+data['archivos'][i].peso+'Kb</span><br>'+
		                            '<span class="label label-info">'+(data['archivos'][i].etiqueta == null?xmls:data['archivos'][i].etiqueta)+'</span>'+
		                          '</div>'+
		                          '</a>'+
		                      '</li>');
		            		}
		            		else{
		            			$("#archivos_email").append('<li class="estilo-archivo">'+
		            			'<a href="'+ruta_archivo+'" target="_blank"  title="'+data['archivos'][i].nombre_original+'">'+
		                        '<img src="/Dazzle/images/otros.png" style="width: 70%;" />'+
		                          '<span class="file-name recortar" >'+data['archivos'][i].nombre_original+'</span>'+
		                          '<div class="buttons">'+
		                            '<span class="file-size">'+data['archivos'][i].peso+'Kb</span><br>'+
		                          '</div>'+
		                          '</a>'+
		                      '</li>');								
		            		}		            		
		            	}
		            	
						if(data['documentos_formales'].length > 0){
							
							$("#mensaje_estado_validacion").html(data['documentos_formales'][0].observacion);
							
							if(data['documentos_formales'][0].estadoValidacion == 1){	
								$("#detalle_verificacion_xml").css("display","block");						
								$("#es_com").text(data['datos_xml'][0].desEstado);
								$("#es_con").text(data['datos_xml'][0].nombreEstado);
								$("#cond_con").text(data['datos_xml'][0].estado_domicilio);
								$("#detalle_validaciones").css("display","none");
							}
							else{
								$("#detalle_verificacion_xml").css("display","none");
							}
						}
						else{
							$("#mensaje_estado_validacion").text("Correo pendiente de verificación");
							$("#detalle_verificacion_xml").css("display","none");
							$("#detalle_validaciones").css("display","block");
							$("#td_val_pdf").html('<span class="badge badge-info" >Pendiente</span>');
							$("#td_val_xml").html('<span class="badge badge-info" >Pendiente</span>');
							$("#td_val_sap").html('<span class="badge badge-info" >Pendiente</span>');
						}        
		            }
		          }
		        });
	}
	
	function ver_archivo(url){
		$('.modal-body').load(url,function(){
	        $('#modal-archivo').modal({show:true});
	    });
	}
	
	function refrescar_bandeja(){
		
		$("#sin-seleccion").css("display","block");
		$("#correo-seleccionado").removeClass("d-md-block");
		nume_correo = -1;
		cortar_busqueda = 0;
		buscar_total_correos();
		buscar_correos();
		$("#cuerpo").html("");
	}
	
	function buscar_total_correos(){
				
		$.ajax({
          	url: ruta_sistema+"/Bandeja/totalCorreos/",
		  	type: "POST",
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
            if(data['code'] == 1)
            {
              $("#total-correos").text(data['datos'][0].total_email);             
            }
            else{
            	$("#total-correos").text("0");		              
            }
          }
        });				
						
	}
	
	function buscar_correo_asunto(asunto){
				
				$.ajax({
		          	url: ruta_sistema+"/Bandeja/buscarCorreosAsunto/",
				  	type: "POST",
					data: asunto,
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
		            if(data['code'] == 0)
		            {
		              alert("No se encontro ningún resultado");              
		            }
		            else{
						$("#alerta_filtro").css("display","block");
						$("#text_filtro").html(asunto);
		            	$("#cuerpo").html("");
						$("#asunto-busqueda").val("");
						cortar_busqueda = 1;
		            	for(var i=0;i<data['datos'].length;i++){
		            	
		            		$("#cuerpo").append('<tr class="'+data['datos'][i].estado_leido+'" id="'+data['datos'][i].idEmail+'" onClick="mostrar_correo('+data['datos'][i].idEmail+','+data['datos'][i].leido+','+data['datos'][i].validado+')">'+	
		            			'<td class="view-message">'+data['datos'][i].numeroEmail+'</td>'+                       
		                       '<td class="view-message dont-show">'+data['datos'][i].envia+' <br><span class="label label-'+data['datos'][i].label+'">'+data['datos'][i].estado_validacion+'</span> </td>'+
		                       '<td class="view-message">'+data['datos'][i].asunto+'</td>'+
		                       '<td class="view-message inbox-small-cells"></td>'+
		                       '<td class="view-message inbox-small-cells">'+data['datos'][i].cantidad_archivos+'<i class="fa fa-paperclip" idEmail="'+data['datos'][i].idEmail+'" onClick="mostrar_popover.call(this)" data-toggle="popover" data-container="body" data-placement="right" data-html="true" ></i></td>'+
		                       '<td class="view-message text-right">'+data['datos'][i].hora+' <br> '+data['datos'][i].dia+' </td>'+
		                     '</tr>');	
		                     
		            	}          	              
		              
		            }
		          }
		        });				
					
	}
	
	function filtrar_correos(estado, texto){
				
				$.ajax({
		          	url: ruta_sistema+"/Bandeja/buscarFiltroCorreo/",
				  	type: "POST",
					data: ''+estado,
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
		            if(data['code'] == 0)
		            {
		              alert("No se encontro ningún resultado");              
		            }
		            else{
		            	$("#cuerpo").html("");
						$("#alerta_filtro").css("display","block");
						$("#text_filtro").html(texto);
						filtro = estado;
		            	for(var i=0;i<data['datos'].length;i++){
		            	
		            		$("#cuerpo").append('<tr class="'+data['datos'][i].estado_leido+'" id="'+data['datos'][i].idEmail+'" onClick="mostrar_correo('+data['datos'][i].idEmail+','+data['datos'][i].leido+','+data['datos'][i].validado+')">'+	
		            			'<td class="view-message">'+data['datos'][i].numeroEmail+'</td>'+                       
		                       '<td class="view-message dont-show">'+data['datos'][i].envia+' <br><span class="label label-'+data['datos'][i].label+'">'+data['datos'][i].estado_validacion+'</span> </td>'+
		                       '<td class="view-message">'+data['datos'][i].asunto+'</td>'+
		                       '<td class="view-message inbox-small-cells"></td>'+
		                       '<td class="view-message inbox-small-cells">'+data['datos'][i].cantidad_archivos+'<i class="fa fa-paperclip" idEmail="'+data['datos'][i].idEmail+'" onClick="mostrar_popover.call(this)" data-toggle="popover" data-container="body" data-placement="right" data-html="true" ></i></td>'+
		                       '<td class="view-message text-right">'+data['datos'][i].hora+' <br> '+data['datos'][i].dia+' </td>'+
		                     '</tr>');	
		                     
		            	}          	              
		              
		            }
		          }
		        });				
					
	}
	
	function cerrar_alerta(){
		$("#alerta_filtro").css("display","none");
		cortar_busqueda = 0;
		filtro = "0";
		refrescar_bandeja();
	}
	
	function procesar_email(){
		clasificar_documentos();
		clasificar_xml();
		validador_documentos();			
	}
	
	function clasificar_documentos(){
		$.ajax({
		          	url: ruta_sistema+"/Bandeja/clasificarDocumentos/",
				  	type: "POST",
					data: $("#idEmailRechazado").val(),
					contentType: "text/plain",
					dataType : 'json',
					async:false,
		          beforeSend: function(){
		            $("#fondo").css("display","block");
					$("#td_val_pdf").html('<img src="/plugins/img/load_login.gif">');
		          },
		          error: function(){
		            $("#fondo").css("display","none");
					$("#td_val_pdf").html('<span class="badge badge-error" >Hubo un problema</span>');
		            alert("Hubo un problema, Intentelo de nuevo");
		          },
		          success: function(data){
		            $("#fondo").css("display","none");
					if(data['datos']['estado'] == 0){
						$("#td_val_pdf").html('<span class="badge badge-error" >'+data['datos']['mensaje']+'</span>');
					}
					else{
						$("#td_val_pdf").html('<span class="badge badge-success" >'+data['datos']['mensaje']+'</span>');
					}
		          }
		        });
	}
	
	function clasificar_xml(){
		$.ajax({
		          	url: ruta_sistema+"/Bandeja/clasificarXML/",
				  	type: "POST",
					data: $("#idEmailRechazado").val(),
					contentType: "text/plain",
					dataType : 'json',
					async:false,
		          beforeSend: function(){
		            $("#fondo").css("display","block");
					$("#td_val_xml").html('<img src="/plugins/img/load_login.gif">');
		          },
		          error: function(){
		            $("#fondo").css("display","none");
					$("#td_val_xml").html('<span class="badge badge-error" >Hubo un problema</span>');
		            alert("Hubo un problema, Intentelo de nuevo");
		          },
		          success: function(data){
		            $("#fondo").css("display","none");
					if(data['datos']['estado'] == 0){
						$("#td_val_xml").html('<span class="badge badge-error" >'+data['datos']['mensaje']+'</span>');
					}
					else{
						$("#td_val_xml").html('<span class="badge badge-success" >'+data['datos']['mensaje']+'</span>');
					}
		          }
		        });
	}
	
	function validador_documentos(){
		var idEmail = $("#idEmailRechazado").val();
		$.ajax({
		          	url: ruta_sistema+"/Bandeja/validadorDocumentos/",
				  	type: "POST",
					data: idEmail,
					contentType: "text/plain",
					dataType : 'json',
					async:false,
		          beforeSend: function(){
		            $("#fondo").css("display","block");
		          },
		          error: function(){
		            $("#fondo").css("display","none");
		            alert("Hubo un problema, Intentelo de nuevo");
		          },
		          success: function(data){
		            $("#fondo").css("display","none");
					if(data['datos']['estado'] == 1){
						 mostrar_correo(idEmail, 1, 1);
					}				
		          }
		        });
	}