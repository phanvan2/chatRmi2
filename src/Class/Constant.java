package Class;

public class Constant {
	public final String LINK_PATH_IMAGE =  "image\\"; 
	public final int PORT = 2020 ;
	public final String HOST = "localhost" ;
	public Boolean checkGroup(String idUser) {
		if(idUser.contains("group"))
			return true  ;
		return false  ; 
	}
	
	public boolean checkChatPublic(String idUser) {
		if(idUser.equals("111publicChat")) 
			return true;
		return false ; 
	}

}
