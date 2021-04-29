package com.ebisu.stream.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
public class ApiService {

	public String consumir_api(String urls, String data) {
		String salida = "";
		
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			
			//if(conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				//throw new RuntimeException("Conexion fallida con codigo: "+conn.getResponseCode());
			//}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output = "";
			
			String linea = "";
			while (linea != null) {
				if(linea == null)
					break;
				output += linea;
				linea = br.readLine();
			}
			
			salida = output;
			conn.disconnect();			
					
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
				
		return salida;
	}
}
