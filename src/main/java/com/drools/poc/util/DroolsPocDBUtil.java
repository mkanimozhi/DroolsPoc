package com.drools.poc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DroolsPocDBUtil {

	private static DroolsPocDBUtil dbutil;
	private static Statement sta;

	static {
		new DroolsPocDBUtil();
	}

	private DroolsPocDBUtil() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restservice","restservice","restservice");
			sta = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DroolsPocDBUtil getInstance() {

		if(dbutil == null) {
			dbutil = new DroolsPocDBUtil();
		}
		return dbutil;
	}


	public static boolean save(String sql) throws SQLException {
		boolean saveStatus = true;		
		System.out.println("sql = "+sql);
		int i = sta.executeUpdate(sql);
		System.out.println("i = "+i);
		if (i == -1) {
			System.out.println("db error : " + sql);
			saveStatus = false;
		}
//		sta.close();
		return saveStatus;
	}

	public static ResultSet executeQuery(String sql) throws SQLException {
		ResultSet resultSet = sta.executeQuery(sql);
		return resultSet;
	}

}