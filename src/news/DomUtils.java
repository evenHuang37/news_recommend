package news;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class DomUtils {

	public static NewsBean findByID(String ID) {
		NewsBean news = new NewsBean();
		File f = new File(StaticConstant.BooksXML);
		SAXBuilder saxb = new SAXBuilder();
		try {
			Document xmldoc = saxb.build(f);
			XPath xpath = XPath.newInstance("//result/Book[str=" + ID + "]/arr");// ����һ����ѯ���Ϊ001��LINEԪ�ص�XPATH��
			List nodes = xpath.selectNodes(xmldoc);// ���в�ѯ
			System.out.println("findByID");
			System.out.println("���ؼ�¼����" + nodes.size());
			Element newsElement = (Element) nodes.get(0);
			String newtitle = newsElement.getChildText("str");
			newsElement = (Element) nodes.get(1);
			String newcontent = newsElement.getChildText("str");
			news.setInNewContent(newcontent);
			news.setInNewTitle(newtitle);
			news.setInNewId(ID);

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return news;
	}

	public static List<NewsBean> findALL() {
		List<NewsBean> listnews = new ArrayList<NewsBean>();
		File f = new File(StaticConstant.BooksXML);
		SAXBuilder saxb = new SAXBuilder();
		try {
			Document xmldoc = saxb.build(f);
			XPath xpath = XPath.newInstance("//result/Book");// ����һ����ѯ���Ϊ001��LINEԪ�ص�XPATH��
			List nodes = xpath.selectNodes(xmldoc);// ���в�ѯ
			System.out.println("findALL");
			System.out.println("���ؼ�¼����" + nodes.size());
			for (int i = 0; i < nodes.size(); i++) {
				NewsBean news = new NewsBean();
				Element newsElement = (Element) nodes.get(i);
				String newid = newsElement.getChildText("str");
				List<Element> arrElement = newsElement.getChildren("arr");
				String newtitle = arrElement.get(0).getChildText("str");
				String newcontent = arrElement.get(1).getChildText("str");
				news.setInNewContent(newcontent);
				news.setInNewTitle(newtitle);
				news.setInNewId(newid);
				listnews.add(news);
				//System.out.println(newid+"--"+newtitle+"--"+newcontent);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listnews;
	}
	public static void main(String[] args) throws IOException,
	FileNotFoundException, JDOMException {
		 findALL() ;
		
	}
	public static void main1(String[] args) throws IOException,
			FileNotFoundException, JDOMException {
		NewsBean news = new NewsBean();
		File f = new File(StaticConstant.BooksXML);
		SAXBuilder saxb = new SAXBuilder();
		try {
			Document xmldoc = saxb.build(f);
			XPath xpath = XPath.newInstance("//result/Book[str=1]/arr");// ����һ����ѯ���Ϊ001��LINEԪ�ص�XPATH��
			List nodes = xpath.selectNodes(xmldoc);// ���в�ѯ
			System.out.println("main1");
			System.out.println("���ؼ�¼����" + nodes.size());
			for (int i = 0; i < nodes.size(); i++) {
				Element newsElement = (Element) nodes.get(i);
				String newid = newsElement.getChildText("str");
				System.out.println(newsElement.getChildText("str"));

				if (newid != null && newid.equals("1")) {
					news.setInNewId(newid);
					newsElement.getChildText("str");
					break;
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
