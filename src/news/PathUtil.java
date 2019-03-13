package news;


import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * ��ͨJAVA��ȡ   WEB��Ŀ�µ�WEB-INFĿ¼
 * @author wang
 *
 */
public class PathUtil {
	public static void main(String[] args) {
		PathUtil pathUtil = new PathUtil();
		System.out.println("path=="+pathUtil.getWebInfPath());
	}
	
	public String getWebInfPath(){
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		String path = url.toString();
		System.out.println(path);
		int index = path.indexOf("WEB-INF");
		
		if(index == -1){
			index = path.indexOf("classes");
		}
		
		if(index == -1){
			index = path.indexOf("bin");
		}
		
		path = path.substring(0, index);
		
		if(path.startsWith("zip")){//��class�ļ���war��ʱ����ʱ����zip:D:/...������·��
			path = path.substring(4);
		}else if(path.startsWith("file")){//��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��
			path = path.substring(6);
		}else if(path.startsWith("jar")){//��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��
			path = path.substring(10); 
		}
		try {
			path =  URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return path;
	}
}