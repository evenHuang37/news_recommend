/*package WjPack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
 * 
 * ��Ϊץ�����û�ID�������ݶ�������434132��ô���ı�ţ�Ϊ�˷�����������棬
 * ������ȫ��ת��Ϊ1��ʼ�ı���ˡ�����˵������4654654,32131321��
 * ���Ҿ�ת��1,2�ˣ��������Ƽ����ٰ�1,2ת��4654654,32131321
 * 
 */
/*
public class id_change {
	
	public static HashMap<String,Integer> user_id = new HashMap<String,Integer>();
	public static HashMap<String,Integer> hotel_id = new HashMap<String,Integer>();
	
	//����·��
	public static String road_main = "data";
	public static String road1 = road_main;
	public static String road1_1 = road_main ;
	public static String road2 = road_main ;
	public static String road2_1 = road_main;
	public static String road3 = road_main ;
	public static String road3_1 = road_main ;
	public static String road4 = road_main ;
	public static String road4_1 = road_main;
	
	
	
	
	
	
	public static void get_rule(HashMap<String,Integer> thing,String r1,String r2) throws IOException {
		FileReader data_about=new FileReader(r1);
		BufferedReader read_data_about=new BufferedReader(data_about);
		FileWriter fw=new FileWriter(r2);
		
		String id; 
		int id_num = 1;
		while((id=read_data_about.readLine())!=null){ 
			if(!thing.containsKey(id)) {
				fw.write(id+"::"+id_num+"\r\n");
				thing.put(id,id_num++);
			}
				
		}
		data_about.close();
		read_data_about.close();
		fw.close();
	}
	public static void change_text_ins(HashMap<String,Integer> thing,String r1,String r2) throws IOException {
		FileReader data_about=new FileReader(r1);
		BufferedReader read_data_about=new BufferedReader(data_about);
		FileWriter fw=new FileWriter(r2);
		
		String []tmps = new String[5];
		
		
		String id; 

		while((id=read_data_about.readLine())!=null){ 
			
			tmps = id.split("::");
			String username = tmps[0];
			String fansname = tmps[1];
			
			if(thing.containsKey(username) && thing.containsKey(fansname)) {
				fw.write(thing.get(username)+"::"+thing.get(fansname)+"\r\n");
			}
				
		}
		data_about.close();
		read_data_about.close();
		fw.close();
	}
	
	public static void change_text_three(HashMap<String,Integer> thing,HashMap<String,Integer> thing2,String r1,String r2) throws IOException {
		FileReader data_about=new FileReader(r1);
		BufferedReader read_data_about=new BufferedReader(data_about);
		FileWriter fw=new FileWriter(r2);
		
		String []tmps = new String[5];
		
		
		String id; 

		while((id=read_data_about.readLine())!=null){ 
			
			tmps = id.split("::");
			String username = tmps[0];
			String fansname = tmps[1];
			String othersname;
			if(tmps.length > 2) {
				othersname= tmps[2];
				
				if(thing.containsKey(username) && thing2.containsKey(fansname)) {
					fw.write(thing.get(username)+"::"+thing2.get(fansname)+"::"+othersname+"\r\n");
				}
			}
				
				
		}
		data_about.close();
		read_data_about.close();
		fw.close();
	}
	



}
*/