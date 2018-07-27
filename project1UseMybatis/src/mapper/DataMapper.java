package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import pojo.Data;

public interface DataMapper {
	
	@Insert("insert into data values(data_seq.nextval,#{time},#{percent})")
	public int add(Data data);
	
	@Delete("delete from data where id=#{id}")
	public void delete(int id);
	
	@Select("select * from data where id=#{id}")
	public Data get(int id);
	
	@Update("update data set time=#{time},percent=#{percent} where id=#{id}")
	public int update(Data data);
	
	@Select("select a1.* from (select data.*,rownum rn from data) a1 where rn between #{start} and #{count}")
	public List<Data> list(@Param("start") int start, @Param("count") int count);
}
