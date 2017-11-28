package dbdao;

import java.sql.Connection;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


import dao.CustomerDAO;
import exeptions.CouponSystemExeption;
import exeptions.DBDAOExeption;
import javabeans.Coupon;
import javabeans.Customer;
import utils.ConnectionPool;

public class CustomerDBDAO implements CustomerDAO {
	private Connection connection;
	private CouponDBDAO couponDBDAO = new CouponDBDAO();

	/** Create customer and add editions to SQL */

	@Override
	public void createCustomer(Customer customer) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String createSQL = "INSERT INTO customer (CUST_NAME,PASSWORD) VALUES (?,?)";
			PreparedStatement pStatement = connection.prepareStatement(createSQL);
			pStatement.setString(1, customer.getName());
			pStatement.setString(2, customer.getPassword());
			pStatement.executeUpdate();
			System.out.println("Customer was created!");
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to create a new Customer.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);			}
		}
	}

	/** Remove customer and add editions to SQL */

	@Override
	public void removeCustomer(Customer customer) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			getAllCustomerCoupon(customer.getId());

			String removeSQL = "DELETE FROM customer WHERE ID=?";
			PreparedStatement pStatement1 = connection.prepareStatement(removeSQL);
			pStatement1.setLong(1, customer.getId());
			pStatement1.executeUpdate();
			String removeSQL1 = "DELETE FROM coupon_customer WHERE customer_ID=?";
			PreparedStatement pStatement2 = connection.prepareStatement(removeSQL1);
			pStatement2.setLong(1, customer.getId());
			pStatement2.executeUpdate();
			System.out.println("Customer " + customer + " was deleted!");
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to remove Customer from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}

	}

	/** Update customer and add editions to SQL */

	@Override
	public void updateCustomer(Customer customer) throws DBDAOExeption {
		try {
			try {
				ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String updateSQL = "UPDATE customer SET  PASSWORD=? WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(updateSQL);
			pStatement.setString(1, customer.getPassword());
			pStatement.setLong(2, customer.getId());
			pStatement.execute();
			System.out.println("Customer " + customer.getName() + " was updated!");

		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to update Customer.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Get customer by id and add editions to SQL */

	@Override
	public Customer getCustomer(long id) throws DBDAOExeption {
		Customer customer = new Customer();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String getSQL = "SELECT * FROM customer WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(getSQL);
			pStatement.setLong(1, id);
			ResultSet result = pStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					customer.setName(result.getString("CUST_NAME"));
					customer.setPassword(result.getString("PASSWORD"));
					customer.setId(result.getLong("ID"));
				}
				System.out.println("Customer " + customer.getName() + " was retrieved!");
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve Customer.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);			}
		}
		return customer;
	}

	/** Get all customers and add editions to SQL */

	@Override
	public Collection<Customer> getAllCustomers() throws DBDAOExeption {
		Collection<Customer> customers = new ArrayList<>();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String getAllSQL = "SELECT * FROM customer";
			PreparedStatement pStatement = connection.prepareStatement(getAllSQL);
			ResultSet result = pStatement.executeQuery();
			if(result != null){
				while (result.next()) {
					Customer customer = new Customer();
					customer.setId(result.getLong(1));
					customer.setName(result.getString(2));
					customer.setPassword(result.getString(3));
					customers.add(customer);
				}
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve all Customers in the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return customers;
	}

	/** Get customer coupons and add editions to SQL */

	@Override
	public Collection<Coupon> getCustomerCoupons(long custId) throws DBDAOExeption {
		Collection<Coupon> CustCoupons = new ArrayList<>();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String getCouponsSQL = "SELECT coupon_ID FROM coupon_customer WHERE customer_ID = ?";
			PreparedStatement pStatement = connection.prepareStatement(getCouponsSQL);
			pStatement.setLong(1, custId);
			ResultSet result = pStatement.executeQuery();
			if(result != null){
				while (result.next()) {
					Coupon coupon;
					try {
						coupon = couponDBDAO.getCoupon(result.getLong(1));
					} catch (SQLException e) {
						throw new DBDAOExeption("Problems with getCustomerCoupons detected!",e);
					}
					if(coupon.getId()!=0)
						CustCoupons.add(coupon);
				}
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve Customer's Coupons.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return CustCoupons;
	}

	/** Get customer coupon by id and add editions to SQL */

	@Override
	public void getCustomerCoupon(long customerId, long couponId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String linkSQL = "INSERT INTO customer_coupon (ID_CUSTOMER , ID_COUPON)"
					+ " values (?,?)";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, customerId);
			pStatement.setLong(2, couponId);
			pStatement.executeUpdate();
			System.out.println("Customer " + customerId + " was linked with coupon " + couponId);
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to link Customer and Coupon.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Get all customers coupons by id and add editions to SQL */

	@Override
	public void getAllCustomerCoupon (long customerId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String linkSQL = "DELETE FROM coupon_customer WHERE customer_ID=?";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, customerId);
			pStatement.executeUpdate();
			System.out.println("Customer " + customerId + " was unlinked from all coupons");
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to unlink Customer from its Coupons.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Login customer and add editions to SQL */

	@Override
	public Customer login(String custName, String password) throws DBDAOExeption {
		String dbPassword = null;
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String loginSQL = "SELECT * FROM customer WHERE CUST_NAME=?";
			PreparedStatement pStatement = connection.prepareStatement(loginSQL);
			pStatement.setString(1, custName);
			ResultSet result = pStatement.executeQuery();

			while (result.next()) {
				dbPassword = result.getString("PASSWORD");
				if (password.equals(dbPassword)) {
					Customer res = new Customer();
					res.setId(result.getLong("ID"));
					res.setName(result.getString("CUST_NAME"));
					res.setPassword(result.getString("PASSWORD"));
					return res;
				}
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Login FAILED !",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return null;
	}


}
