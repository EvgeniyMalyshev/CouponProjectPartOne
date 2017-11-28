package utils;

import facade.CompanyFacade;
import facade.CustomerFacade;

import enums.ClientType;
import exeptions.CouponSystemExeption;
import facade.AdminFacade;
import facade.CouponClientFacade;

public class CouponSystem {

	private DailyCleaningTask dailyClean;
	private static CouponSystem instance = null;

	private CouponSystem() {
		dailyClean = new DailyCleaningTask();
		new Thread(dailyClean).start();

	}
	public DailyCleaningTask getDailyTask() {
		return dailyClean;
	}

	/** create of instance*/

	public static CouponSystem getInstance() {
		if (instance == null) {
			synchronized (CouponSystem.class) {
				if (instance == null) {
					instance = new CouponSystem();
					System.out.println("Get it!");
				}
			}
		}
		return instance;

	}

	/** login for all Facades*/

	public CouponClientFacade login(String name, String password,ClientType clientType) throws CouponSystemExeption {

		AdminFacade adminfacade = new AdminFacade();
		CompanyFacade companyfacade = new CompanyFacade();
		CustomerFacade customerfacade = new CustomerFacade();

		switch (clientType) {
		case ADMIN:
			adminfacade = (AdminFacade) adminfacade.login(name, password);
			return adminfacade;

		case COMPANY:
			companyfacade = (CompanyFacade) companyfacade.login(name, password);
			return companyfacade;

		case CUSTOMER:
			customerfacade = (CustomerFacade) customerfacade.login(name, password);
			return customerfacade;

		default:
			return null;

		}
	}

	/** close all functions */

	public void shutdown() throws CouponSystemExeption{
		if(dailyClean != null){
			dailyClean.setActive(false);
		}
		ConnectionPool.getInstanse().shutdown();
		System.out.println("Shutdown!");
	}
}
