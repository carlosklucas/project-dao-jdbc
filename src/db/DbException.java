package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DbException(String msg) {
		super(msg);

	}

	public static void closeStatement (Statement st) {

		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// lanco a minha excepcao personalizada (throw ne DbException) do tipo runtimeException
				throw new DbException (e.getMessage());
			}
		}
	}
	
	public static void closeResutSet (ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// lanco a minha excepcao personalizada (throw new DbException) do tipo runtimeException
				throw new DbException (e.getMessage());
			}
		}
	}
	
}
