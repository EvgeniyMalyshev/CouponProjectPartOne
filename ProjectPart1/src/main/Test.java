package main;

import java.text.SimpleDateFormat;
import enums.CouponType;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;
import utils.CouponSystem;
/** Test method
 * Because it is the test method there is some conditions for use: 
 * When you delete company/customer you delete and all coupons that have 
 * connections with this company/customer. 
 * This method use ".removeCoupon", so we didn't show it like separate 
 * method, but we leave it in code like comment (lines 90-92).So, if you what to delete
 * coupon with method ".removeCoupon" you must comment line 86 and 88, and uncomment lines 90-92. 
 * */

public class Test {
	public static void main(String[] args) throws Exception {


		/** only for test*/
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


		java.util.Date startDate = simpleDateFormat.parse("13/09/2017");
		java.sql.Date sqlStart = new java.sql.Date(startDate.getTime());


		java.util.Date endDate = simpleDateFormat.parse("14/09/2017");
		java.sql.Date sqlEnd = new java.sql.Date(endDate.getTime());



		java.util.Date nowDate = simpleDateFormat.parse("12/09/2017");
		java.sql.Date sqlnow = new java.sql.Date(nowDate.getTime());

		/** start of test*/
		
		CouponSystem.getInstance();

		

		Coupon good_coupon = new Coupon("Free pizza", sqlStart, sqlEnd, 10, CouponType.FOOD, "32 shkalim pizza", 32.0, "none");

		long idCompany = ((java.util.List<Company>)AdminFacade.getAllCompanies()).get(0).getId();
		long idCustomer = ((java.util.List<Customer>)AdminFacade.getAllCustomers()).get(0).getId();
		

		

		CustomerFacade.purchaseCoupon(idCompany, good_coupon);

		CustomerFacade.getAllPurchasedCoupons(idCustomer);

		CustomerFacade.getAllPurchasedCouponsByType(idCustomer,CouponType.FOOD);

		CustomerFacade.getAllPurchasedCouponsByPrice(idCustomer,32.0);

		
		//long idCoupon =  ((java.util.List<Coupon>)AdminFacade.getAllCoupons()).get(0).getId();

		//CompanyFacade.removeCoupon(idCoupon, good_coupon);

		CouponSystem.getInstance().shutdown();

	}
}
