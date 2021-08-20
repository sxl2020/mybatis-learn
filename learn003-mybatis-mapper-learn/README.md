---
title: Mybatis映射文件之参数传递
date: 2021-08-08 15:27:34
categories: 
- ORM
tags:
- Mybatis
---

## Mapper参数（Parameters）传递

映射文件指导着MyBatis如何进行数据库增删改查， 有着非常重要的意义。在mapper映射文件中，常见的标签有如下：

- insert – 映射插入语句
- update – 映射更新语句
- delete – 映射删除语句
- select – 映射查询语句

而在增删改查的过程中，往往需要传递参数。因此，本文主要来说明在使用mybatis的过程中，一些常见的参数传递方式。

在操作的过程中，主要通过tbl_emp和tbl_dept这两张表来操作。

表的结构如下：

```sql
CREATE TABLE `tbl_employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `d_id` int DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `did` int DEFAULT NULL,
  `dept_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```

### 单个参数

单个参数传递时，可以接受基本类型，对象类型，集合类型的值。这种情况 MyBatis可直接使用这个参数，不需要经过任何处理。

可以通过**#{参数名/任意名}：取出参数值。**

若定义的接口函数为：single

```java
public Employee getEmpById(Integer id);
```

则如下两种方式，都可以取出传递的值


```xml
<select id="getEmpById" resultType="com.sxl.mapperlearn.bean.Employee">
		select * from tbl_employee where id = #{id}
</select>

<select id="getEmpById" resultType="com.sxl.mapperlearn.bean.Employee">
		select * from tbl_employee where id = #{anystring}
</select>
```

###  **多个参数**

任意多个参数，在传递的过程中，都会被MyBatis**重新包装成一个Map传入。**

传参的方式，可以采用多个入参传入，也可以直接采用一个Map对象进行传入。

- 方式一

```java
// 方式一
public Employee getEmpByIdAndLastName(Integer id, String lastName);
```

则对应的映射文件，每个参数可以参数与入参一致的字段表示，或者使用param1等代替。

如下所示：

```xml
<select id="getEmpByIdAndLastName" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from tbl_employee where id = #{id} and last_name=#{lastName}
</select>
或者
<select id="getEmpByIdAndLastName" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from tbl_employee where id = #{param1} and last_name=#{param2}
</select>
```

- 方式二

```java
public Employee getEmpByMap(Map<String, Object> map);
```

而采用map的方式时，**在xml文件中接收的字段，必须与map中的key保持一致才可**。

若以上接口传递的参数为：

```java
Map<String,Object> params = new HashMap<>();
params.put("id111",1);
params.put("lastName111","sqc");	
```

则在xml文件中对应的值必须与上述保持一致，不能采用param等方式代指。

```xml
<select id="getEmpByIdAndLastName" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from tbl_employee where id = #{id111} and last_name=#{lastName111}
</select>
```

### 命名参数

为参数使用@Param起一个名字，MyBatis就会将这些参数封 装进map中，**key就是我们自己指定的名字**。

```java
public Employee getEmpByIdAndLastName(@Param("id")Integer id,@Param("lastName")String lastName);
```

在这种情况下，在xml中的传递参数的名字必须与@Param注解中指定的名字一致。

### POJO

如果多个参数正好是我们业务逻辑的数据模型，我们就可以直接传入pojo；
#{属性名}：取出传入的pojo的属性值   

```java
public Long addEmp(Employee employee);
```

对应的mapper映射文件为：

```xml
	<insert id="addEmp" parameterType="com.sxl.mapperlearn.bean.Employee">
		insert into tbl_employee(last_name,email,gender) 
		values(#{lastName},#{email},#{gender})
	</insert>
```

### **Map**

如果多个参数不是业务模型中的数据，没有对应的pojo，不经常使用，为了方便，我们也可以传入map。
#{key}：取出map中对应的值

```xml
public Employee getEmpByMap(Map<String, Object> map);
```

对应的Mapper映射文件为：

```xml
	<select id="getEmpByMap" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from ${tableName} where id=${id} and last_name=#{lastName}
 	</select>
```


## 参数处理


### #{} 与 ${}

- #{}: 以预编译的形式，将参数设置到sql语句中，即PreparedStatement，可以方式sql注入
- ${}: 取出的值直接拼装在SQL中。**会有安全问题。** 

大多数情况下，采用#{}的形式。

原生jdbc不支持占位符的地方我们就可以使用${}进行取值。比如分表、排序。。。；按照年份分表拆分等。

```sql
<!-- public Employee getEmpByMap(Map<String, Object> map); -->
 	<select id="getEmpByMap" resultType="com.sxl.mapperlearn.bean.Employee">
 		select * from ${tableName} where id=${id} and last_name=#{lastName}
 	</select>
 	
 	select * from ${year}_salary where xxx;
	select * from tbl_employee order by ${f_name} ${order}
```

### jdbcType

数据库列字段都是有类型的，不同的数据库有不同的类型。为了表示这些数据类型，Java源码是采用枚举来定义的：

```java
public enum JDBCType implements SQLType {
    TINYINT(Types.TINYINT),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER)
}
```

枚举变量也是有类型的，也是有值的，正如整数变量是整数类型，也有值大小一样，上述枚举变量值的定义是通过Types来描述刻画的。

```java
public class Types {
     public final static int TINYINT         =  -6;
     public final static int SMALLINT        =   5;
     public final static int INTEGER         =   4;  
}
```

在Mybatis明文建议在映射字段数据时需要将JdbcType属性加上，这样相对来说是比较安全的。

```xml
<insert id="saveRole">
    insert into role values (
        #{roleId},
        #{name},
        #{remarks},
        #{orderNo},
        #{createBy,jdbcType=VARCHAR},
        #{createDept,jdbcType=VARCHAR},
        #{createTime,jdbcType=DATE},
        #{updateBy,jdbcType=VARCHAR},
        #{updateTime,jdbcType=DATE}
    )
</insert>
```

这样，保证了前四种是不能为空的前提下，而后面几项为空时也不至于程序报错。

如果createBy为空，插入的时候mybatis不知道具体转换成什么jdbcType类型，通常会使用一个默认设置，虽然默认配置一般情况下不会出错，但是遇到个别情况还是会有问题的。

Mybatis经常出现的：无效的列类型: 1111 错误，就是因为没有设置JdbcType造成的。

- 举例

在使用#{} 来传递参数时，可以来规定参数的一些规则。

– javaType、jdbcType、mode、numericScale、 resultMap、typeHandler、jdbcTypeName、

javaType 在某些特定的情况下，需要被设置。具体设置什么类型，通常可以从参数对象中来去确定。

– **如果 null 被当作值来传递，对于所有可能为空的列， jdbcType 需要被设置**

在我们数据为null的时候，有些数据库可能不能识别mybatis对null的默认处理。比如Oracle（报错）；

因为mybatis对所有的null都映射的是**原生Jdbc的OTHER类型**，即:由于全局配置中：jdbcTypeForNull=OTHER；但是Oracle不支  持处理这种类型。

- 解决办法1：在SQL语句中手动指定

手动引导mybatis 将null映射为NULL。

```xml
#{email,jdbcType=NULL};
```

- 解决方法2： 全局设置

```xml
<setting name="jdbcTypeForNull" value="NULL"/>
```



