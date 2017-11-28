<html>
	<head>
		<title>student</title>
	</head>
	<body>
		学生信息:<br>
		学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
		姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
		年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
		家庭地址:${student.address}<br>
		<hr>
		学生列表:
		<table border="1">
			<tr>
				<th>序号</th>
				<th>学号</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>家庭住址</th>
			</tr>
			<#list list as stu>
			<#if stu_index%2==0>
				<tr bgcolor="red">
			<#else>
				<tr bgcolor="green">
			</#if>
					<td>${stu_index}</td>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
					<td>${stu.address}</td>
				</tr>
			</#list>
		</table>
		<hr>
		当前日期:${date?date}<br>
		当前日期:${date?time}<br>
		当前日期:${date?datetime}<br>
		当前日期:${date?string("yyyy/MM/dd HH:mm:ss")}
		
		<hr>
		空值处理${null!":值为空"}
		判断null的值是否为空:<br>
		<#if null??>
		有内容
		<#else>
		无内容
		</#if>
		
		<hr>
		引用模板测试:<br>
		<#include "hello.ftl">
	</body>
</html>