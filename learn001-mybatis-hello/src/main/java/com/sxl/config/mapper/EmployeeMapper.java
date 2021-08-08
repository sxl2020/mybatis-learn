package com.sxl.config.mapper;


import com.sxl.config.bean.Employee;

/*
	此接口的作用，就是查出数据，并将其封装为Employee对象
	接口可以和配置文件动态绑定！
        方法名 可以和配置文件中的标签绑定：
        	get  ---> <select>
        	set  ---> <insert>
        	update --> <update>
 */
public interface EmployeeMapper {
	
	public Employee getEmpById(Integer id);

}
