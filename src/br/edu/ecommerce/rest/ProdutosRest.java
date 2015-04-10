package br.edu.ecommerce.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.edu.ecommerce.dao.ProdutoDAO;
import br.edu.ecommerce.entidades.Pagamento;
import br.edu.ecommerce.entidades.PagamentoCliente;
import br.edu.ecommerce.entidades.Produto;
import br.edu.ecommerce.entidades.Usuario;
import br.edu.ecommerce.entidades.Venda;
import br.edu.ecommerce.util.Util;

import com.google.gson.Gson;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Path("produto")
public class ProdutosRest {
	
	private ProdutoDAO produtoDAO = new ProdutoDAO();

	@Path("produtos")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProdutos(@QueryParam("str") String str) {
		return Util.converterJSONUTF8(produtoDAO.consultarPorTitulo(str)).toString();
	}
	
	@Path("checkPagto")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificarPagto(@FormParam("idPagto") String idPagto, @FormParam("jsonClientePagto") String jsonClientePagto, @FormParam("idUsuario") String idUsuario) {
		br.edu.ecommerce.rest.Response r = new br.edu.ecommerce.rest.Response();
		try {
			OAuthTokenCredential tokenCredential = Payment.initConfig(getClass().getClassLoader().getResourceAsStream("sdk_config.properties"));
			
			String accessToken = tokenCredential.getAccessToken();
			
			APIContext apiContext = new APIContext(accessToken);
			
			Payment pagto = Payment.get(apiContext, idPagto);
			
			if (!pagto.getState().equals("approved")) {
				r.setErro(true);
				r.setMsg("Pagamento não verificado. Status: ");
				return Response.ok(new Gson().toJson(r), MediaType.APPLICATION_JSON).build();
			}
			
			Gson gson = new Gson();
			PagamentoCliente pagtoParam = gson.fromJson(jsonClientePagto, PagamentoCliente.class);
			double valorCliente = pagtoParam.getQuantia();
			String moedaCliente = pagtoParam.getMoeda();
			
			Transaction trans = pagto.getTransactions().get(0);
			String valorServidor = trans.getAmount().getTotal();
			String moedaServidor = trans.getAmount().getCurrency();
			
			String estadoVenda = trans.getRelatedResources().get(0).getSale().getState();
			
			// storePayment($payment->getId(), $userId, $payment->getCreateTime(), $payment->getUpdateTime(), $payment->getState(), $amount_server, $amount_server);
			Pagamento pagtoFinal = new Pagamento();
			pagtoFinal.setEstado(pagto.getState());
			pagtoFinal.setPagtoPaypalId(pagto.getId());
			
			Usuario usuario = new Usuario();
			usuario.setId(idUsuario != null ? Integer.parseInt(idUsuario) : 1);
			pagtoFinal.setUsuario(usuario);
			
			pagtoFinal.setMoeda(moedaCliente);
			pagtoFinal.setValor(Double.parseDouble(valorServidor));
			
			int idPagtoBD = produtoDAO.salvarPagto(pagtoFinal);
			pagtoFinal.setId(idPagtoBD);
			
			if (Double.parseDouble(valorServidor) != valorCliente) {
				r.setErro(true);
				r.setMsg("Quantias de pagamento não conferem!");
				return Response.ok(new Gson().toJson(r), MediaType.APPLICATION_JSON).build();
			}
			
			if (!moedaServidor.equals(moedaCliente)) {
				r.setErro(true);
				r.setMsg("Moedas de pagamento não conferem!");
				return Response.ok(new Gson().toJson(r), MediaType.APPLICATION_JSON).build();
			}
			
			if (!estadoVenda.equals("completed")) {
				r.setErro(true);
				r.setMsg("Venda não completada!");
				return Response.ok(new Gson().toJson(r), MediaType.APPLICATION_JSON).build();
			}
			
			inserirItensVendas(pagtoFinal, trans, estadoVenda);
			
		} catch (PayPalRESTException ex) {
			ex.printStackTrace();
			return Response.serverError().entity("Erro na comunicação com o PayPal. Favor tentar novamente!").build();
		}
		
		r.setErro(false);
		r.setMsg("Pagamento verificado com sucesso!");
		
		return Response.ok(new Gson().toJson(r), MediaType.APPLICATION_JSON).build();
	}
	
	private void inserirItensVendas(Pagamento pagtoFinal, Transaction trans, String estadoVenda) {
		ItemList listaItens = trans.getItemList();
		
		for (Item i : listaItens.getItems()) {
			Venda venda = new Venda();
			// storeSale($payment_id, $product_id, $state, $salePrice, $quantity) {
			venda.setQtde(Integer.parseInt(i.getQuantity()));
			venda.setPreco(Double.parseDouble(i.getPrice()));
			venda.setPagamento(pagtoFinal);
			venda.setEstado(estadoVenda);
			
			Produto produto = produtoDAO.consultarPorSku(i.getSku());
			venda.setProduto(produto);
			
			produtoDAO.salvarVenda(venda);
		}
	}

}
