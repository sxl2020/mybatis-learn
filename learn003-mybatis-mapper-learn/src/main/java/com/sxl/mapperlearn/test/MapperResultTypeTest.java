package com.sxl.mapperlearn.test;

import com.sxl.mapperlearn.bean.Employee;
import com.sxl.mapperlearn.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MapperResultTypeTest {
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * @description 演示Mybatis原理，采用的是动态代理，为mapper接口生成一个代理对象
	 */
	@Test
	public void pojoResultTypeTest() throws IOException {
		// 1、获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee1 = mapper.getEmpByIdAndLastName(1,"sqc");
			System.out.println(employee1);
		} finally {
			openSession.close();
		}
	}

	/**
	 * @description 返回List对象
	 */
	@Test
	public void pojoListResultTypeTest() throws IOException {
		// 1、获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			List<Employee> employeeList = mapper.getEmpsList();
			for(Employee e:employeeList){
				System.out.println(e);
			}
		} finally {
			openSession.close();
		}
	}

	/**
	 * @description 返回List对象
	 */
	@Test
	public void mapResultTypeTest() throws IOException {
		// 1、获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession(true);
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String,Employee> employeeMap1 = mapper.getEmpByLastNameLikeReturnMap("tom");
			System.out.println(employeeMap1);

			Map<String,Object> employeeMap2 = mapper.getEmpByIdReturnMap(1);
			System.out.println(employeeMap2);

			List<Map<String,Object>> employeeMap3 = mapper.getAllEmpByReturnMapList();
			System.out.println(employeeMap3);

		} finally {
			openSession.close();
		}
	}
}
