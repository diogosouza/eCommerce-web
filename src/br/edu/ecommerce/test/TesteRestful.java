package br.edu.ecommerce.test;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class TesteRestful {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget alvoWS = client.target(getURIBase());
		
		System.out.println(alvoWS.request(MediaType.TEXT_XML).get(String.class));
		System.out.println(alvoWS.request(MediaType.TEXT_HTML).get(String.class));
		System.out.println(alvoWS.request(MediaType.TEXT_PLAIN).get(String.class));
		System.out.println(alvoWS.request(MediaType.APPLICATION_JSON).get(String.class));
	}
	
	private static URI getURIBase() {
		return UriBuilder.fromUri("http://localhost:8080/LoginRestful/login").build();
	}

}
