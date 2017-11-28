package utils;


import exeptions.CouponSystemExeption;


/** cleaning for old coupons , starts one time in a day*/

public class DailyCleaningTask implements Runnable {

	private boolean active = true;

	public DailyCleaningTask() {
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void run()  {
		int seconds = 0;
		while (active) {
			try {
				Thread.sleep(1000);
				if (seconds >= (60*60*24)){
					DateTime.removeExpiredCoupon();
					seconds = 0;
				}
			} catch (InterruptedException | CouponSystemExeption e) {
				e.printStackTrace();
			}
			seconds++;
		}

	}

}
