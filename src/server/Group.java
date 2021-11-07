package server;

import java.util.Vector;

/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */

public class Group {
	private String idGroup ;
	private Vector<Chatter> chatter ;
	public Group(String idGroup, Vector<Chatter> chatter) {
		super();
		this.idGroup = idGroup;
		this.chatter = chatter;
	}
	public String getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(String idGroup) {
		this.idGroup = idGroup;
	}
	public Vector<Chatter> getChatter() {
		return chatter;
	}
	public void setChatter(Vector<Chatter> chatter) {
		this.chatter = chatter;
	}
	
}
