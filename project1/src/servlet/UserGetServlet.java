package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import bean.User;
import dao.UserDAO;

public class UserGetServlet extends HttpServlet{

	
	private Document User2Xml(List<User> users) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element _users = root.addElement("users");
		
		//加个total
		root.addElement("total").setText(new UserDAO().getTotal() + "");
		
		for(User user : users) {
			
			Element _user = _users.addElement("user");
			
			Element key = _user.addElement("key");
			Element code = _user.addElement("code");
			Element name = _user.addElement("name");
			Element sex = _user.addElement("sex");
			Element age = _user.addElement("age");
			Element position = _user.addElement("position");
			
			key.setText(user.getKey() + "");
			code.setText(user.getCode() + "");
			name.setText(user.getName() + "");
			sex.setText(user.getSex() + "");
			age.setText(user.getAge() + "");
			position.setText(user.getPosition() + "");
			
		}
		return document;
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
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
		
		users = new UserDAO().list(start,next);

		//将users封装成xml
		Document document = User2Xml(users);
		String responseXML = document.asXML();
		
		
		//返回xml
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		PrintWriter out = response.getWriter();
		out.print(responseXML);
	}
}
