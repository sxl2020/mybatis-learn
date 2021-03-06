package com.sxl.mapperlearn.dao;

import com.sxl.mapperlearn.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author suqiancheng
 * @date 2021/8/10 下午6:59
 */

public interface EmployeeMapper {

	/**
	 * ******************************************************************************************
	 * @description : 入参demo1: 入参为单个参数
	 * @description : resultType demo1: 返回结果为单个实体类
	 * ******************************************************************************************
	*/
 	Employee getEmpById(Integer id);

	/**
	 * ******************************************************************************************
	 * @description : 入参demo2
	 * 入参为多个参数
	 *
	 * ******************************************************************************************
	*/
	 Employee getEmpByMap(Map<String, Object> map);


	/**
	 * ******************************************************************************************
	 * @description : 入参demo3
	 * 使用@Params 该注解告诉Mybatis，使用哪一个名称来传递参数
	 *
	 * ******************************************************************************************
	 */
	 Employee getEmpByIdAndLastName(@Param("id")Integer id, @Param("lastName")String lastName);


	/**
	 * ******************************************************************************************
	 * @description : 入参demo4
	 * 使用PoJo 来传递参数
	 *
	 * ******************************************************************************************
	 */

	public Employee getEmpByPojo(Employee employee);



	/**
	 * ******************************************************************************************
	 * @description : resultType demo2
	 * 				  返回结果为对象列表
	 *
	 * ******************************************************************************************
	 */
	public List<Employee> getEmpsList();

	/**
	 * ******************************************************************************************
	 * @description : resultType demo3
	 * 				  返回结果为 map对象
	 *
	 * ******************************************************************************************
	 */
	@MapKey("email")
	public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);

	public Map<String, Object> getEmpByIdReturnMap(Integer id);

	public List<Map<String, Object>> getAllEmpByReturnMapList();


	public int addEmp(Employee employee);

	public boolean updateEmp(Employee employee);

	public void deleteEmpById(Integer id);



}
