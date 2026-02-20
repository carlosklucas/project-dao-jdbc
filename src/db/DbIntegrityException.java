package db;

public class DbIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DbIntegrityException(String msg) {
		//passo a string para a super class RuntimeException
		super(msg);
	}

}
