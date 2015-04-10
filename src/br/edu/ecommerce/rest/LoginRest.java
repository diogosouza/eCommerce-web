package br.edu.ecommerce.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import br.edu.ecommerce.dao.LoginBO;
import br.edu.ecommerce.entidades.Usuario;

@Path("login")
public class LoginRest {
	
	private LoginBO loginBO = new LoginBO();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("logar")
	public String logar(@QueryParam("usuario") String user, @QueryParam("senha") String pass) {
		Usuario usuario = loginBO.validarLogin(user, pass);
		String resultado = null;
		if (usuario != null) {
			resultado = new Gson().toJson(usuario);
		}
		return resultado;
	}

}
