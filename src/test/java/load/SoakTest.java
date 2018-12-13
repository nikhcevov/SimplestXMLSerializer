package load;

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import xml.serializer.Attribute;
import xml.serializer.Element;
import xml.serializer.ElementList;
import xml.serializer.Root;
import xml.serializer.load.Persister;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Collection;

public class SoakTest extends TestCase {
        
   private static DocumentBuilderFactory factory;

   static {
      factory = DocumentBuilderFactory.newInstance();
   }

   private DocumentBuilder builder;        
        
   private static final String ENTRY =
   "<?xml version=\"1.0\"?>\n"+
   "<root number='1234' flag='true'>\n"+
   "   <name>${example.name}</name>  \n\r"+
   "   <path>${example.path}</path>\n"+
   "   <constant>${no.override}</constant>\n"+
   "   <text>\n"+
   "     Some example text where ${example.name} is replaced"+
   "     with the system property value and the path is "+
   "     replaced with the path ${example.path}"+
   "   </text>\n"+
   "   <list>\n"+
   "     <entry key='name.1'>\n"+
   "        <value>value.1</value>\n"+
   "     </entry>\n"+
   "     <entry key='name.2'>\n"+
   "        <value>value.2</value>\n"+
   "     </entry>\n"+
   "     <entry key='name.3'>\n"+
   "        <value>value.4</value>\n"+
   "     </entry>\n"+
   "     <entry key='name.4'>\n"+
   "        <value>value.4</value>\n"+
   "     </entry>\n"+
   "     <entry key='name.5'>\n"+
   "        <value>value.5</value>\n"+
   "     </entry>\n"+
   "  </list>\n"+ 
   "</root>";
   
   @Root(name="root")
   public static class RootEntry {

      @Attribute(name="number")
      private int number;     

      @Attribute(name="flag")
      private boolean bool;
      
      @Element(name="constant")
      private String constant;

      @Element(name="name")
      private String name;

      @Element(name="path")
      private String path;

      @Element(name="text")
      private String text;

      @ElementList(name="list", type=ElementEntry.class)
      private Collection list;              
   }

   @Root(name="entry")
   public static class ElementEntry {

      @Attribute(name="key")
      private String name;

      @Element(name="value")
      private String value;                 
   }

   static {
      System.setProperty("example.name", "some name");
      System.setProperty("example.path", "/some/path");
      System.setProperty("no.override", "some constant");
   } 
        
   private Persister systemSerializer;

   public void setUp() throws Exception {
      systemSerializer = new Persister();
      builder = factory.newDocumentBuilder();
   }
	
   public void testSystem() throws Exception {    
      Document doc = builder.parse(new InputSource(new StringReader(ENTRY)));
      long start = System.currentTimeMillis();

      for(int i = 0; i < 10000; i++) {
         systemSerializer.read(RootEntry.class, doc);
      }
      System.err.println("TIME ["+(System.currentTimeMillis() - start)+"] FOR 10,000 ITERATIONS");      
   }
}
