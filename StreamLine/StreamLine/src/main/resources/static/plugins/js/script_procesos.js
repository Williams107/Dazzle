
function validar_check(idProceso){
	if($("#radio_descarga_correo").prop('checked')){
		activar_desactivar(idProceso);
	}
	else{
		alert("descheckado");
	}
}

function activar_desactivar(id){
	
	if(id != ""){
		
		$.ajax({
		          	url: "/Dazzle/Config/levantarProcesos/",
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
		          }
		        });		
		
	}
	
}