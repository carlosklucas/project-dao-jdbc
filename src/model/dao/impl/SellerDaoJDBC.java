package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn = null;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;

		try {
			// instanciar o objecto do tipo statement com variavel de ligaçao ao JDBC
			st = conn.prepareStatement(
					"INSERT INTO "
							+ "Seller(Name, Email, BirthDate, BaseSalary, DepartmentId) "
							+ "Values"
							+ "(?, ?, ?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if (rs.next())  {
					//getInt(1) porque vai ser 1 da primeira coluna das generatedKeys
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DbException.closeResutSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbException.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement(
					"UPDATE seller "
							+ "SET Name = ?, "
							+ "Email = ?, "
							+ "BirthDate = ?, "
							+ "BaseSalary = ?, "
							+ "DepartmentId = ? "
							+ "WHERE "
							+ "Id = ?;");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, 200.0);
			st.setInt(5, 2);
			st.setInt(6, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DbException.closeStatement(st);
		}		
		
	}

	@Override
	public void delete(Integer id) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"DELETE FROM seller "
					+ "WHERE "
					+ "Id = ?;");

			st.setInt(1, id);
			st.executeUpdate();
			

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbException.closeStatement(st);
		}
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
				
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);
				
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}

	//envia a excepção a ser tratada pela classe seguinte
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName("DepName");
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// Classe SQL é que contém classes que fazem a ligação ao JDBC
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// instanciar o objecto do tipo statement com variavel de ligaçao ao JDBC
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "ORDER BY Name");
			
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			//o resultSet começa sempre na posição zero que não contém valores, entao fazemos maior qu zero
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantiateSeller(rs, dep);
				list.add(sel);
				}
				
				return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbException.closeStatement(st);
			DbException.closeResutSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		// Classe SQL é que contém classes que fazem a ligação ao JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		int id = department.getId();

		try {
			// instanciar o objecto do tipo statement com variavel de ligaçao ao JDBC
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "WHERE DepartmentId = ? "
							+ "ORDER BY Name");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			//o resultSet começa sempre na posição zero que não contém valores, entao fazemos maior qu zero
			while (rs.next()) {
				
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantiateSeller(rs, department);
				list.add(sel);
				}
				
				return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DbException.closeStatement(st);
			DbException.closeResutSet(rs);
		}
	}
	
	

}
