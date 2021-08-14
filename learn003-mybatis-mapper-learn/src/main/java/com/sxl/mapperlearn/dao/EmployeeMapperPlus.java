package com.sxl.mapperlearn.dao;


import com.sxl.mapperlearn.bean.Employee;

import java.util.List;

/**
 * @description 关于映射文件的 resultMap标签的使用
 * @date 2021/8/12 下午9:31
 */


public interface EmployeeMapperPlus {
	
	public Employee getEmpById(Integer id);
	
	public Employee getEmpAndDept(Integer id);
	
	public Employee getEmpByIdStep(Integer id);
	
	public List<Employee> getEmpsByDeptId(Integer deptId);

}
