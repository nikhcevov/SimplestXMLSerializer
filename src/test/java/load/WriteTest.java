package load;

import junit.framework.TestCase;
import org.junit.Before;
import xml.serializer.Attribute;
import xml.serializer.Element;
import xml.serializer.Root;
import xml.serializer.load.Persister;

import java.io.StringWriter;


public class WriteTest extends TestCase {


    public static final String PERSON =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<person country=\"RUS\">\n" +
            "    <fullname>Sergey</fullname>\n" +
            "    <age class=\"java.lang.Integer\">32</age>\n" +
            "    <info relevance=\"true\">\n" +
            "        <phone>1488</phone>\n" +
            "        <married class=\"java.lang.Boolean\">true</married>\n" +
            "        <child class=\"java.lang.Integer\">10</child>\n" +
            "    </info>\n" +
            "</person>";

    @Root(name = "person")
    public class Person {

        @Attribute(name = "country")
        public String country;

        @Element(name = "fullname")
        private String name;

        @Element(name = "age")
        private int age;

        @Element(name = "info")
        public Info info;

        public Person(String country, String name, int age, Info info) {
            this.country = country;
            this.name = name;
            this.age = age;
            this.info = info;
        }
    }

    public class Info {

        @Attribute(name = "relevance")
        public boolean isRelevant;

        @Element(name = "phone")
        public String phone;

        @Element(name = "married")
        public boolean isMarried;

        @Element(name = "child")
        public int child;

        public Info(boolean isRelevant, String phone, boolean isMarried, int child) {
            this.isRelevant = isRelevant;
            this.phone = phone;
            this.isMarried = isMarried;
            this.child = child;
        }
    }


    private Persister serializer;

    @Before
    public void setUp() {
        serializer = new Persister();
    }

    public void testWrite() throws Exception {
        Person person = new Person("RUS", "Sergey", 32,
                new Info(true, "1488", true, 10));

        StringWriter writer = new StringWriter();
        serializer.write(person, writer);
        serializer.write(person, System.out);

        assertTrue(!writer.toString().isEmpty());
    }
}
