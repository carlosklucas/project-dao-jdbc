package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DbException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		// Classe SQL é que contém classes que fazem a ligação ao JDBC
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// instanciar o objecto do tipo statement com variavel de ligaçao ao JDBC
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();

			//o resultSet começa sempre na posição zero que não contém valores, entao fazemos maior qu zero
			if (rs.next()) {
				
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName("DepName");
				Seller sel = new Seller();
				sel.setId(rs.getInt("Id"));
				sel.setName(rs.getString("Name"));
				sel.setEmail(rs.getString("Email"));
				sel.setBaseSalary(rs.getDouble("BaseSalary"));
				sel.setBirthDate(rs.getDate("BirthDate"));
				sel.setDepartment(dep);
				
				return sel;
			} 
			
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbException.closeStatement(st);
			DbException.closeResutSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
