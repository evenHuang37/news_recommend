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
 * 因为抓到的用户ID都是数据都是类似434132这么长的编号，为了方便我用数组存，
 * 事先我全部转化为1开始的编号了。比如说有两个4654654,32131321，
 * 那我就转成1,2了，处理完推荐完再把1,2转成4654654,32131321
 * 
 */
/*
public class id_change {
	
	public static HashMap<String,Integer> user_id = new HashMap<String,Integer>();
	public static HashMap<String,Integer> hotel_id = new HashMap<String,Integer>();
	
	//数据路径
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