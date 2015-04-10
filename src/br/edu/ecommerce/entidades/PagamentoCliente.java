package br.edu.ecommerce.entidades;

import com.google.gson.annotations.SerializedName;

public class PagamentoCliente {

	@SerializedName("amount")
	private double quantia;

	@SerializedName("currency_code")
	private String moeda;

	public double getQuantia() {
		return quantia;
	}

	public void setQuantia(double quantia) {
		this.quantia = quantia;
	}

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

}
