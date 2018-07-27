package pojo;

public class User {

	private String method;
	private int key;
	private int code=0;
	private String name;
	private int sex=1;
	private int age=0;
	private int position=0;
	
	
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMethod() {
		return method;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getKey() {
		return key;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getSex() {
		return sex;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return age;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return "User [method=" + method + ",code=" + code + ",key=" + key +
				",name=" + name + ",sex=" + sex + ",age=" + age + ",position=" + position+ "]";
	}
}
