---
title: mybatis之动态sql
date: 2021-08-15 15:27:34
categories: 
- ORM
tags:
- Mybatis
---

在实际开发中，经常会出现各种条件查询。即： 查询员工，要求携带了哪个字段查询条件就带上这个字段的值 ，这种场景下，往往需要能够根据传入的参数动态生成sql，进行查询。在动态生成的过程 中，常用一下标签来辅助生成各种动态查询语句：
- if: 判断
- choose (when, otherwise): 分支选择；带了break的swtich-case
	如果带了id就用id查，如果带了lastName就用lastName查;只会进入其中一个
- trim 字符串截取(where(封装查询条件), set(封装修改条件))
- foreach 遍历集合

### if标签

举例：

```xml
	 <select id="getEmpsByConditionIf" resultType="com.atguigu.mybatis.bean.Employee">
	 	select * from tbl_employee
	 	<where>
		 	<if test="id!=null">
		 		id=#{id}
      </if>
      <if test="lastName!=null">
		 		and last_name=#{lastName}
      </if>
	 	</where>
	 </select>
```

在 if 标签中， test作为判断表达式，（OGNL）。if  test从参数中取值进行判断。当遇见特殊符号时，应该去写转义字符。
在OGNL中，常见的转移字符对应如下：

| 字符                           | 十进制 | 转义字符 |
| -----------------------------  | ------ | -------- |
| "                              | `&#34;`  | `&quot; `  |
| &                              | `&#38;`  | `&amp;`  |
| <                              | `&#60;`  | `&lt;`     |
| >                           | `&#62;`  | `&gt;`     |
| 不断开空格(non-breaking space) | `&#160;` | `&nbsp;`   |

- **where 1=1 与 <where>**

在动态参数时，若参数传递的过少，则可能导致sql出错，对于这种情况，有两种情况。

(1)  一种是在where条件的后边加上 1==1这个方法。

```xml
	 <select id="getEmpsByConditionIf" resultType="com.sxl.mybatis.bean.Employee">
	 	select * from tbl_employee
	 	where 1==1
		 	<if test="id!=null">
		 		and id=#{id}
		 	</if>
		 	<if test="lastName!=null &amp;&amp; lastName!=&quot;&quot;">
		 		and last_name like #{lastName}
		 	</if>
		 	<if test="email!=null and email.trim()!=&quot;&quot;">
		 		and email=#{email}
		 	</if> 
		 	<!-- ognl会进行字符串与数字的转换判断  "0"==0 -->
		 	<if test="gender==0 or gender==1">
		 	 	and gender=#{gender}
		 	</if>
	 	</where>
	 </select>
	 
```

(2)  另一种是采用<where>标签。

```xml
	 <select id="getEmpsByConditionIf" resultType="com.sxl.mybatis.bean.Employee">
	 	select * from tbl_employee
	 	<where>
		 	<if test="id!=null">
		 		id=#{id}
		 	</if>
		 	<if test="lastName!=null &amp;&amp; lastName!=&quot;&quot;">
		 		and last_name like #{lastName}
		 	</if>
		 	<if test="email!=null and email.trim()!=&quot;&quot;">
		 		and email=#{email}
		 	</if> 
		 	<!-- ognl会进行字符串与数字的转换判断  "0"==0 -->
		 	<if test="gender==0 or gender==1">
		 	 	and gender=#{gender}
		 	</if>
	 	</where>
	 </select>
```

在这种方式下，<where>标签会自动将 if 条件中,第一个 多余的```and``` 或者``` or``` 去掉。

 

### trim标签

trim表示字符串截取的意思。  对该标签的使用案例如下：

```xml
<select id="getEmpsByConditionTrim" resultType="com.sxl.mybatis.bean.Employee">
	 	select * from tbl_employee
	 	<!-- 自定义字符串的截取规则 -->
	 	<trim prefix="where" suffixOverrides="and">
	 		<if test="id!=null">
		 		id=#{id} and
		 	</if>
		 	<if test="lastName!=null &amp;&amp; lastName!=&quot;&quot;">
		 		last_name like #{lastName} and
		 	</if>
		 	<if test="email!=null and email.trim()!=&quot;&quot;">
		 		email=#{email} and
		 	</if> 
		 	<!-- ognl会进行字符串与数字的转换判断  "0"==0 -->
		 	<if test="gender==0 or gender==1">
		 	 	gender=#{gender}
		 	</if>
		 </trim>
	 </select>
```

trim 标签可以解决<where>标签不能解决的问题，即：后面多出的and或者or。

trim标签体中是整个字符串拼串后的结果。可以trim标签中的一些标签来自定义字符串的截取规则，相关标签的含义如下

- prefix="":前缀：

  - prefix**给拼串后的整个字符串加一个前缀** 。
  
- prefixOverrides="": 前缀覆盖： 
  - **去掉整个字符串前面多余的字符**

- suffix="": 后缀
  - suffix **给拼串后的整个字符串加一个后缀** 

- suffixOverrides=""后缀覆盖：
     - **去掉整个字符串后面多余的字符**

### choose (when,otherwise)

choose标签的作用，类似于Java语言中的switch ... case. 根据传入的不同的值，来执行不同的值。

即：如果带了id就用id查，如果带了lastName就用lastName查;只会进入其中一个

举例如下：

