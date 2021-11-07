package Class;

import javax.swing.JLabel;

public class PacketUser {
	private String name ;
	private JLabel lblUser ; 
	public PacketUser(String name, JLabel lblUser) {
		super();
		this.name = name;
		this.lblUser = lblUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JLabel getLblUser() {
		return lblUser;
	}
	public void setTxtUser(JLabel lblUser) {
		this.lblUser = lblUser;
	}
	

}
