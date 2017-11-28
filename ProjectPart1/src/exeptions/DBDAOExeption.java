package exeptions;

public class DBDAOExeption extends DAOExeption {

	/** Exceptions of third layer */

	private static final long serialVersionUID = 1L;

	public DBDAOExeption(String msg, Throwable throwable) {
		super(msg, throwable);

	}

}
