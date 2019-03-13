package news;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.HashMap;


/**
 *
 *
 **/
//”√”⁄Ω‚Œˆxml
public class ContentHandler extends DefaultHandler {
  private boolean inNewId;
  private boolean inNewTitle;
  StringBuilder builder = new StringBuilder();
  private String itemId;
  private String NewTitle;
  Map<String, String> map = new HashMap<String, String>();

  
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("str") && attributes.getValue("name") != null && attributes.getValue("name").equals("Bookid")) {
      inNewId = true;
    } else if (qName.equals("arr") && attributes.getValue("name") != null && attributes.getValue("name").equals("Booktitle")) {
      inNewTitle = true;
    }

  }

  @Override
  public void characters(char[] chars, int offset, int len) throws SAXException {
    if (inNewId == true || inNewTitle == true) {
      builder.append(chars, offset, len);
    }
  }

  @Override
  public void endElement(String uri, String local, String qName) throws SAXException {
    if (inNewId == true) {
      itemId = builder.toString();
      inNewId = false;
    } else if (inNewTitle == true) {
      NewTitle = builder.toString();
      inNewTitle = false;
    }
    if (qName.equals("Book")) {
   System.out.println("Adding: " + itemId + " title: " + NewTitle);
      map.put(itemId, NewTitle);
    }
    builder.setLength(0);
  }
}
