package dao;

import java.lang.reflect.GenericArrayType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.User;

public class UserDAO {
	public UserDAO(){
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
				"bigsword","bigsword");
	}
	
	/**
	 * @return
	 */
	public int getTotal() {
		int total = 0;
		try(Connection c = getConnection(); Statement s= c.createStatement();){
			String sql = "select count(*) from user_";
			
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
			rs.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public void add(User user) {
		String sql = "insert into user_ values(user_seq.nextval,?,?,?,?,?)";
		System.out.println(sql);
		
		try(Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
			
			ps.setInt(1, user.getCode());
			ps.setString(2, user.getName());
			ps.setInt(3, user.getSex());
			ps.setInt(4, user.getAge());
			ps.setInt(5, user.getPosition());
			
			ps.execute();
			c.commit();
			
/*			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int key = rs.getInt(1);
				user.setKey(key);
			}*/
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(User user) {
		String sql = "update user_ set code=?,name=?,sex=?,age=?,position=? where key=?";
		System.out.println(sql);
		try(Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
			
			ps.setInt(1, user.getCode());
			ps.setString(2, user.getName());
			ps.setInt(3, user.getSex());
			ps.setInt(4, user.getAge());
			ps.setInt(5, user.getPosition());
			ps.setInt(6, user.getKey());
			
			ps.execute();
			c.commit();
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int key) {
		try(Connection c= getConnection();Statement s = c.createStatement();) {
			String sql = "delete from user_ where key = " + key;
			System.out.println(sql);
			
			//¥¶¿Ìsql”Ôæ‰
			s.execute(sql);
			c.commit();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User get(int key) {
		User user = null;
		String sql = "select * from user_ where key = " + key;
		try(Connection c = getConnection();Statement s= c.createStatement();
				ResultSet rs = s.executeQuery(sql);) {
			
			if(rs.next()) {
				user = new User();
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int sex = rs.getInt("sex");
				int age = rs.getInt("age");
				int position = rs.getInt("position");
				user.setName(name);
				user.setSex(sex);
				user.setAge(age);
				user.setPosition(position);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public List<User> list(){
		return list(0,Short.MAX_VALUE);
	}
	
	public List<User> list(int start, int count){
		List<User> users = new ArrayList<User>();
		
		String sql = "select a1.* from (select a.*,rownum rn from (select * from user_ order by code) a) a1 where rn between ? and ?";
		
		try(Connection c= getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			 ps.setInt(1,start+1);
			 ps.setInt(2,count);
			 ResultSet rs = ps.executeQuery();
			
		
			while(rs.next()) {
				User user = new User();
				int key = rs.getInt("key");
				int code = rs.getInt("code");
				String name = rs.getString("name");
				int sex = rs.getInt("sex");
				int age = rs.getInt("age");
				int position = rs.getInt("position");
				user.setKey(key);
				user.setCode(code);
				user.setName(name);
				user.setSex(sex);
				user.setAge(age);
				user.setPosition(position);
				
				users.add(user);
			}
			rs.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
}
