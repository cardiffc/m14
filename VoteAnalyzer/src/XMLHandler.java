import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.SQLException;

public class XMLHandler extends DefaultHandler {
    StringBuilder query;
    private int iteration = 0;
    public XMLHandler() {
        query = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("voter")) {

            try {
                String name = attributes.getValue("name");
                String bd = attributes.getValue("birthDay");
                query.append((query.length() == 0 ? "":",") + "('" + name + "','" + bd + "', 1)");
                /** Вот тут эмперически установлен размер буфера. Дальше наращивать нет смысла, т.к. не дает прироста
                 * производительности. При этом мы приближаемся к максимальной длинне запроса в MySQL (это, конечно, можно
                 * потюнить, но задача то не про это. Для понимания также печатаем количество итераций "сброса" буфера
                 */
                if (query.length() > 20 * 1024 * 1024) {
                    iteration++;
                    System.out.println(iteration);
                    DBConnection.executeMultiInsert(query);
                    query = new StringBuilder();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
     }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("voter")) {
        }
    }


    public void writeBuffer () throws SQLException {
        DBConnection.executeMultiInsert(query);
        query = new StringBuilder();
        System.out.println(query);

    }

}
