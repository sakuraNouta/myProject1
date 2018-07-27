package servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dao.UserDAO;
import mapper.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pojo.User;

public class UserSaveServlet extends HttpServlet{
	
	public void saveJSON(Object data, SqlSession session) {
		
		UserMapper mapper = session.getMapper(UserMapper.class);
		
		List<User> users = new ArrayList<>();
		
		JSONArray jsonArray = JSONArray.fromObject(data);
		
		for(Object json : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(json);
			//System.out.println(jsonObject.get("key"));
			if(jsonObject.getString("method").length()==0) continue;

			if(jsonObject.getInt("key") == 0) {
				//System.out.println("0");
				if(jsonObject.getString("method") == "delete")
					continue;
				if(jsonObject.getString("method") == "update")
					jsonObject.put("method","add");
			}
			
			User user = (User)JSONObject.toBean(jsonObject,User.class);
			users.add(user);
		}
		
		for(User user : users) {
			//分别add update delete
			System.out.println(user);
			switch(user.getMethod()) {
			case "add": mapper.add(user); break;
			case "update": mapper.update(user); break;
			case "delete": mapper.delete(user.getKey()); break;
			default: System.out.println("no method!");
			}
		}
		session.commit();
		session.close();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException{
		
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		
		String data = request.getParameter("data");
		System.out.println(data);
		if(data == null) {
			BufferedReader reader = request.getReader();
			data = reader.readLine();
		}
		System.out.println(data);
		
		JSONObject json = new JSONObject();
		if(data != null)
			try {
				saveJSON(data, session);
				//成功返回0(json)
				json.put("flag", "0");
				
			} catch (Exception e) {
				//失败返回1
				json.put("flag", "1");
				e.printStackTrace();
			}

		//返回xml
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		PrintWriter out = response.getWriter();
		out.print(json);
	}
}
