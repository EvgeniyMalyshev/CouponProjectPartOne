package dbdao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


import dao.CouponDAO;
import enums.CouponType;
import exeptions.CouponSystemExeption;
import exeptions.DBDAOExeption;
import javabeans.Company;
import javabeans.Coupon;
import utils.ConnectionPool;

public class CouponDBDAO implements CouponDAO {
	private Connection connection;

	/** Create coupon and add editions to SQL */

	@Override
	public void createCoupon(Coupon coupon, long companyId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String createSQL = "INSERT INTO coupon (TITLE, START_DATE, END_DATE, AMOUNT, COUPON_TYPE, MESSAGE, PRICE, IMAGE) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement pStatement = connection.prepareStatement (createSQL, Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, coupon.getTitle());
			pStatement.setDate(2, new java.sql.Date(coupon.getStartDate().getTime()));
			pStatement.setDate(3, new java.sql.Date(coupon.getEndDate().getTime()));
			pStatement.setInt(4, coupon.getAmount());
			pStatement.setString(5, coupon.getType().toString());
			pStatement.setString(6, coupon.getMessage());
			pStatement.setDouble(7, coupon.getPrice());
			pStatement.setString(8, coupon.getImage());
			int affectedRows = pStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Failed to create Coupon.");
			}
			ResultSet generatedKeys = pStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				coupon.setId(generatedKeys.getLong(1));
			} else {
				throw new SQLException("Create Coupon FAILED, No ID Was Obtained.");
			}
			
			String createSQL2 = "INSERT INTO coupon_company (coupon_ID,company_ID) VALUES (?,?)";
			PreparedStatement pStatement2 = connection.prepareStatement (createSQL2, Statement.RETURN_GENERATED_KEYS);
			pStatement2.setLong(1, coupon.getId());
			pStatement2.setLong(2, companyId);
			pStatement2.executeUpdate();

			System.out.println("Coupon " + coupon.getTitle() + " was created!");
		} catch (SQLException e) {
			throw new DBDAOExeption("Creating a new Coupon has failed!",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Remove coupon and add editions to SQL */

	@Override
	public void removeCoupon(Coupon coupon) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String removeSQL = "DELETE FROM coupon_customer WHERE coupon_ID=?";
			PreparedStatement pStatement3 = connection.prepareStatement(removeSQL);
			pStatement3.setLong(1, coupon.getId());
			pStatement3.executeUpdate();
			System.out.println("Coupon" + coupon.getTitle() + "was removed from the database.");

			String removeSQL2 = "DELETE FROM coupon_company WHERE coupon_ID=?";
			PreparedStatement pStatement4 = connection.prepareStatement(removeSQL2);
			pStatement4.setLong(1, coupon.getId());
			pStatement4.executeUpdate();
			System.out.println("Coupon" + coupon.getTitle() + "was removed from the database.");

			String removeSQL3 = "DELETE FROM coupon WHERE ID=?";
			PreparedStatement pStatement5 = connection.prepareStatement(removeSQL3);
			pStatement5.setLong(1, coupon.getId());
			pStatement5.executeUpdate();
			System.out.println("Coupon" + coupon.getTitle() + "was removed from the database.");

		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to remove Coupon from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Update coupon and add editions to SQL */

	@Override
	public void updateCoupon(Coupon coupon) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String updateSQL = "UPDATE coupon SET END_DATE=?, PRICE=? WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(updateSQL);
			pStatement.setDate(1, new java.sql.Date(coupon.getEndDate().getTime()));
			pStatement.setDouble(2, coupon.getPrice());
			int affectedRows = pStatement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Failed to update Coupon.");
			}
			System.out.println("Coupon " + coupon.getTitle() + " was updated!");
		} catch (SQLException e) {
			throw new DBDAOExeption("Update Coupon FAILED",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Get coupon by id and add editions to SQL */

	@Override
	public Coupon getCoupon(long id) throws DBDAOExeption {
		Coupon coupon = new Coupon();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String getSQL = "SELECT * FROM coupon WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(getSQL);
			pStatement.setLong(1, id);
			ResultSet result = pStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					coupon.setTitle(result.getString("TITLE"));
					coupon.setStartDate(result.getDate("START_DATE"));
					coupon.setEndDate(result.getDate("END_DATE"));
					coupon.setAmount(result.getInt("AMOUNT"));
					coupon.setType(CouponType.valueOf(result.getString("COUPON_TYPE")));
					coupon.setMessage(result.getString("MESSAGE"));
					coupon.setPrice(result.getInt("PRICE"));
					coupon.setImage(result.getString("IMAGE"));
					coupon.setId(id);				
				}
			}
			else{
				throw new DBDAOExeption("Coupon was not found in the database.", null);
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve Coupon from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return coupon;
	}

	/** Get all coupons and add editions to SQL */

	@Override
	public Collection<Coupon> getAllCoupons() throws DBDAOExeption {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String getAllSQL = "SELECT ID FROM coupon";
			PreparedStatement pStatement = connection.prepareStatement(getAllSQL);
			ResultSet result = pStatement.executeQuery();

			if (result != null) {
				while (result.next()) {
					Coupon coupon = getCoupon(result.getLong("ID"));
					coupons.add(coupon);
				}
				System.out.println("All coupons retrieved from the database!");
			}
			else{
				System.out.println("there is no coupons in database!");
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve all coupons from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
		}
		return coupons;
	}

	/** Get coupon's of company and add editions to SQL */

	@Override
	public Collection<Coupon> getCompanyCoupons(long companyId) throws DBDAOExeption {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String getCompanyCouponsSQL = "SELECT coupon_ID FROM coupon_company WHERE company_ID = ?";
			PreparedStatement pStatement = connection.prepareStatement(getCompanyCouponsSQL);
			pStatement.setLong(1, companyId);
			ResultSet result = pStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					Coupon coupon = getCoupon(result.getLong(1));
					coupons.add(coupon);
				}
				System.out.println("Company ID-" + companyId + " : all coupons retrieved!");
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Problems with getCompanyCoupons detected!", e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return coupons;
	}

	/** Get coupon by type and add editions to SQL */

	@Override
	public Collection<Coupon> getCouponByType(CouponType couponType) throws DBDAOExeption {
		Collection<Coupon> coupons = new ArrayList<>();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String getTypeSQL = "SELECT ID FROM coupon WHERE COUPON_TYPE=?";
			PreparedStatement pStatement = connection.prepareStatement(getTypeSQL);
			pStatement.setString(1, couponType.toString());
			ResultSet result = pStatement.executeQuery();
			int resultCount = 0;
			if (result != null) {
				while (result.next()) {
					Coupon coupon = getCoupon(result.getLong("ID"));
					if (coupon.getId() != 0) {
						coupons.add(this.getCoupon(coupon.getId()));
						resultCount++;
					}
				}
				System.out.println("--- End of " + couponType.toString() + " coupon list");
			}
			else{
				throw new DBDAOExeption("Error occured while trying to retrieve the coupons.", null);
			}
			if (resultCount==0) {
				System.out.println("Coupon of the type " + couponType.toString()
				+ " was not found");
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to retrieve Coupons by type.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return coupons;
	}

	/** Remove coupon of company and add editions to SQL */

	@Override
	public void removeCompanyCoupon(long couponId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);			}
			String linkSQL = "DELETE FROM coupon_company WHERE coupon_ID=?";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, couponId);
			pStatement.executeUpdate();
			System.out.println("Coupon " + couponId + " was deleted and unlinked "
					+ "from all companies");
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to remove Coupon from all Companies.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
		}
	}

	/** Remove customer coupon and add editions to SQL */

	@Override
	public void removeCustomerCoupon(long couponId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);			}
			String linkSQL = "DELETE FROM coupon_customer WHERE coupon_ID=?";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, couponId);
			pStatement.executeUpdate();
			System.out.println("Coupon " + couponId + " was deleted and unlinked "
					+ "from all customers");
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to remove Coupon from all Customers.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
		}
	}
}
