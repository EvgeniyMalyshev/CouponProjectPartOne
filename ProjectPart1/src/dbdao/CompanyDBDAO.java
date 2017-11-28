package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.ResultSet;
import java.util.ArrayList;
import dao.CompanyDAO;
import exeptions.CouponSystemExeption;
import exeptions.DBDAOExeption;
import javabeans.Company;
import utils.ConnectionPool;

/** Connection with SQL */

public class CompanyDBDAO implements CompanyDAO {
	private Connection connection;

	/** Create company and add editions to SQL */

	@Override
	public void createCompany(Company company) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch ( CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String createSQL = "INSERT INTO company(COMP_NAME, PASSWORD, EMAIL) VALUES (?,?,?)";
			PreparedStatement pStatement = connection.prepareStatement(createSQL);
			pStatement.setString(1, company.getName());
			pStatement.setString(2, company.getPassword());
			pStatement.setString(3, company.getEmail());
			pStatement.executeUpdate();
			System.out.println("a new Company was created in the database.");
		} catch (SQLException e) {
			throw new DBDAOExeption("Error encountered while attempting to create a new company.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
		}
	}

	/** Remove company and add editions to SQL */

	@Override
	public void removeCompany(Company company) throws DBDAOExeption{
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			getAllCompanyCoupon(company.getId());

			String removeSQL1 = "DELETE FROM company WHERE ID=?";
			PreparedStatement pStatement1 = connection.prepareStatement(removeSQL1);
			pStatement1.setLong(1, company.getId());
			pStatement1.executeUpdate();
			String removeSQL2 = "DELETE FROM coupon_company WHERE company_ID=?";
			PreparedStatement pStatement2 = connection.prepareStatement(removeSQL2);
			pStatement2.setLong(1, company.getId());
			pStatement2.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBDAOExeption("Error encountered while attempting to remove company from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption  e) {
				throw new DBDAOExeption("Problems with return of connection detected!",e);

			}
		}
	}

	/** Update company and add editions to SQL */

	@Override
	public void updateCompany(Company company) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String updateSQL = "UPDATE company SET PASSWORD=?, EMAIL=? WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(updateSQL);
			pStatement.setString(1, company.getPassword());
			pStatement.setString(2, company.getEmail());
			pStatement.setLong(3, company.getId());
			pStatement.execute();

			System.out.println("Company " + company.getName() + " was updated!");
		} catch (SQLException e) {
			throw new DBDAOExeption("Error encountered while attempting to update company.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				System.out.println("Problems with return of connection detected!");
				e.printStackTrace();
			}
		}
	}

	/** Get company by id */

	@Override
	public Company getCompany(long id) throws DBDAOExeption {
		Company company = new Company();
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String getSQL = "SELECT * FROM COUPON_DB.COMPANY WHERE ID=?";
			PreparedStatement pStatement = connection.prepareStatement(getSQL);
			pStatement.setLong(1, id);
			ResultSet result = pStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					company.setName(result.getString("COMP_NAME"));
					company.setPassword(result.getString("PASSWORD"));
					company.setEmail(result.getString("EMAIL"));
					company.setId(result.getLong("ID"));
				}
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Error encountered while attempting to retrieve company from the database.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				System.out.println("Problems with return of connection detected!");
				e.printStackTrace();
			}
		}
		return company;
	}

	/** Get all company's */

	@Override
	public Collection<Company> getAllCompanies() throws DBDAOExeption {
		Collection<Company> companies = new ArrayList<>();

		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}
			String getAllSQL = "SELECT * FROM company";
			PreparedStatement pStatement = connection.prepareStatement(getAllSQL);
			ResultSet result = pStatement.executeQuery();

			while (result.next()) {
				Company comp = new Company();
				comp.setId(result.getLong(1));
				comp.setName(result.getString(2));
				comp.setPassword(result.getString(3));
				comp.setEmail(result.getString(4));
				companies.add(comp);
			}
		} catch (SQLException e) {
			throw new DBDAOExeption("Error encountered while attempting to retrieve all companies from the database.", e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
		return companies;
	}

	/** Get company's coupon and add editions to SQL */

	@Override
	public void getCompanyCoupon(long companyId, long couponId) throws DBDAOExeption {
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);

			}

			String linkSQL = "INSERT INTO COUPON_DB.COMPANY_COUPON VALUES(?,?)";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, companyId);
			pStatement.setLong(2, couponId);
			pStatement.executeUpdate();
			System.out.println("Company " + companyId + " was linked with coupon " + couponId);
		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to link Company with the new coupon.",e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Get company's coupons and add editions to SQL */

	@Override
	public void getAllCompanyCoupon(long companyId) throws DBDAOExeption {
		try { 
			try{connection = ConnectionPool.getInstanse().getConnection();
			}
			catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String linkSQL = "DELETE FROM coupon_company WHERE company_ID=?";
			PreparedStatement pStatement = connection.prepareStatement(linkSQL);
			pStatement.setLong(1, companyId);
			pStatement.executeUpdate();
			System.out.println("Company " + companyId + " was unlinked from its coupons");

		} catch (SQLException e) {
			throw new DBDAOExeption("Failed to unlink Company from its Coupons", e);
		} finally {
			try {
				ConnectionPool.getInstanse().returnConnection(connection);
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with return connection detected!",e);
			}
		}
	}

	/** Login company and add editions to SQL */

	@Override
	public Company login(String compName, String password) throws DBDAOExeption {
		String dbPassword = null;
		try {
			try {
				connection = ConnectionPool.getInstanse().getConnection();
			} catch (CouponSystemExeption e) {
				throw new DBDAOExeption("Problems with connection detected!",e);
			}

			String loginSQL = "SELECT * FROM COUPON_DB.COMPANY WHERE COMP_NAME=?";
			PreparedStatement pStatement = connection.prepareStatement(loginSQL);
			pStatement.setString(1, compName);
			ResultSet result = pStatement.executeQuery();

			while (result.next()) {
				dbPassword = result.getString("PASSWORD");
				if (password.equals(dbPassword)) {
					Company res = new Company();
					res.setId(result.getLong("ID"));
					res.setName(result.getString("COMP_NAME"));
					res.setPassword(result.getString("PASSWORD"));
					res.setEmail(result.getString("EMAIL"));
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
