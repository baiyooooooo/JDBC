package sample.easy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SampleEasy {

	/*--- JDBC --- */

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/JDBCTEST";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "930717bai";

	public static void batchStatement(Connection conn) {
		try {
			Statement state = conn.createStatement();
			conn.setAutoCommit(false);
			String sql1 = "";
			String sql2 = "";
			state.addBatch(sql1);
			state.addBatch(sql2);
			state.executeBatch(); // return an array, 每个语句的更新计数
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static void displayResult(ResultSet rs) {
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				int age = rs.getInt("age");
				String first = rs.getString("first");
				String last = rs.getString("last");

				System.out.print("ID: " + id);
				System.out.print(" age: " + age);
				System.out.print(" first: " + first);
				System.out.println(" last: " + last);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void statement(Connection conn) {
		System.out.println("--- Statement ---");
		Statement state = null;
		try {
			System.out.println("Creating statement");
			// 在创建statement的时候，隐含的定义了结果集（resultSet）的参数
			// 详细的讲解 见 Note.md
			state = conn.createStatement();
			// 上一句等同于下一句 因为上一句省略了默认result set的参数
			// Statement stmt =
			// conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT id, first, last, age FROM Employees";

			System.out.println("Able to retrieve data: " + state.execute(sql));

			// Cannot execute 'SELECT' via executeUpdate or executeLargeUpdate
			// System.out.println("SQL influence " + state.executeUpdate(sql) +
			// " lines");

			ResultSet rs = state.executeQuery(sql);

			displayResult(rs);

			System.out.println("Closing results & state");
			rs.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void preparedStatement(Connection conn) {
		System.out.println("--- Prepared Statement ---");
		PreparedStatement pState = null;
		// ? means parameters, Need to be specified before execution
		String sql = "UPDATE Employees SET age = ? WHERE id = ?";
		try {
			pState = conn.prepareStatement(sql);
			pState.setInt(1, 17);
			pState.setInt(2, 100);
			System.out.println("Update " + pState.executeUpdate() + " lines successfully");

			sql = "SELECT id, first, last, age from Employees";
			ResultSet rs = pState.executeQuery(sql);

			displayResult(rs);

			System.out.println("Closing results & state");
			rs.close();
			pState.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pState.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("resource")
	public static void stream(Connection conn) {
		PreparedStatement pState = null;
		ResultSet rs = null;

		try {
			File file = new File("xml_data.xml");
			// long fileLength = file.length();
			FileInputStream fileInputStream = new FileInputStream(file);

			String sql = "INSERT INTO XML_DATA VALUES (?, ?)";
			pState = conn.prepareStatement(sql);
			pState.setInt(1, 125);
			pState.setAsciiStream(2, fileInputStream, (int) file.length());
			pState.execute();

			fileInputStream.close();

			sql = "SELECT DATA FROM XML_DATA WHERE ID = 125";
			pState = conn.prepareStatement(sql);
			rs = pState.executeQuery();

			while (rs.next()) {
				InputStream inputStream = rs.getAsciiStream(1);
				int c;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((c = inputStream.read()) != -1) {
					bos.write(c);
				}
				System.out.println(bos.toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			try {
				if (pState != null) {
					pState.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void jdbc() {
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

			// Set auto commit, default is true
			// conn.setAutoCommit(true);

			// commit & rollback
			// conn.commit();
			// conn.rollback();
			// rollback can rollback to a save point
			// Savepoint savepoint = conn.setSavepoint(name);
			// conn.releaseSavepoint(savepoint);

			// Properties info = new Properties( );
			// info.put( "user", "root" );
			// info.put( "password", "password12321" );
			// DriverManager.getConnection(url, info)

			// Statement
			statement(conn);

			// PreparedStatement
			preparedStatement(conn);

			// Stream
			stream(conn);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					System.out.println("Connection is closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void mappingFile() {

		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");

		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();

		Employee e1 = new Employee();
		e1.setId(111);
		e1.setAge(30);
		e1.setFirst("Max");
		e1.setLast("Su");

		session.persist(e1);

		transaction.commit();
		session.close();

		System.out.println("Saving data successfully");
	}

	/*--- Hibernate --- */

	public static void annotation() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		EmployeeAnnotation ea = new EmployeeAnnotation();
		ea.setId(112);
		ea.setAge(15);
		ea.setLast("Bai");
		ea.setFirst("Yubo");
		
		EmployeeAnnotation ea1 = new EmployeeAnnotation();
		ea1.setId(113);
		ea1.setAge(15);
		ea1.setLast("Yang");
		ea1.setFirst("Mike");
		
		session.persist(ea);
		session.persist(ea1);
		
		transaction.commit();
		session.close();
		System.out.println("Saving successfully");
	}

	public static void hibernate() {
//		mappingFile();
//		annotation();
	}

	public static void main(String[] args) {
//		 jdbc();
		hibernate();
	}
}
