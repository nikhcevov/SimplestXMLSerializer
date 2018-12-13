/*
 * Primitive.java July 2006
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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The <code>Primitive</code> object is used to provide serialization
 * for primitive objects. This can serialize and deserialize any
 * primitive object and enumerations. Primitive values are converted
 * to text using the <code>String.valueOf</code> method. Enumerated
 * types are converted using the <code>Enum.valueOf</code> method.
 * <p>
 *
 */
final class Primitive implements Converter {

   /**
    * This is used to convert the string values to primitives.
    */
   private PrimitiveFactory factory;

    /**
    * Constructor for the <code>Primitive</code> object. This is used
    * to convert a DOM node to a primitive object and vice versa. To
    * perform deserialization the primitive object requires the source
    * object used for the instance of serialization to peformed.
    *
    * @param root the source object used for the serialization
    * @param type this is the type of primitive this represents
    */
   public Primitive(Source root, Class type) {
      this.factory = new PrimitiveFactory(type);
   }

   /**
    * This <code>read</code> methos will extract the text value from
    * the node and converting it to a primitive value. This uses
    * the <code>Source</code> object used for this instance of
    * serialization.
    *
    * @param node this is the node to be converted to a primitive
    *
    * @return this returns the primitive that has been deserialized
    */
   public Object read(Node node) throws Exception{
      Node child = node.getFirstChild();
      String text = child.getNodeValue();

      return factory.getInstance(text);
   }

   /**
    * This <code>write</code> method will serialize the contents of
    * the provided object to the given DOM element. This will use
    * the <code>String.valueOf</code> method to convert the object to
    * a string if the object represents a primitive, if however the
    * object represents an enumerated type then the text value is
    * created using <code>Enum.name</code>.
    *
    * @param source this is the object to be serialized
    * @param node this is the DOM element to have its text set
    */
   public void write(Object source, Element node) throws Exception {
      Class type = source.getClass();
      String text = getText(source, type);

      node.setTextContent(text);
   }

   /**
    * This method is used to convert the provided object to a text
    * value so that it can be written to a DOM element. This will
    * check the object type to convert enumerations using the
    * <code>Enum.name</code> and primitives to their string values.
    *
    * @param source this is the primitive object to be converted
    * @param type the type of the object, checks for enumerations
    *
    * @return this returns the text value for the primitive object
    */
   private String getText(Object source, Class type) {
      if(!type.isEnum()) {
         return String.valueOf(source);
      }
      Enum value = (Enum)source;
      return value.name();
   }
}
