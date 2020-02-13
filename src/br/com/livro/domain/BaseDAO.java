package br.com.livro.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
	public BaseDAO() {
		try {
			// Necessario para utilizar o driver JDBC do Postgres, MySQL, etc
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			// Erro de driver JDBC (adicione o driver .jar do Postgres em /WEB-INF/lib)
			e.printStackTrace();
		}
	}
	protected Connection getConnection() throws SQLException {
		// URL de conexao com o banco de dados
		String url = "jdbc:postgresql://localhost:5432/livro";
		
		// Conecta utilizando a URL, usuario e senha
		Connection conn = DriverManager.getConnection(url, "postgres", "ifpb");
		return conn;
	}
	
	public static void main(String[] args) throws SQLException {
		BaseDAO db = new BaseDAO();
		// Testa a conexao
		Connection conn = db.getConnection();
		System.out.println(conn);
		
	}
}