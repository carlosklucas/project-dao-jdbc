package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
//		Department dep = new Department(1, "Books");
//		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, dep);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		//DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TEST 1: seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("=== TEST 2: seller findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller sel : list) {
			System.out.println(sel);
		}

		System.out.println("=== TEST 3: seller findAll ===");
		list = sellerDao.findAll();
		for (Seller sel : list) {
			System.out.println(sel);
		}
		
		System.out.println("=== TEST 4: seller Insert ===");
		Seller sellerInsert = new Seller(null, "carlos", "carloslucas@gmail.com",
				new Date(), 4000.0, department);
		sellerDao.insert(sellerInsert);
		System.out.println("Inserted! New id = "  + sellerInsert.getId());
		
		System.out.println("=== TEST 5: seller Update ===");
		seller = sellerDao.findById(1);
		seller.setName("Carlos2");
		sellerDao.update(seller);
		System.out.println("Updated! New id = "  + seller.getId());
		
		System.out.println("=== TEST 6: seller Delete ===");
		sellerDao.delete(6);
		System.out.println("Delete completed");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		sellerDao.delete(id);
		System.out.println("Deleted id:" + id);
		
	}

}
