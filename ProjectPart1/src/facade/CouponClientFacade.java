package facade;

import exeptions.FacadeExeptions;

public interface CouponClientFacade {

	CouponClientFacade login(String name, String password) throws FacadeExeptions;

}
