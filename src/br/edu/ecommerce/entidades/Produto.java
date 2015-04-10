package br.edu.ecommerce.entidades;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Produto {

	private int id;
	
	private String titulo;

	@SerializedName("imagem")
	private String img;

	private int qtde;

	private double valor;

	private String sku;
	
	private List<String> categoria;
	
	private List<Venda> vendas;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getQtde() {
		return qtde;
	}

	public void setQtde(int qtde) {
		this.qtde = qtde;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public List<String> getCategoria() {
		return categoria;
	}

	public void setCategoria(List<String> categorias) {
		this.categoria = categorias;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
