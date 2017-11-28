package dao;

/** DAO Company Layer */

import java.util.Collection;
import exeptions.DAOExeption;
import javabeans.Company;

public interface CompanyDAO {


	public void createCompany(Company company) throws DAOExeption;

	public void removeCompany(Company company) throws DAOExeption;

	public void updateCompany(Company company) throws DAOExeption;

	public Company getCompany(long id) throws DAOExeption;

	public Collection<Company> getAllCompanies() throws DAOExeption;

	public void getCompanyCoupon(long companyId, long couponId) throws DAOExeption;

	public void getAllCompanyCoupon(long companyId) throws DAOExeption;

	public Company login(String compName, String password) throws DAOExeption;


}


