package dao;

/** DAO Coupon Layer */

import java.util.Collection;

import enums.CouponType;
import exeptions.DAOExeption;
import javabeans.Coupon;

public interface CouponDAO {


	public void createCoupon(Coupon coupon, long companyId) throws DAOExeption;

	public void removeCoupon(Coupon coupon) throws DAOExeption;

	public void updateCoupon(Coupon coupon) throws DAOExeption;

	public Coupon getCoupon(long id) throws DAOExeption;

	public Collection<Coupon> getAllCoupons() throws DAOExeption;

	public Collection<Coupon> getCompanyCoupons(long companyId) throws DAOExeption;

	public Collection<Coupon> getCouponByType(CouponType couponType) throws DAOExeption;

	void removeCompanyCoupon(long couponId) throws DAOExeption;

	void removeCustomerCoupon(long couponId) throws DAOExeption;



}


