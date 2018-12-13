/*
 * Generator.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package xml.serializer.load;

import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;

/**
 * The <code>Formatter</code> object is used generate the XML from a
 * DOM document object. The generated XML is written to a provided
 * <code>Result</code> object, which in turn may transfer the content
 * to an output stream or file. This is used to write the serialized
 * object to text based XML from the DOM object level representation. 
 * 
 */
final class Formatter {

   /**
    * The transformer factory that is used to create transformers.
    */
   private static TransformerFactory factory;
  
   static {
      factory = TransformerFactory.newInstance(); 
   }

   /**
    * This is the transformer used to generate the result tree. 
    */
	private Transformer transformer;

   /**
    * This is the result object the XML tree is generated in to.
    */
   private Result result;

   /**
    * Determines whether the resulting XML should be formatted.
    */
   private boolean format;

   /**
    * Constructor for the the <code>Formatter</code> object. This is
    * given the result object that is used to generate the XML source.
    * The instance created constructor generates unformatted XML.
    * 
    * @param result the result to generate the XML source to
    * 
    * @throws Exception thrown if a transformer could not be created
    */
   public Formatter(Result result) throws Exception {
      this(result, false);
   }
   
   /**
    * Constructor for the the <code>Formatter</code> object. This is
    * given the result object that is used to generate the XML source.
    * A boolean parameter can be provided to specify whether this
    * formatter will format the resulting XML source.
    * 
    * @param result the result to generate the XML source to
    * @param format if true the resulting XML source is formatted
    * 
    * @throws Exception thrown if a transformer could not be created
    */
   public Formatter(Result result, boolean format) throws Exception {
      this.transformer = factory.newTransformer();
      this.result = result;
           
   }
   
   /**
    * This <code>write</code> method will transfer the provided DOM
    * document to the result object. If formatting is turned on this
    * will indent the resulting XML source text generated. 
    * 
    * @param source this is the DOM source for the XML 
    * 
    * @throws Exception thrown if the XML could not be generated
    */
   public void write(Document source) throws Exception {
	   write(new DOMSource(source)); 
   }	      
   
   /**
    * This <code>write</code> method will transfer the provided DOM
    * document to the result object. If formatting is turned on this
    * will indent the resulting XML source text generated. 
    * 
    * @param source this is the DOM source for the XML 
    * 
    * @throws Exception thrown if the XML could not be generated
    */
   private void write(Source source) throws Exception {
      if(format) {
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      }
      transformer.transform(source, result);
   }
}
