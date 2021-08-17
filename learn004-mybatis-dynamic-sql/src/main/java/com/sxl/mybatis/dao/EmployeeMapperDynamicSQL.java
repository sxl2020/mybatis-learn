package com.sxl.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sxl.mybatis.bean.Employee;

public interface EmployeeMapperDynamicSQL {
	
	 List<Employee> getEmpsTestInnerParameter(Employee employee);
	
	//携带了哪个字段查询条件就带上这个字段的值
	 List<Employee> getEmpsByConditionIf(Employee employee);
	
	 List<Employee> getEmpsByConditionTrim(Employee employee);
	
	 List<Employee> getEmpsByConditionChoose(Employee employee);
	
	 void updateEmp(Employee employee);
	
	//查询员工id'在给定集合中的
	 List<Employee> getEmpsByConditionForeach(@Param("ids") List<Integer> ids);
	
	 void addEmps(@Param("emps") List<Employee> emps);

}
