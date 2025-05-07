package com.shinhan.day15;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CRUDTest2 {
	
	public static void main(String[] args) throws SQLException {
		// 모두 성공하면 commit, 하나라도 실패하면 rollback
		//insert...
		//update...
		Connection conn = null;
		Statement st1 = null;
		Statement st2 = null;
		String sql1 = """
				INSERT INTO EMPL(EMPLOYEE_ID, FIRST_NAME, LAST_NAME, HIRE_dATE, JOB_ID,EMAIL)
				        VALUES(4,'AA','BB',SYSDATE,'IT','ABCDEF')
				""";
		String sql2 = """
				UPDATE EMPL SET SALARY = 1000 WHERE EMPLOYEE_ID = 198
				""";
		conn = DBUtill.getConnection();
		conn.setAutoCommit(false);
		st1 = conn.createStatement();
		int result1 = st1.executeUpdate(sql1);
		st2 = conn.createStatement();
		int result2 = st2.executeUpdate(sql2);
		
		if(result1>= 1 && result2>=1) {
			conn.commit();
		}else {
			conn.rollback();
		}
		
		System.out.println(result1);
		System.out.println(result2);
	}
	
	public static void f_4() throws SQLException {
		Connection conn = null;
		Statement st = null;
		int result = 0;
		String sql = """
				DELETE FROM empl
				WHERE EMPLOYEE_ID = 99
								""";
		conn = DBUtill.getConnection();
		st = conn.createStatement();
		result = st.executeUpdate(sql); // 자동으로 commit;
		System.out.println(result >= 1 ? "DELETE success" : "DELETE fail");
	}

	public static void f_3(String[] args) throws SQLException {
		Connection conn = null;
		Statement st = null;
		int result = 0;
		String sql = """
				UPDATE empl
				SET DEPARTMENT_ID = (SELECT DEPARTMENT_ID
				                    FROM EMPLOYEES
				                    WHERE EMPLOYEE_ID = 102),
				    SALARY = (SELECT SALARY
				            FROM EMPLOYEES
				            WHERE EMPLOYEE_ID = 103)
				WHERE EMPLOYEE_ID = 1
								""";
		conn = DBUtill.getConnection();
		st = conn.createStatement();
		result = st.executeUpdate(sql); // 자동으로 commit;
		System.out.println(result >= 1 ? "Update success" : "Update fail");
	}

	public static void f_2() throws SQLException {
		Connection conn = null;
		Statement st = null;
		int result = 0;
		String sql = """
				INSERT INTO EMP1 VALUES(7,'채','승','dbcotmd0@gmail.com','폰',SYSDATE,'JOB',100,0.2,100,'20')
				""";
		conn = DBUtill.getConnection();
		st = conn.createStatement();
		result = st.executeUpdate(sql);
		System.out.println(result >= 1 ? "insert success" : "insert fail");

	}

	public static void f_1() throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = """
				SELECT ENAME, SAL ,MGR
				FROM EMP
				WHERE MGR = (
				        SELECT EMPNO
				        FROM EMP
				        WHERE ENAME = 'KING')
				""";

		conn = DBUtill.getConnection();
		st = conn.createStatement();
		rs = st.executeQuery(sql);
		while (rs.next()) {
			String a = rs.getString(1);
			int b = rs.getInt(2);
			int c = rs.getInt(3);
			System.out.println(a + "\t" + b + "\t" + c);
		}

		DBUtill.dbDisconnect(conn, st, rs);

	}

}
