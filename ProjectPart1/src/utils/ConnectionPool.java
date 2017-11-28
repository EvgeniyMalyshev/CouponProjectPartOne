package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import exeptions.CouponSystemExeption;


public class ConnectionPool {

	private static ConnectionPool instanse;
	public static final int MAX_CONNECTION=5;
	
	private ConcurrentLinkedQueue<Connection> idleConnections = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Connection> allConnections = new ConcurrentLinkedQueue<>();

	/** JDBC URL, username and password of MySQL server*/
	
	private static final String url = "jdbc:mysql://localhost:3306/Coupon_db?useSSL=false";
	private static final String user = "root";
	private static final String password = "1234";

	public static synchronized ConnectionPool getInstanse() throws CouponSystemExeption{
		if(instanse == null){
			instanse = new ConnectionPool();
		}
		return instanse;
	}
	
	private ConnectionPool() throws CouponSystemExeption {
		try {
			/**connection*/
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found - Couldn't connect to the pool");
			e.printStackTrace();
		}
		/** opening database connection to MySQL server*/
				for(int i =0; i<MAX_CONNECTION; i++) {
			try {
				Connection con = DriverManager.getConnection(url, user, password);
				allConnections.add(con);
				idleConnections.add(con);
				System.out.println("Contact");
			} catch (SQLException e) {
				throw new CouponSystemExeption ("Unavailable connection - Couldn't connect to the pool", e);	
			}
		}
	}
	
	/**get connection*/
	
	public Connection getConnection() {
		Connection conn = idleConnections.poll();
		if (conn == null)
			System.out.println("No idle connections found.");
		return conn;
	}
	
	/**return this connection*/
	
	public void returnConnection(Connection returnCon) {
		idleConnections.add(returnCon);
	}
	
	/**shutdown all systems*/
	
	public void shutdown() throws CouponSystemExeption{
		for(Connection connection : idleConnections){
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponSystemExeption ("Unavailable return all connections", e);	
			}
		}
		idleConnections.clear();
		allConnections.clear();
	}

}
