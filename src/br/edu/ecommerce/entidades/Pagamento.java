package br.edu.ecommerce.entidades;

public class Pagamento {

	private int id;

	private String pagtoPaypalId;

	private String estado;

	private String moeda;

	private double valor;

	private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPagtoPaypalId() {
		return pagtoPaypalId;
	}

	public void setPagtoPaypalId(String pagtoPaypalId) {
		this.pagtoPaypalId = pagtoPaypalId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
