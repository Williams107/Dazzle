package com.ebisu.stream.Services;

import java.io.File;
import java.util.HashMap;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class CorreosService {
	//Importante hacer la inyección de dependencia de JavaMailSender:
    @Autowired
    private JavaMailSender mailSender;
    
    private final TemplateEngine templateEngine;
    
    public CorreosService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
    	this.templateEngine = templateEngine;
        this.mailSender = javaMailSender;
    }

    //Pasamos por parametro: destinatario, asunto y el mensaje
    public void sendEmail(String destino, String subject, String template, HashMap<String, String> datos_correo, String archivo) throws MessagingException {
    	
    	Context context = new Context();
    	context.setVariable("datos", datos_correo);

        String contenido = templateEngine.process(template, context);

        javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        helper.setSubject(subject);
        helper.setText(contenido, true);
        helper.setTo(destino);
        helper.setFrom("setdocumentos_parapago@marinasol.com.pe");
        
        if(!archivo.equals("")) {
        	 FileSystemResource file  = new FileSystemResource(new File(archivo));
             helper.addAttachment("archivo.pdf", file);
        }        
        mailSender.send(mimeMessage);        
    }
}
