package exeptions;

public class CouponSystemExeption extends Exception {

	/** Exceptions of first layer */

	private static final long serialVersionUID = 1L;
	public CouponSystemExeption(){

	}
	public CouponSystemExeption(String msg, Throwable ex){
		super (msg,ex);
	}
}
