package facade;

import java.util.Collection;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import exeptions.DBDAOExeption;
import exeptions.FacadeExeptions;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;

public class AdminFacade implements CouponClientFacade {

	private static CompanyDBDAO companyDBDAO = new CompanyDBDAO();
	private static CouponDBDAO couponDBDAO = new CouponDBDAO();
	private static CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	/** Constructor */

	public AdminFacade() {
	}

	/** create company */

	public static void createCompany(Company company) throws FacadeExeptions {
		try {
			companyDBDAO.createCompany(company);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Problems with creating  Company.", e);
		}
	}

	/** remove company */

	public static void removeCompany(Company company) throws FacadeExeptions {
		try {
			Collection<Coupon> coupons = couponDBDAO.getCompanyCoupons(company.getId());
			for (Coupon c : coupons) {
				couponDBDAO.removeCompanyCoupon(c.getId());
				couponDBDAO.removeCustomerCoupon(c.getId());
				couponDBDAO.removeCoupon(c);
			}
			companyDBDAO.removeCompany(company);
			System.out.println("Company removed!");
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Failed to remove Company. Please check the cause for this.",e);
		}
	}

	/** update company */

	public static void updateCompany(Company company) throws FacadeExeptions {
		try {
			companyDBDAO.updateCompany(company);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to update company. Please check the cause for this.",e);
		}
	}

	/** get company by id */
	public static Company getCompany(Long id) throws FacadeExeptions {
		try {
			return companyDBDAO.getCompany(id);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to retrieve company. Please check the cause for this.",e);
		}
	}

	/** get all company's */

	public static Collection<Company> getAllCompanies() throws FacadeExeptions {
		try {
			return companyDBDAO.getAllCompanies();
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to retrieve companies. Please check the cause for this.",e);
		}
	}

	/** create customer */

	public static void createCustomer(Customer customer) throws FacadeExeptions {
		try {
			customerDBDAO.createCustomer(customer);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Problems with creating  Customer.", e);
		}
	}

	/** remove customer */

	public static void removeCustomer(Customer customer) throws FacadeExeptions {
		try {
			Collection<Coupon> coupons = customerDBDAO.getCustomerCoupons(customer.getId());
			for (Coupon c : coupons) {
				couponDBDAO.removeCustomerCoupon(c.getId());
			}
			customerDBDAO.removeCustomer(customer);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to delete customer. Please check the cause for this.",e);
		}
	}

	/** update customer */

	public static void updateCustomer(Customer customer) throws FacadeExeptions {
		try {
			customerDBDAO.updateCustomer(customer);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to update customer. Please check the cause for this.",e);
		}
	}

	/** get customer by id */

	public static Customer getCustomer(long id) throws FacadeExeptions {
		try {
			return customerDBDAO.getCustomer(id);
		} catch (DBDAOExeption e) {
			throw new FacadeExeptions("Error encountered while attempting to retrieve customer. Please check the cause for this.", e);
		}
	}

	/** get all customers */

	public static Collection<Customer> getAllCustomers() throws FacadeExeptions {
		try {
			return customerDBDAO.getAllCustomers();
		} catch (Exception e) {
			throw new FacadeExeptions("Error encountered while attempting to retrieve all customers. Please check the cause for this.", e);
		}
	}
	
	/** get all coupons */

	public static Collection<Coupon> getAllCoupons() throws FacadeExeptions {
		try {
			return couponDBDAO.getAllCoupons();
		} catch (Exception e) {
			throw new FacadeExeptions("Error encountered while attempting to retrieve all coupons. Please check the cause for this.", e);
		}
	}

	/** login of facade */

	@Override
	public CouponClientFacade login(String name, String password) throws FacadeExeptions {
		if (name.equalsIgnoreCase("admin") && password.equals("1234"))
			return new AdminFacade();
		else
			throw new FacadeExeptions("Login FAILED for admin", null);
	}

	@Override
	public String toString() {
		return "admin";
	}
}
