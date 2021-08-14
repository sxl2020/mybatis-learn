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
import java.sql.JDBCType;
import java.util.HashMap;
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
	public void test01() throws IOException {
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

			Map<String,Object> params = new HashMap<>();
			params.put("tableName","tbl_employee");
			params.put("id",1);
			params.put("lastName","sqc");
			Employee employee2 = mapper.getEmpByMap(params);
			System.out.println(employee2);
		} finally {
			openSession.close();
		}
	}
	
	public void testJdbcType(){
		JDBCType jdbcType = null;
		System.out.println(jdbcType.getName());
	}
}
