package Class;

import java.awt.Font;

import javax.swing.JLabel;

public class PacketUser {
	private String name ;
	private JLabel lblUser ; 
	private String idUser ; 
	public PacketUser(String name, JLabel lblUser, String idUser) {
		super();
		this.name = name;
		this.lblUser = lblUser;
		this.idUser = idUser ; 
		lblUser.setFont(new Font("Meiryo", Font.PLAIN, 13));
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
	public String getIdUser() {
		return this.idUser; 
	}
	

}
