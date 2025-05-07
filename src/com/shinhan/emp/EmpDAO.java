package com.shinhan.emp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.shinhan.day15.DBUtill;

//DAO(Data Access Object) : DB에 CRUD작업(select, Insert, update, delete)
//Statement는 SQL문을 보내는 통로 ...바인딩변수 지원하지않음 
//PreparedStatement : Statement상속받음, 바인딩변수 지원, sp호출 지원안함
//CallableStatement :sp호출 지원
public class EmpDAO {

	// Stored Procedure를 실행하기 (직원번호를 받아서 이메일과 급여를 return)
	public EmpDTO execute_sp(int empid) {
		EmpDTO emp = null;
		Connection conn = DBUtill.getConnection();
		CallableStatement st = null;
		String sql = "{call sp_empinfo2(?,?,?)}";

		try {
			st = conn.prepareCall(sql);
			st.setInt(1, empid);
			st.registerOutParameter(2, Types.VARCHAR);
			st.registerOutParameter(3, Types.DECIMAL);

			boolean result = st.execute();
			System.out.println(result);
			// true if the first result is a ResultSetobject;
			// false if the first result is an updatecount or there is no result

			emp = new EmpDTO();
			String email = st.getString(2);
			double salary = st.getDouble(3);
			emp.setEmail(email);
			emp.setSalary(salary);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return emp;
	}

	public int empUpdate(EmpDTO emp) {
		int result = 0;
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		Map<String, Object> dynamicSQL = new HashMap<String, Object>();

		/*
		 * FIRST_NAME aa LAST_NAME bb
		 * 
		 */

		if (emp.getFirst_name() != null)
			dynamicSQL.put("FIRST_NAME", emp.getFirst_name());
		if (emp.getLast_name() != null)
			dynamicSQL.put("LAST_NAME", emp.getLast_name());
		if (emp.getSalary() != null)
			dynamicSQL.put("SALARY", emp.getSalary());
		if (emp.getHire_date() != null)
			dynamicSQL.put("HIRE_DATE", emp.getHire_date());
		if (emp.getEmail() != null)
			dynamicSQL.put("EMAIL", emp.getEmail());
		if (emp.getPhone_number() != null)
			dynamicSQL.put("PHONE_NUMBER", emp.getPhone_number());
		if (emp.getJob_id() != null)
			dynamicSQL.put("JOB_ID", emp.getJob_id());
		if (emp.getCommission_pct() != null)
			dynamicSQL.put("Commission_pct", emp.getCommission_pct());
		if (emp.getManager_id() != null)
			dynamicSQL.put("manager_id", emp.getManager_id());
		if (emp.getDepartment_id() != null)
			dynamicSQL.put("department_id", emp.getDepartment_id());

		String sql = "update employees set ";
		String sql2 = " where EMPLOYEE_ID = ? ";
		for (String key : dynamicSQL.keySet()) {
			sql += key + "=?,";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += sql2;

		try {
			st = conn.prepareStatement(sql);
			int i = 1;
			for (String key : dynamicSQL.keySet()) {
				st.setObject(i++, dynamicSQL.get(key));
			}
			st.setObject(i, emp.getEmployee_id());
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 수정
	public int empUpdate2(EmpDTO emp) {
		int result = 0;
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;

		String sql = """
					update employees set
						FIRST_NAME=?,
						LAST_NAME=?,
						EMAIL=?,
						PHONE_NUMBER=?,
						HIRE_DATE=? ,
						JOB_ID=?,
						SALARY=?,
						COMMISSION_PCT=?,
						MANAGER_ID=?,
						DEPARTMENT_ID=?
				where EMPLOYEE_ID = ?
					""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(11, emp.getEmployee_id());
			st.setString(1, emp.getFirst_name());
			st.setString(2, emp.getLast_name());
			st.setString(3, emp.getEmail());
			st.setString(4, emp.getPhone_number());
			st.setDate(5, emp.getHire_date());
			st.setString(6, emp.getJob_id());
			st.setDouble(7, emp.getSalary());
			st.setDouble(8, emp.getCommission_pct());
			st.setInt(9, emp.getManager_id());
			st.setInt(10, emp.getDepartment_id());
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 입력
	public int empInsert(EmpDTO emp) {
		int result = 0;
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into employees(
				    EMPLOYEE_ID,
					FIRST_NAME,
					LAST_NAME,
					EMAIL,
					PHONE_NUMBER,
					HIRE_DATE ,
					JOB_ID,
					SALARY,
					COMMISSION_PCT,
					MANAGER_ID,
					DEPARTMENT_ID  )
				values( ?,?,?,?,?,?,?,?,?,?,? )
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, emp.getEmployee_id());
			st.setString(2, emp.getFirst_name());
			st.setString(3, emp.getLast_name());
			st.setString(4, emp.getEmail());
			st.setString(5, emp.getPhone_number());
			st.setDate(6, emp.getHire_date());
			st.setString(7, emp.getJob_id());
			st.setDouble(8, emp.getSalary());
			st.setDouble(9, emp.getCommission_pct());
			st.setInt(10, emp.getManager_id());
			st.setInt(11, emp.getDepartment_id());
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 삭제
	public int empDeleteById(int empid) {
		int result = 0;
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		String sql = "delete from employees where employee_id = ?";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, empid);
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<EmpDTO> selectByCondition(Integer[] arr, String jobid, int salary, String hdate) {
		List<EmpDTO> emplist = new ArrayList<>();
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String deptStr = Arrays.stream(arr).map(id -> "?").collect(Collectors.joining(",")); // "?,?,?"

		String sql = "select * from employees" + " where job_id like ? " + " and salary >= ?" + " and hire_date >= ?"
				+ " and department_id  in (" + deptStr + ")"; // ?,?,?

		try {
			st = conn.prepareStatement(sql); // SQL문을 준비한다.
			st.setString(1, "%" + jobid + "%"); // 1번째 ?에 값을 setting한다.
			st.setInt(2, salary);
			Date d = DateUtil.convertToSQLDate(DateUtil.convertToDate(hdate));
			st.setDate(3, d);

			int col = 4;
			for (int i = 0; i < arr.length; i++) {
				st.setInt(col++, arr[i]);
			}

			rs = st.executeQuery();
			while (rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emplist;
	}

	public List<EmpDTO> selectByJobAndDept(String job, int dept) {
		List<EmpDTO> emplist = new ArrayList<>();
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from employees where job_id = ? and department_id = ?";
		try {
			st = conn.prepareStatement(sql); // SQL문을 준비한다.
			st.setString(1, job); // 1번째 ?에 값을 setting한다.
			st.setInt(2, dept);
			rs = st.executeQuery();
			while (rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emplist;
	}

	// job으로 직원조회
	public List<EmpDTO> selectByJob(String job) {
		List<EmpDTO> emplist = new ArrayList<>();
		Connection conn = DBUtill.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from employees where job_id = ? ";
		try {
			st = conn.prepareStatement(sql); // SQL문을 준비한다.
			st.setString(1, job); // 1번째 ?에 값을 setting한다.
			rs = st.executeQuery();
			while (rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emplist;
	}

	// 부서의 직원조회
	public List<EmpDTO> selectByDept(int deptid) {
		List<EmpDTO> emplist = new ArrayList<>();
		Connection conn = DBUtill.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from employees where department_id = " + deptid;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emplist;
	}

	// 직원번호로 직원정보를 상세보기
	public EmpDTO selectById(int empid) {
		EmpDTO emp = null;
		Connection conn = DBUtill.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from employees where employee_id = " + empid;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				emp = makeEmp(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emp;
	}

	// 모든직원조회
	public List<EmpDTO> selectAll() {
		List<EmpDTO> emplist = new ArrayList<>();
		Connection conn = DBUtill.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from employees";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtill.dbDisconnect(conn, st, rs);
		}
		return emplist;
	}

	private EmpDTO makeEmp(ResultSet rs) throws SQLException {
		EmpDTO emp = EmpDTO.builder().commission_pct(rs.getDouble("Commission_pct"))
				.department_id(rs.getInt("department_id")).email(rs.getString("email"))
				.employee_id(rs.getInt("employee_id")).first_name(rs.getString("first_name"))
				.hire_date(rs.getDate("hire_date")).job_id(rs.getString("job_id")).last_name(rs.getString("last_name"))
				.manager_id(rs.getInt("manager_id")).phone_number(rs.getString("phone_number"))
				.salary(rs.getDouble("salary")).build();

		return emp;
	}

}
