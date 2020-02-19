import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XMLHandler extends DefaultHandler {
    Voter voter;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private Map<Voter,Integer> voterCounts;
    public XMLHandler() {
        voterCounts = new TreeMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter") && voter == null) {
                Date bithDay = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), bithDay);
            }
            if (qName.equals("visit") && voter != null) {
                int count = voterCounts.getOrDefault(voter,0);
                voterCounts.put(voter, count + 1);
            }
        } catch (ParseException e) {
            e.getMessage();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("voter")) {
            voter = null;
        }
    }
    public void getDuplicatedVoters () {
        for (Voter voter : voterCounts.keySet()) {
            int count = voterCounts.get(voter);
            if (count  > 1) {
                System.out.println(voter.toString() + "-" + count);
            }
        }
    }

}
