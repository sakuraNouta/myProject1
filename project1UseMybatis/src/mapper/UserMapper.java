package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import pojo.User;

public interface UserMapper {
	
	@Insert("insert into user_ values(user_seq.nextval,#{code},#{name},#{sex},#{age},#{position})")
	public int add(User user);
	
	@Delete("delete from user_ where key=#{key}")
	public void delete(int key);
	
	@Select("select * from user_ where name=#{name}")
	public User get(String name);
	
	@Update("update user_ set code=#{code},name=#{name},sex=#{sex},age=#{age},position=#{position} where key=#{key}")
	public int update(User user);
	
/*	@Select("select * from user_")
	public List<User> list();*/
	
	@Select("select count(*) from user_")
	public int getTotal();
	
	@Select("select a1.* from (select a.*,rownum rn from (select * from user_ order by code) a) a1 where rn between #{start} and #{count}")
	public List<User> list(@Param("start") int start, @Param("count") int count);
}
