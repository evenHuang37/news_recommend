package news;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileUtils {
	public static void ReaderFileUse(String nameid, String newsid) {
		FileReader reader;
		try {
			reader = new FileReader(StaticConstant.StaticRating);
			BufferedReader bf = new BufferedReader(reader);// 一行一行读
			String yhNews = nameid + "," + newsid + ",";
			try {
				String t1 = bf.readLine();
				t1 = bf.readLine();
				boolean ishava = false;
				while (t1 != null) {
					if (t1.indexOf(yhNews) != -1) {
						bf.close();
						reader.close();
						ishava = true;
						break;
					}

					// System.out.println(t1);
					t1 = bf.readLine();
				}
				bf.close();
				reader.close();
				if (!ishava) {
					Random rdm = new Random(System.currentTimeMillis());
					String rate = String
							.valueOf(Math.abs(rdm.nextInt()) % 5 + 1);
					yhNews = yhNews + rate;
					fileApand(yhNews);
					System.out.println("不存在：" + yhNews);
				}
				System.out.println(yhNews);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ReaderFileUse("358", "cc1ee");
	}

	public static void fileApand(String addString) {
		try {
			FileWriter fw = new FileWriter(StaticConstant.StaticRating, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(addString);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
