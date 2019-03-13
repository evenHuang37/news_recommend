package news;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class RandomUtils {

	public static int[] createNum() {
		System.out.println("ffffffff");
		Random rnd = new Random();
		int[] nums = new int[200];
		for (int i = 1; i < 1618; i++) {
			int p = rnd.nextInt(200);
			if (nums[p] != 0)
				i--;
			else
				nums[p] = i;
		}
		return nums;

	}

	public static void main(String[] args) {
		/*
		 * int[] nums =createNum(); for(int rnum: nums){
		 * System.out.println(rnum); } System.out.println("ffffffff");
		 */
		createRandomNum();
	}

	public static int[] createRandomNum() {
		int[] intRet = new int[200];
		int intRd = 0; // 存放随机数
		int count = 0; // 记录生成的随机数个数
		int flag = 0; // 是否已经生成过标志
		while (count < 200) {
			Random rdm = new Random(System.currentTimeMillis());
			intRd = Math.abs(rdm.nextInt()) % 1681 + 1;
			for (int i = 0; i < count; i++) {
				if (intRet[i] == intRd) {
					flag = 1;
					break;
				} else {
					flag = 0;
				}
			}
			if (flag == 0) {
				intRet[count] = intRd;
				count++;
			}
		}
		for (int t = 0; t < 200; t++) {
			System.out.println(t + "->" + intRet[t]);
		}
		return intRet;
	}

}
