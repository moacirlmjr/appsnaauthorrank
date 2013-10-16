package br.com.ufpb.appsnaauthorrank.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class DAOUtil {
	
	private static final String driver = "com.mysql.jdbc.Driver";

	public static Connection returnConnection(String url, String user, String senha) throws Exception {
		Driver d = new com.mysql.jdbc.Driver();
		DriverManager.registerDriver(d);
		return DriverManager.getConnection(url, user, senha);
	}
}
