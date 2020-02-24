import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class XMLHandler extends DefaultHandler {
    Map<Voter,Integer> voters;
    Voter voter = null;
    StringBuilder query;
    int bufferSize = 1_100_000;
    public XMLHandler() {
        voters = new TreeMap();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (voters.size() >= bufferSize) {
            writeToDb();
            voters.clear();
        }
        if (qName.equals("voter") && voter == null) {
            voter = new Voter(attributes.getValue("name"), attributes.getValue("birthDay"));
        }
        if (qName.equals("visit") && voter != null) {
            int count = voters.getOrDefault(voter, 0);
            count++;
            voters.put(voter, count);

        }
    }

    public void writeToDb () {
        try {
            query = new StringBuilder();
            for (Voter voter : voters.keySet()) {
                String name = voter.getName();
                String birthDate = voter.getBirthDay();
                int count = voters.get(voter);
                query.append((query.length() == 0 ? "" : ",") + "('" + name + "','" + birthDate + "'," + count + ")");
            }
            DBConnection.executeMultiInsert(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("voter")) {
            voter = null;
        }
    }
}
