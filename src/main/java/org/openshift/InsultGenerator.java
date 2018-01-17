package org.openshift;

import java.sql.*;


public class InsultGenerator {
	public String generateInsult() {
		String theInsult = "";
		String vowels = "AEIOU";
		String article = "an";

		String databaseURL = "jdbc:postgresql://";
		databaseURL += System.getenv("POSTGRESQL_SERVICE_HOST");
		databaseURL += "/" + System.getenv("POSTGRESQL_DATABASE");

		String username = System.getenv("POSTGRESQL_USER");
		String password = System.getenv("POSTGRESQL_USER_PASSWORD");
		try {
			Connection connection = DriverManager.getConnection(databaseURL,username,password);
			if (connection != null)
			{
				String sql = "SELECT a.string AS first, b.string AS second, c.string AS noun " +
						"FROM short_adjective a, long_adjective b, noun c" +
						"ORDER BY random()" +
						"LIMIT 1";

				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					if(vowels.indexOf(rs.getString("first").charAt(0)) == -1)
					{
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!", article, rs.getString("first"), rs.getString("second"), rs.getString("noun"));
				}
				rs.close();
				connection.close();
			}
		} catch (SQLException e) {
			return "Problem connecting to the database!";
		}

		return theInsult;
	}

}
