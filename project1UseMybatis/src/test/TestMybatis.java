package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mapper.DataMapper;
import mapper.UserMapper;
import pojo.Data;
import pojo.User;

public class TestMybatis {
	
	private void xmlCRUD(SqlSession session) {
		User u = new User();
		u.setName("Coooool");
		session.insert("addUser",u);
		
		User u2 = session.selectOne("getUser",u.getName());
		u2.setCode(110);
		session.update("updateUser",u2);
		
		User u3 = new User();
		u3.setKey(3);
		session.delete("deleteUser",u3);
		
		List<User> users = session.selectList("listUser");
		for(User user : users) {
			System.out.println(user);
		}
	}
	
	public void mapperCRUD(SqlSession session) {
		UserMapper mapper = session.getMapper(UserMapper.class);
		
		User u1 = new User();
		u1.setName("Colllll");
		mapper.add(u1);
		
		u1 = mapper.get(u1.getName());
		u1.setCode(100);
		mapper.update(u1);
		
		mapper.delete(3);
		
		List<User> us = mapper.list(0,1);
		for(User u : us	) {
			System.out.println(u);
		}
	}
	
	public void mapperData(SqlSession session) {
		DataMapper mapper = session.getMapper(DataMapper.class);
		
		for(int i = 0; i < 8; i++) {
			Data data = new Data();
			
			String[] time = {"00","30"};
			data.setTime(10+i/2 + ":" + time[i%2]);
			data.setPercent((int)(Math.random()*60 + 30));
			mapper.add(data);
		}
		session.commit();

		
		List<Data> ds = mapper.list(0, 10);
		for(Data d : ds) {
			System.out.println(d);
		}
		
	}

	public static void main(String[] args) throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		
		//new TestMybatis().xmlCRUD(session);
		//new TestMybatis().mapperCRUD(session);
		new TestMybatis().mapperData(session);

	}
}
