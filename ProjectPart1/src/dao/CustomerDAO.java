package dao;

/** DAO Customer Layer */

import java.util.Collection;

import exeptions.DAOExeption;
import javabeans.Coupon;
import javabeans.Customer;

public interface CustomerDAO {

	public void createCustomer(Customer customer) throws DAOExeption;

	public void removeCustomer(Customer customer) throws DAOExeption;

	public void updateCustomer(Customer customer) throws DAOExeption;

	public Customer getCustomer(long id) throws DAOExeption;

	public Collection<Customer> getAllCustomers() throws DAOExeption;

	public Collection<Coupon> getCustomerCoupons(long custId) throws DAOExeption;

	public void getCustomerCoupon(long companyId, long couponId) throws DAOExeption;

	public void getAllCustomerCoupon(long customerId) throws DAOExeption;

	public Customer login(String custName, String password) throws DAOExeption;
}
