package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	// interface que itnernamente retorna a classe da sua implementacao
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	// interface que itnernamente retorna a classe da sua implementacao
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}

}
