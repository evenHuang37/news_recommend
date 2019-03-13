package conn;

import java.io.File;

public class DeleteFile {
	DeleteFile() {
	}

	DeleteFile(String fileName) {

		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();

		} else {

		}
	}
	 public static boolean deleteFile(String fileName){    
	        File file = new File(fileName); 
	      
	        if(file.isFile() && file.exists()){    
	            file.delete();    
	            return true;    
	        }else{    
	            return false;    
	        }    
	    }    
public static void main(String[] args) {
}

}
