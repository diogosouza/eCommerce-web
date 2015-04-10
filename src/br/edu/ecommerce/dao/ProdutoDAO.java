package br.edu.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ecommerce.db.DBUtil;
import br.edu.ecommerce.entidades.Pagamento;
import br.edu.ecommerce.entidades.Produto;
import br.edu.ecommerce.entidades.Venda;

public class ProdutoDAO {

	public List<Produto> listarProdutos() {
		List<Produto> resultado = new ArrayList<Produto>();

		Connection con = null;
		try {
			con = DBUtil.getConnexao();

			String sql = "SELECT * FROM TB_PRODUTO";

			PreparedStatement st = con.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Produto produto = new Produto();
				
				int idProduto = rs.getInt("id_produto");
				
				produto.setId(idProduto);
				produto.setTitulo(rs.getString("titulo"));
				produto.setQtde(rs.getInt("qtde"));
				produto.setSku(rs.getString("sku"));
				produto.setValor(rs.getDouble("valor"));
				produto.setImg(rs.getString("img"));
				produto.setCategoria(consultarCategoriasPorId(idProduto));

				resultado.add(produto);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return resultado;
	}
	
	public List<Produto> consultarPorTitulo(String str) {
		List<Produto> resultado = new ArrayList<Produto>();
		
		Connection con = null;
		try {
			con = DBUtil.getConnexao();
			
			String sql = "SELECT * FROM TB_PRODUTO";
			if (str != null) {
				sql += " WHERE TITULO LIKE ?";
			}
			
			PreparedStatement st = con.prepareStatement(sql);
			if (str != null) {
				st.setString(1, "%"+ str + "%");
			}
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Produto produto = new Produto();
				
				int idProduto = rs.getInt("id_produto");
				
				produto.setId(idProduto);
				produto.setTitulo(rs.getString("titulo"));
				produto.setQtde(rs.getInt("qtde"));
				produto.setSku(rs.getString("sku"));
				produto.setValor(rs.getDouble("valor"));
				produto.setImg(rs.getString("img"));
				produto.setCategoria(consultarCategoriasPorId(idProduto));
				
				resultado.add(produto);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return resultado;
	}
	
	public Produto consultarPorSku(String sku) {
		Produto produto = new Produto();
		
		Connection con = null;
		try {
			con = DBUtil.getConnexao();
			
			String sql = "SELECT * FROM TB_PRODUTO";
			if (sku != null) {
				sql += " WHERE SKU = ?";
			}
			
			PreparedStatement st = con.prepareStatement(sql);
			if (sku != null) {
				st.setString(1, sku);
			}
			
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				int idProduto = rs.getInt("id_produto");
				
				produto.setId(idProduto);
				produto.setTitulo(rs.getString("titulo"));
				produto.setQtde(rs.getInt("qtde"));
				produto.setSku(rs.getString("sku"));
				produto.setValor(rs.getDouble("valor"));
				produto.setImg(rs.getString("img"));
				produto.setCategoria(consultarCategoriasPorId(idProduto));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return produto;
	}


	public List<String> consultarCategoriasPorId(int idProduto) {
		List<String> resultado = new ArrayList<String>();

		Connection con = null;
		try {
			con = DBUtil.getConnexao();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    cat.descricao");
			sql.append(" FROM");
			sql.append("    tb_categoria cat");
			sql.append("        INNER JOIN");
			sql.append("    tb_produto_categoria pc ON cat.id_categoria = pc.id_categoria");
			sql.append(" WHERE");
			sql.append("    pc.id_produto = ?");

			PreparedStatement st = con.prepareStatement(sql.toString());
			st.setInt(1, idProduto);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				resultado.add(rs.getString("descricao"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return resultado;
	}
	
	public int salvarPagto(Pagamento pagamento) {
		int chaveGerada = 0;
		
		Connection con = null;
		try {
			con = DBUtil.getConnexao();
			
			String sql = "INSERT INTO TB_PAGAMENTO(PAGTOPAYPALID, ESTADO, VALOR, MOEDA, IDUSUARIO) VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, pagamento.getPagtoPaypalId());
			st.setString(2, pagamento.getEstado());
			st.setDouble(3, pagamento.getValor());
			st.setString(4, pagamento.getMoeda());
			st.setInt(5, pagamento.getUsuario().getId());
			
			st.execute();
			ResultSet rs = st.getGeneratedKeys();
			if(rs.next()) {
				chaveGerada = rs.getInt(1);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return chaveGerada;
	}
	
	public List<Produto> salvarVenda(Venda venda) {
		List<Produto> resultado = new ArrayList<Produto>();
		
		Connection con = null;
		try {
			con = DBUtil.getConnexao();
			
			String sql = "INSERT INTO TB_VENDA(ESTADO, PRECO, QTDE, PRODUTOID, PAGTOID) VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, venda.getEstado());
			st.setDouble(2, venda.getPreco());
			st.setInt(3, venda.getQtde());
			st.setInt(4, venda.getProduto().getId());
			st.setInt(5, venda.getPagamento().getId());
			
			st.execute();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return resultado;
	}
}
