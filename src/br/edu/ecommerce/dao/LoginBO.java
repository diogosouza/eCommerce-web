package br.edu.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.edu.ecommerce.db.DBUtil;
import br.edu.ecommerce.entidades.Usuario;

public class LoginBO {

	public Usuario validarLogin(String usuario, String senha) {
		Usuario user = null;
		
		Connection con = null;
		try {
			con = DBUtil.getConnexao();
			
			String sql = "SELECT * FROM TB_USUARIO WHERE LOGIN = ? AND SENHA = ?";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, usuario);
			st.setString(2, senha);
			
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				user = new Usuario();
				user.setId(rs.getInt("ID_USUARIO"));
				user.setLogin(rs.getString("LOGIN"));
				user.setSenha(rs.getString("SENHA"));
				user.setLogado(true);
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
		
		return user;
	}
	
}
