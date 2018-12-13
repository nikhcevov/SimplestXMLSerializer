/*
 * Visitor.java July 2006
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

/**
 * The <code>Visitor</code> object is used to track which fields within
 * an object have been visited by a converter. This object is nessecary
 * for processing <code>Composite</code> objects. In particular it is
 * nessecary to keep track of which required nodes have been visited 
 * and which have not, if a required not has not been visited then the
 * XML source does not match the XML class schema and serialization
 * must fail before processing any further. 
 */
final class Visitor {

   /**
    * Contains a map of all attributes present within the XML schema.
    */
   private LabelMap attributes;
   
   /**
    * Contains a mpa of all elements present within the XML schema.
    */
   private LabelMap elements;

   /**
    * Constructor for the <code>Visitor</code> object. This is used 
    * to wrap the element and attribute XML annotations scanned from
    * a class schema. The visitor tracks all fields visited so that
    * a converter can determine if all fields have been serialized.
    * 
    * @param schema this contains all labels scanned from the class
    */
   public Visitor(Schema schema) {
      this.attributes = schema.getAttributes();
      this.elements = schema.getElements();
   }
   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. Labels contained within
    * this map are used to convert primitive types only.
    * 
    * @return map with the details extracted from the schema class
    */ 
   public LabelMap getAttributes() {
      return attributes;
   }

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. 
    * 
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements() {
      return elements;
   }
}
