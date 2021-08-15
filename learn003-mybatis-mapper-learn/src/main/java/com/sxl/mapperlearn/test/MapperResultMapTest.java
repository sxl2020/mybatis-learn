package com.sxl.mapperlearn.test;

import com.sxl.mapperlearn.bean.Department;
import com.sxl.mapperlearn.bean.Employee;
import com.sxl.mapperlearn.dao.DepartmentMapper;
import com.sxl.mapperlearn.dao.EmployeeMapper;
import com.sxl.mapperlearn.dao.EmployeeMapperAnnotation;
import com.sxl.mapperlearn.dao.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapperResultMapTest {
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

	@Test
	public void test02() throws IOException{
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
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 */
	@Test
	public void test03() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//测试添加
			Employee employee = new Employee(null, "jerry4","1234@qq.com", "jh");
			mapper.addEmp(employee);
			//2、手动提交数据
			openSession.commit();
			System.out.println(employee.getId());

		}finally{
			openSession.close();
		}
		
	}

	@Test
	public void test04() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//Employee employee = mapper.getEmpByIdAndLastName(1, "tom");
			Map<String, Object> map = new HashMap<>();
			map.put("id", 2);
			map.put("lastName", "Tom");
			map.put("tableName", "tbl_employee");
			Employee employee = mapper.getEmpByMap(map);
			openSession.commit();
			
			System.out.println(employee);
			
		}finally{
			openSession.close();
		}
	}
	
	@Test
	public void test05() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);

			Employee empAndDept = mapper.getEmpAndDept(1);
			System.out.println(empAndDept);
			System.out.println(empAndDept.getDept());

			Employee employee = mapper.getEmpByIdStep(1);
			System.out.println(employee);
			System.out.println(employee.getDept());
			System.out.println(employee.getDept());
		}finally{
			openSession.close();
		}
		
		
	}
	
	@Test
	public void test06() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
			Department department = mapper.getDeptByIdPlus(1);
			System.out.println(department);
			System.out.println(department.getEmps());
		}finally{
			openSession.close();
		}
	}	
	
}
