package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import bean.User;
import dao.UserDAO;
import net.sf.json.JSONObject;

public class UserSaveServlet extends HttpServlet{
	
	public void saveXML(Document document) {
		
		Element root = document.getRootElement();
		Element usersEle = root.element("users");
		
		List<User> users = new ArrayList<>();
		
		for(Iterator i = usersEle.elementIterator(); i.hasNext(); ) {
			
			Element element = (Element) i.next();

			String method = element.elementText("method");
			
			int key = 0, code = 0, sex = 0, age = 0, position = 0;
			String name = "";
			
			try {
				key = Integer.parseInt(element.elementText("key"));
				code = Integer.parseInt(element.elementText("code"));
				name = element.element("name").getText();
				sex = Integer.parseInt(element.elementText("sex"));
				age = Integer.parseInt(element.elementText("age"));
				position = Integer.parseInt(element.elementText("position"));
				
			}catch(NumberFormatException e) {
				//System.out.println("不同的方法上传的参数不一样,不算错误");
			}

			User user = new User();
			user.setMethod(method);
			user.setKey(key);
			user.setCode(code);
			user.setName(name);
			user.setSex(sex);
			user.setAge(age);
			user.setPosition(position);
			
			users.add(user);
			//System.out.println(user);
		}
		
		UserDAO userDAO = new UserDAO();
		
		for(User user : users) {
			//分别add update delete
			System.out.println(user);
			switch(user.getMethod()) {
			case "add": userDAO.add(user); break;
			case "update": userDAO.update(user); break;
			case "delete": userDAO.delete(user.getKey()); break;
			default: System.out.println("no method!");
			}
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException{
		String data = request.getParameter("data");
		
		//dom4j解析xml
		System.out.println(data);
		
		StringBuffer responseXML = null;
		
		if(data != null)
			try {
				Document document = DocumentHelper.parseText(data);
				saveXML(document);
				//成功返回0
				responseXML = new StringBuffer("<root>"
						+ "<code>0</code><msg>success</msg>"
						+ "</root>");
				
			} catch (Exception e) {
				//失败返回1
				responseXML = new StringBuffer("<root>"
						+ "<code>1</code><msg>fail</msg>"
						+ "</root>");
				e.printStackTrace();
			}

		//返回xml
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-COntrol", "no-cache");
		
		PrintWriter out = response.getWriter();
		out.print(responseXML);
	}
}
