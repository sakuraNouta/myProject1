package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mapper.DataMapper;
import mapper.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pojo.Data;

public class DataGetServlet extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException ,IOException {
		
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		DataMapper mapper = session.getMapper(DataMapper.class);
		
		List<Data> ds = mapper.list(0, 10);
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		for(Data d : ds) {
			json.put("time", d.getTime());
			json.put("percent", d.getPercent());
			array.add(json);
		}
		
		
		PrintWriter out = response.getWriter();
		out.print(array);
	};
	
}
