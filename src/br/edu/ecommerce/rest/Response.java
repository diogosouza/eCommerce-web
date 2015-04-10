package br.edu.ecommerce.rest;

public class Response {

	private String msg;

	private boolean erro;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isErro() {
		return erro;
	}

	public void setErro(boolean erro) {
		this.erro = erro;
	}
	
}
