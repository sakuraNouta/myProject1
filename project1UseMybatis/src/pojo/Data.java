package pojo;

public class Data {

	private int id;
	private String time;
	private int percent;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	public int getPercent() {
		return percent;
	}
	
	@Override
	public String toString() {
		return "id:" + id + " time:" + time + " percent:" + percent;
	}
}
