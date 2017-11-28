package com.jisen.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author Administrator
 *
 */
public class FreeMarkerTest {
	@Test
	public void testFreeMarker()throws Exception{
		
		//1创建一个模板文件
		//2创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3设置模板文件的目录
		configuration.setDirectoryForTemplateLoading(new File("C:/Users/Administrator/workspace_yilifang/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//4模板文件的编码格式,一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5加载一个模板文件,创建一个模板对象
		Template template = configuration.getTemplate("student.ftl");
		//6.创建一个数据集,可以是pojo也可以是map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarkerhahahhahah");
		//创建一个pojo对象
		Student student = new Student(1001,"小明",18,"赣州");
		data.put("student", student);
		//添加一个list
		List<Student> list = new ArrayList<Student>();
		list.add(new Student(1001,"小明1",20,"赣州"));
		list.add(new Student(1002,"小明2",20,"赣州"));
		list.add(new Student(1003,"小明3",20,"赣州"));
		list.add(new Student(1004,"小明4",20,"赣州"));
		list.add(new Student(1005,"小明5",20,"赣州"));
		list.add(new Student(1006,"小明6",20,"赣州"));
		data.put("list", list);
		//添加日期类型
		data.put("date", new Date());
		//添加null
		data.put("null", 123);
		//7.创建一个writer对象,指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("C:/Users/Administrator/Desktop/student.html"));
		//8.生成静态页面
		template.process(data, out);
		//9.关闭流
		out.close();
		
	}
}
