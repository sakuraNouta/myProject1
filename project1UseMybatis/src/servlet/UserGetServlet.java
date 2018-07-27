package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dao.UserDAO;
import mapper.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pojo.User;

public class UserGetServlet extends HttpServlet{

	
	private JSONObject User2Json(List<User> users,int total) {
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject temp = new JSONObject();

		for(User user : users) {
			temp.put("user", JSONObject.fromObject(user));
			array.add(temp);
		}
		json.put("users", array);
		json.put("total", total);
		return json;
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);

		//xml <-this <- dao <- oracle
		List<User> users = new ArrayList<User>();
		
		//分页查询
		int start = 0;
		int count = 10;
		
		try {
			start = Integer.parseInt(request.getParameter("start"));
		} catch(NumberFormatException e) {
			//当浏览器没有穿参数start
			e.printStackTrace();
		}
		
		int next = start + count;
		
		users = mapper.list(start+1,next);
		int total = mapper.getTotal();

		//将users封装成json
		JSONObject json = User2Json(users,total);
		//返回xml
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		PrintWriter out = response.getWriter();
		out.print(json);
	}
}
