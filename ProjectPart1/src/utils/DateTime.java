package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import dbdao.CouponDBDAO;
import exeptions.CouponSystemExeption;
import javabeans.Coupon;

public  class DateTime {
	public static java.sql.Date convertTime(java.util.Date date) {
		if (date == null){
			return null;
		}
		return new java.sql.Date(date.getTime());

	}
	
	/** function to remove old coupons*/
	
	public static void removeExpiredCoupon() throws CouponSystemExeption  {
		Collection<Coupon> taskList = null;
		CouponDBDAO couponTask = new CouponDBDAO();
		taskList = (ArrayList<Coupon>) couponTask.getAllCoupons();
		Iterator<Coupon> iter = taskList.iterator();
		if (iter != null) {
			while (iter.hasNext()) {
				Coupon currentCoupon = iter.next();
				if (!currentCoupon.getEndDate().after(new java.util.Date())) {
					couponTask.removeCoupon(currentCoupon);

				}
			}
		}
	}
}