```xml
<!-- public List<Employee> getEmpsByConditionChoose(Employee employee); -->
	 <select id="getEmpsByConditionChoose" resultType="com.sxl.mybatis.bean.Employee">
	 	select * from tbl_employee 
	 	<where>
	 		<!-- 如果带了id就用id查，如果带了lastName就用lastName查;只会进入其中一个 -->
	 		<choose>
	 			<when test="id!=null">
	 				id=#{id}
	 			</when>
	 			<when test="lastName!=null">
	 				last_name like #{lastName}
	 			</when>
	 			<when test="email!=null">
	 				email = #{email}
	 			</when>
	 			<otherwise>
	 				gender = 0
	 			</otherwise>
	 		</choose>
	 	</where>
	 </select>
```

  ### foreach

当需要批量查询时，可将where与 foreach 标签相结合，实现批量查询。

示例如下：

```xml
<select id="getEmpsByConditionForeach" resultType="com.sxl.mybatis.bean.Employee">
	 	select * from tbl_employee
	 	<foreach collection="ids" item="item_id" separator=","
	 		open="where id in(" close=")">
	 		#{item_id}
	 	</foreach>
	 </select>
```

关于foreach标签下的各个子属性，介绍如下：

collection：指定要遍历的集合：

item：将当前遍历出的元素赋值给指定的变量

separator: **每个元素之间的分隔符**

open ：**遍历出列表中的所有结果拼接一个开始的字符**

close:   **遍历出列表中的所有结果拼接一个结束的字符** 

index: 索引。

遍历*list*的时候是*index*就是索引，*item*就是当前值

*#{*变量名*}*就能取出变量的值也就是当前遍历出的元素。



#### foreach 批量插入1

```xml
<!-- 批量保存 foreach 方式1-->
	 <!--public void addEmps(@Param("emps")List<Employee> emps);  -->
	 <!--MySQL下批量保存：可以foreach遍历   mysql支持values(),(),()语法-->
	<insert id="addEmps">
	 	insert into tbl_employee( last_name,email,gender,d_id)
		values
		<foreach collection="emps" item="emp" separator=",">
			(#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
		</foreach>
	 </insert>
```

该方式批量插入式，使用的是一条sql来执行的。

#### foreach 批量插入2

```xml
<!-- 批量保存 foreach 方式2-->
	<!-- 这种方式需要数据库连接属性allowMultiQueries=true；-->
	  <insert id="addEmps">
	 	<foreach collection="emps" item="emp" separator=";">
	 		insert into tbl_employee(last_name,email,gender,d_id)
	 		values(#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
	 	</foreach>
	 </insert>
```

这种分号分隔的方式批量插入，是将多个sql执行来实现，可以用于其他的批量操作（删除，修改）。

### 两个内置参数

 mybatis默认还有两个内置参数：分别为 _parameter 和 _databaseId
_parameter: 代表整个参数_

- 单个参数：_parameter就是这个参数
- 多个参数：参数会被封装为一个map；_parameter就是代表这个map

_databaseId:   

如果在mybatis的全局设置中配置了databaseIdProvider标签，则 _databaseId就是代表当前数据库的别名。 



### bind

在动态sql的查询过程中，可以使用bind标签，将OGNL表达式的值绑定到一个变量中，方便后来引用这个变量的值。

```xml
<!--public List<Employee> getEmpsTestInnerParameter(Employee employee);  -->
<select id="getEmpsTestInnerParameter" resultType="com.sxl.mybatis.bean.Employee">
      <!-- bind：可以将OGNL表达式的值绑定到一个变量中，方便后来引用这个变量的值 -->
      <bind name="_lastName" value="'%'+lastName+'%'"/>
      <if test="_databaseId=='mysql'">
         select * from tbl_employee
         <if test="_parameter!=null">
            where last_name like #{_lastName}
         </if>
      </if>
</select>
```



### sql

抽取可重用的sql片段。方便后面引用 
1、sql抽取：经常将要查询的列名，或者插入用的列名抽取出来方便引用。
2、可以使用 include来引用已经抽取的sql。
3、include还可以自定义一些property，sql标签内部就能使用自定义的属性
      其中，在include标签定义的property，取值的正确方式为${prop},不能使用#{}这种方式。

例如，定义可重复的sql片段如下：

```
<sql id="insertColumn">
     last_name,email,gender,d_id
</sql>
```

在批量保存中，引用上述的片段为：



```
<!-- 批量保存 -->
 <!--public void addEmps(@Param("emps")List<Employee> emps);  -->
 <!--MySQL下批量保存：可以foreach遍历   mysql支持values(),(),()语法-->
<insert id="addEmps">
   insert into tbl_employee(
      <include refid="insertColumn"></include>
   ) 
   values
   <foreach collection="emps" item="emp" separator=",">
      (#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
   </foreach>
 </insert>
 
```



```xml
 <!-- 批量保存 -->
 <!--public void addEmps(@Param("emps")List<Employee> emps);  -->
 <!--MySQL下批量保存：可以foreach遍历   mysql支持values(),(),()语法-->
<insert id="addEmps">
   insert into tbl_employee(
      <include refid="insertColumn"></include>
   ) 
   values
   <foreach collection="emps" item="emp" separator=",">
      (#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
   </foreach>
 </insert>
```
