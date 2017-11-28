package facade;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import enums.CouponType;
import exeptions.DBDAOExeption;
import exeptions.FacadeExeptions;
import javabeans.Company;
import javabeans.Coupon;

public class CompanyFacade implements CouponClientFacade {

	private static CompanyDBDAO companyDBDAO = new CompanyDBDAO();
	private static CouponDBDAO couponDBDAO = new CouponDBDAO();

	public CompanyFacade() {}

	/**create of coupon*/

	public static void createCoupon(Coupon coupon,long companyID) throws Exception {
		couponDBDAO.createCoupon(coupon, companyID);
	}

	/** remove of coupon*/

	public static void removeCoupon(Coupon coupon) throws FacadeExeptions {
		try{
			couponDBDAO.removeCompanyCoupon(coupon.getId());
			couponDBDAO.removeCustomerCoupon(coupon.getId());
			couponDBDAO.removeCoupon(coupon);
		}catch( DBDAOExeption e){
			throw new FacadeExeptions ("Failed to remove Coupon. Please consult with your administartor",e);
		}
	}
	/** update of coupon */

	public static void updateCoupon(Coupon coupon) throws FacadeExeptions {
		try {
			couponDBDAO.updateCoupon(coupon);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to update Coupon. Please consult with your administrator",e);
		}
	}

	/** get coupon by id*/

	public static Coupon getCoupon(long id) throws FacadeExeptions {
		try {
			return couponDBDAO.getCoupon(id);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupon. Please consult with yur administrator.",e);
		}
	}

	/** get coupons by company id */

	public static Collection<Coupon> getAllCoupons(long companyId) throws FacadeExeptions  {
		Collection<Coupon> coupons = new ArrayList<>();
		try {
			coupons = couponDBDAO.getCompanyCoupons(companyId);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve all Coupons. Please consult your administrator.",e);
		}
		return coupons;
	}

	/** get coupons by company id and coupon type */

	public static Collection<Coupon> getCouponByType(long companyId,CouponType couponType) throws FacadeExeptions  {
		Collection<Coupon> couponsOfType = new ArrayList<>();
		try {
			couponsOfType = couponDBDAO.getCompanyCoupons(companyId);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupons according to type. Please consult your administrator.",e);
		}
		Iterator<Coupon> iter = couponsOfType.iterator();
		while (iter.hasNext()) {
			Coupon coup = iter.next();
			if (!(coup.getType().equals(couponType)))
				iter.remove();
		}
		System.out.println("Purchased coupons of type " + couponType + " retrieved!");
		return couponsOfType;
	}

	/** get coupons by company id and coupon price */

	public static Collection<Coupon> getAllPurchasedCouponsByPrice(long compID, double price) throws FacadeExeptions {
		Collection<Coupon> purchasedOfPrice = new ArrayList<>();
		try {
			purchasedOfPrice = couponDBDAO.getCompanyCoupons(compID);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupons. Please contact our support team.",e);
		}
		Iterator<Coupon> iter = purchasedOfPrice.iterator();
		while(iter.hasNext()){
			Coupon coup = iter.next();
			if (coup.getPrice() <= price)
				iter.remove();
		}
		System.out.println("Purchased coupons that cost at least " + price + " retrieved!");
		return purchasedOfPrice;
	}

	/** get coupons by company id and coupon date */

	public static Collection<Coupon> getAllPurchasedCouponsByTime(long companyId,Date date) throws FacadeExeptions {
		Collection<Coupon> purchasedOfEndTime = new ArrayList<>();
		try {
			purchasedOfEndTime = couponDBDAO.getCompanyCoupons( companyId);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to retrieve Coupons. Please contact our support team.",e);
		}
		Iterator<Coupon> iter = purchasedOfEndTime.iterator();
		while(iter.hasNext()){
			Coupon coup = iter.next();
			if (!coup.getEndDate().after(new java.util.Date()))
				iter.remove();
		}
		System.out.println("Purchased coupons that havn't  get expiration time retrieved!");
		return purchasedOfEndTime;
	}

	/** login facade */

	@Override
	public CouponClientFacade login(String name, String password) throws FacadeExeptions{
		Company company = null;
		try {
			company = companyDBDAO.login(name, password);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Name or Passowrd is wrong! Please try again", null);
		}
		if (company == null)
			throw new FacadeExeptions("Login FAILED. Please consult with your administrator.", null);

		CompanyFacade clientFacade = new CompanyFacade();
		return clientFacade;
	}

	@Override
	public String toString() {
		return "company";
	}

}
