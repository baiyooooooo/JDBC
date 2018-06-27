package sample.db.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// 利用反射，创建一个可以适用于所有表的 JDBC取值class
public class DBUtil {

	/*--- JDBC --- */

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/JDBCTEST";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "930717bai";

	@SuppressWarnings("finally")
	public Connection getConnection() {
		Connection conn = null;
		try {

			// register driver
			// approch 1:
			Class.forName(JDBC_DRIVER);

			// approch 2:
			// Driver driver = new com.mysql.cj.jdbc.Driver();
			// DriverManager.deregisterDriver(driver);

			System.out.println("Connecting database JDBCTEST");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return conn;
		}

	}

	@SuppressWarnings("finally")
	public ResultSet getResultSet(Connection conn) {
		System.out.println("--- Statement ---");
		Statement state = null;
		ResultSet rs = null;
		try {
			state = conn.createStatement();
			String sql = "SELECT id, first, last, age FROM Employees";

			rs = state.executeQuery(sql);
			
//			state.close();

			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return rs;
		}
	}

	/*
	 * @param rs JDBC select 之后的Result set
	 * 
	 * @param clazz DB table对应的java class类型
	 * 
	 * @return 填充了DB table的数据的java entity list
	 */
	public List<? extends EntityBase> getList(ResultSet rs, Class clazz) {
		ArrayList<EntityBase> list = new ArrayList<>();
		try {
			while (rs.next()) {
				// 新建object
				EntityBase entity = (EntityBase) clazz.newInstance();

				// 遍历class中的每个属性， 将其从rs中读出来放在新建的 entity里面
				for (Field field : clazz.getFields()) {
					// 拿到属性对饮的数据类型的SimpleName
					switch (field.getType().getSimpleName()) {
					case "String":
						// 给entity 的属性赋值
						field.set(entity, rs.getString(field.getName()));
						break;
					case "int":
						field.set(entity, rs.getInt(field.getName()));
						break;
					}
				}
				
				list.add(entity);
			}
			
//			rs.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
