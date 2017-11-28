package facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import enums.CouponType;
import exeptions.DBDAOExeption;
import exeptions.FacadeExeptions;
import javabeans.Coupon;
import javabeans.Customer;

public class CustomerFacade implements CouponClientFacade {

	private static CustomerDBDAO customerDBDAO = new CustomerDBDAO();
	private static CouponDBDAO couponDBDAO = new CouponDBDAO();

	public CustomerFacade() {}

	/** purchasing of coupons */

	public static void purchaseCoupon(Long customerID,Coupon coupon) throws FacadeExeptions {
		
		try {
			coupon = couponDBDAO.getCoupon(coupon.getId());
			Collection<Coupon> customerCoupons = new ArrayList<>();
			customerCoupons = customerDBDAO.getCustomerCoupons(customerID);
			if (!customerCoupons.contains(coupon)){
				//if (coupon.getEndDate().after(new Date(System.currentTimeMillis()))){
					if (coupon.getAmount() > 0) {
						coupon.setAmount(coupon.getAmount() - 1);
						couponDBDAO.updateCoupon(coupon);
						customerDBDAO.getCustomerCoupon(customerID, coupon.getId());
						System.out.println("Coupon " + coupon.getTitle() + " " + coupon.getId() + " Purchased!");
						System.out.println("Expires at: " + coupon.getEndDate().toString());
					}
				}
			//}
		}
		catch(DBDAOExeption e){
			throw new FacadeExeptions("Failed to purchase Coupon. Please contact our support team.",e);
		} 
	}

	/** all coupons that was purchased */

	public static Collection<Coupon> getAllPurchasedCoupons(Long customerID) throws FacadeExeptions  {
		Collection<Coupon> purchased = null;
		try {
			purchased = customerDBDAO.getCustomerCoupons(customerID);
			System.out.println("Purchased coupons retrieved!");
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve purchased Coupons. Please contact our support team.",e);
		}
		return purchased;
	}

	/** all coupons that was purchased sorted by type */

	public static Collection<Coupon> getAllPurchasedCouponsByType(Long customerID,CouponType type) throws FacadeExeptions  {
		Collection<Coupon> purchasedOfType = new ArrayList<>();
		try {
			purchasedOfType = customerDBDAO.getCustomerCoupons(customerID);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupons. Please contact our support team.",e);
		}
		Iterator<Coupon> iter = purchasedOfType.iterator();
		Coupon coup = new Coupon();
		while (iter.hasNext()) {
			coup = iter.next();
			if(!type.equals(coup.getType())){
				iter.remove();
			}
		}
		System.out.println("Purchased coupons of type " + type + " retrieved!");
		return purchasedOfType;
	}

	/** all coupons that was purchased sorted by price */

	public static Collection<Coupon> getAllPurchasedCouponsByPrice(Long customerID,double price) throws FacadeExeptions {
		Collection<Coupon> purchasedOfPrice = new ArrayList<>();
		try {
			purchasedOfPrice = customerDBDAO.getCustomerCoupons(customerID);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupons. Please contact our support team.",e);
		}
		Iterator<Coupon> iter = purchasedOfPrice.iterator();
		while(iter.hasNext()){
			Coupon coup = iter.next();
			if (coup.getPrice() < price)
				iter.remove();
		}
		System.out.println("Purchased coupons that cost at least " + price + " retrieved!");
		return purchasedOfPrice;
	}

	/** login facade */

	public CouponClientFacade login(String name, String password) throws FacadeExeptions {
		Customer customer = null;
		try {
			customer = customerDBDAO.login(name, password);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Name or Passowrd is wrong! Please try again", null);
		}
		if (customer == null)
			throw new FacadeExeptions("Login FAILED. Please consult with your administrator.", null);

		CustomerFacade clientFacade = new CustomerFacade();
		return clientFacade;
	}

	@Override
	public String toString() {
		return "customer";
	}

}
