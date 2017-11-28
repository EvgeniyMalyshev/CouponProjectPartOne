package exeptions;

public class DAOExeption extends CouponSystemExeption {

	/** Exceptions of second layer */

	private static final long serialVersionUID = 1L;
	public DAOExeption(String msg, Throwable throwable){
		super(msg,throwable);
	}

}
