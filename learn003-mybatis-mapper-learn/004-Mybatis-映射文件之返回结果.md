---
title: Mybatis映射文件之返回结果
date: 2021-08-14 15:27:34
categories: 
- ORM
tags:
- Mybatis
---

在mybatis的默认设置中，会默认开启自动映射。所谓自动映射，即将JavaBean中的属性映射到数据库表中的列名。

设置自动映射的规则，需要在全局setting标签中进行。

```xml
<settings>
   <setting name="autoMappingBehavior" value="NULL"/>
   	<!--	开启下划线命名规则至驼峰命名规则的映射	-->
   <setting name="mapUnderscoreToCamelCase" value="true"/> 
</settings>
```

### resultType

在mybatis中select元素来定义查询操作。其中，resultType表示查询返回的值类型。

类型可以是别名或者全类名，如果返回的是集合或者是单个实体，resultType定义为集合中元素的类型。不能和resultMap同时使用。

#### 返回单个实体

```java
	Employee getEmpById(Integer id);
```

- 定义的xml查询语句如下：

```xml
		<select id="getEmpById" resultType="com.sxl.mapperlearn.bean.Employee">
		select * from tbl_employee where id = #{id}
	</select>
```

#### **返回List对象**

```java
	public List<Employee> getEmpsList();
```

定义的xml语句

```xml
<select id="getEmpsList" resultType="com.sxl.mapperlearn.bean.Employee">
		select * from tbl_employee
	</select>
```

#### 返回map对象

- **Map<String,Employee>**

有时候，我们需要返回一个对象，并且把对象放在一个map中。此时，可以使用@MapKey这个注解，来实现指定map中的key.

```java
@MapKey("email")
public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);
```

对应的xml文件为：

```xml
	<select id="getEmpByLastNameLikeReturnMap" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from tbl_employee where  last_name like #{param1}
 	</select>
```

需要注意的是，上面的返回类型还是具体的实体类，只是在mapper接口中，定义的返回格式为Map.

查询的结果为：

```cmd
{tom@163.c0m = Employee [id=1, lastName=null, email=tom@163.c0m, gender=man]}
```

- map

若需要返回的为map,即以key-value的形式返回，直接将resultType的类型定义为map即可。mybatis会自动将表的字段映射为对应的key.

```java
	public Map<String, Object> getEmpByIdReturnMap(Integer id);
```

对应的xml文件为：

```xml
<!--public Map<String, Object> getEmpByIdReturnMap(Integer id);  -->
	<select id="getEmpByIdReturnMap" resultType="map">
 		select * from tbl_employee where id=#{id}
 	</select>
```

查询的结果为

```
{gender=man, d_id=1, last_name=tom, id=1, email=tom@163.c0m}
```

#### 返回mapList

```java
	public List<Map<String, Object>> getAllEmpByReturnMapList();
```

ml：

```xml
<select id="getAllEmpByReturnMapList" resultType="map">
 		select * from tbl_employee
 	</select>
```

最终得到的结果封装到List之中。



### resultMap

在一些较复杂的查询中，使用实体类无法完全满足对返回结果的映射。这时候可以采用自定义resultMap，该方法可以实现高级结果集的映射。

#### resultMap标签解析

使用resultMap 来定义一个简单结果集的常见格式如下：

```xml
	<resultMap type="com.sxl.mapperlearn.bean.Employee" id="MySimpleEmp">
		<id column="id" property="id"/>
		<!-- 定义普通列封装规则 -->
		<result column="last_name" property="lastName"/>
		<result column="email" property="email"/>
		<result column="gender" property="gender"/>
	</resultMap>
```

在上述的格式定义中，各标签的含义如下：

- id 
  - 表示一个 ID 结果; 标记结果作为 ID 可以帮助提高整体效能

- result 
  - 注入到字段或 JavaBean 属性的普通结果

在一些复杂的结果集中，还有一些字段来实现更复杂的映射:

- **association**
  - 一个复杂的类型关联;
  - 许多结果将包成这种类型 嵌入结果映射 – 结果映射自身的关联
- **collection**
  -  复杂类型的集
  - 嵌入结果映射 – 结果映射自身的集,或者参考一个

