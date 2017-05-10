package qt.mqtt;

public class Msg {
	private Integer id;
	private String msg;
	public Msg(){}
	public Msg(Integer id, String msg)
	{
		this.id=id;
		this.msg=msg;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
