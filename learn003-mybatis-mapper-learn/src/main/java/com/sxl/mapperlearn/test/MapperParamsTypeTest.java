package com.sxl.mapperlearn.test;

import com.sxl.mapperlearn.bean.Employee;
import com.sxl.mapperlearn.dao.EmployeeMapper;
import com.sxl.mapperlearn.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapperParamsTypeTest {
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * @description 入参demo1
	 * @date 2021/8/10 下午7:06
	 */
	@Test
	public void singleParamsTest() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
		}finally{
			openSession.close();
		}
	}

	/**
	 * @description 入参demo2
	 * @date 2021/8/14 下午7:15
	 */
	@Test
	public void multiParamsTest() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String, Object> map = new HashMap<>();
			map.put("id", 1);
			map.put("lastName", "tom");
			map.put("tableName", "tbl_employee");
			Employee employee1 = mapper.getEmpByMap(map);
			System.out.println(employee1);
		}finally{
			openSession.close();
		}
	}

	/**
	 * @description 入参demo3
	 * @date 2021/8/14 下午7:15
	 */
	@Test
	public void WithParamsTest() throws IOException{

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession(true);

		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee1 = mapper.getEmpByIdAndLastName(1, "tom");
			System.out.println(employee1);
		}finally{
			openSession.close();
		}
	}


	/**
	 * @description 入参demo4
	 * 使用Pojo 传参
	 * @date 2021/8/14 下午7:33
	 */

	@Test
	public void pojoParamsTest() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee myEmp = new Employee();
			myEmp.setId("1");
			myEmp.setLastName("tom");
			myEmp.setGender("man");
			Employee employee = mapper.getEmpByPojo(myEmp);
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}
	
}
