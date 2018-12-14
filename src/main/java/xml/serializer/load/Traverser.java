/*
 * Traverser.java July 2006
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.serializer.load.exceptions.RootException;
import xml.serializer.Root;

/**
 * The <code>Traverser</code> object is used to traverse the XML class
 * schema and either serialize or deserialize an object. This is the
 * root of all serialization and deserialization operations. It uses
 * the <code>Root</code> annotation to ensure that the XML schema
 * matches the provided DOM element. If no root element is defined the
 * serialization and deserialization cannot be performed.
 */
final class Traverser {

   /**
    * This is the root object used for the traversal performed.
    */
   private Source root;
        
   /**
    * Constructor for the <code>Traverser</code> object. This creates
    * a traverser that can be used to perform serialization or
    * or deserialization of an object. This requires a source object.
    * 
    * @param root the source object used for the traversal
    */
   public Traverser(Source root) {
      this.root = root;           
   }

   /**
    * Constructor for the <code>Traverser</code> object. This creates
    * a traverser using the supplied document object. This
    * is a convinience constructor that creates the source object.
    *
    * @param root this is the root DOM document object used
    */
   public Traverser(Document root) {
      this(new Source(root));
   }

   /**
    * This <code>read</code> method is used to deserialize an object 
    * from the provided DOM element. The class provided acts as the
    * XML schema definition used to control the deserialization. If
    * the XML schema does not have a <code>Root</code> annotation 
    * this throws an exception. Also if the root annotation name is
    * not the same as the DOM element name an exception is thrown.  
    * 
    * @param node this is the node that is to be deserialized
    * @param type this is the XML schema class to be used
    * 
    * @return an object deserialized from the DOM element 
    * 
    * @throws Exception if the XML schema does not match the DOM
    */
   public Object read(Node node, Class type) throws Exception {
      Root label = getRoot(type);

      if(label == null) {
         throw new RootException("No root annotation defined for %s", type.getName());
      }      
      String name = node.getNodeName();
      
      if(!label.name().equals(name)) {
         throw new RootException("Annotation %s does not match XML element %s", label, name);              
      } 
      return getComposite(type).read(node);       
   }

   /**
    * This <code>write</code> method is used to convert the provided
    * object to a DOM element. This creates an <code>Element</code> 
    * from the <code>Source</code> object provided. Once this root
    * DOM element is created it is populated with the fields of the
    * source object in accordance with the XML schema class.  
    * 
    * @param source this is the object to be serialized to a DOM
    * 
    * @return returns a DOM element representing the object
    * 
    * @throws Exception thrown if there is a problem serializing
    */
   public Element write(Object source) throws Exception {
      Class type = source.getClass();
      Root label = getRoot(type);

      if(label == null) {
         throw new RootException("No root annotation defined for %s", type.getName());
      }
      return write(source, label.name());
   }
   
   /**
    * This <code>write</code> method is used to convert the provided
    * object to a DOM element. This creates an <code>Element</code> 
    * from the <code>Source</code> object provided. Once this root
    * DOM element is created it is populated with the fields of the
    * source object in accordance with the XML schema class.  
    * 
    * @param source this is the object to be serialized to a DOM
    * @param name this is the name of the root element 
    * 
    * @return returns a DOM element representing the object
    * 
    * @throws Exception thrown if there is a problem serializing
    */
   private Element write(Object source, String name) throws Exception {
      Element node = root.getElement(name);      
      Class type = source.getClass();
      
      getComposite(type).write(source, node);
      return node;
   }
   
   /**
    * This will create a <code>Composite</code> object using the XML 
    * schema class provided. This makes use of the source object that
    * this traverser has been given to create a composite converter. 
    * 
    * @param type this is the XML schema class to be used
    * 
    * @return a converter for the specified XML schema class
    */
   private Composite getComposite(Class type) {
      return new Composite(root, type);
   }
   
   /**
    * Extracts the <code>Root</code> annotation from the provided XML
    * schema class. If no annotation exists in the provided class the
    * super class is checked and so on until the <code>Object</code>
    * is encountered, if no annotation is found this returns null.
    *  
    * @param type this is the XML schema class to use
    * 
    * @return this returns the root annotation for the XML schema
    */
   private Root getRoot(Class type) throws Exception {
      while(type != null){
         if(type.isAnnotationPresent(Root.class)) {
            return (Root)type.getAnnotation(Root.class);
         }
         type = type.getSuperclass();
      }
      return null;
   }
}
