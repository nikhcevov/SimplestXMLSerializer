/*
 * Schema.java July 2006
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

import xml.serializer.Attribute;
import xml.serializer.Element;
import xml.serializer.ElementList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * The <code>Schema</code> object performs the reflective inspection
 * of a class and builds a map of attributes and elements for each
 * annotated field. The schema acts as a cachable store for reflection
 * actions performed on a specific type. When scanning the provided
 * class this inserts the scanned field as a <code>Label</code> in to
 * a map so that it can be retrieved by name. Annotations classified
 * as attributes have the <code>Attribute</code> annotation, all other
 * annotated fields are stored as elements.
 *
 *
 * @see Visitor
 */
final class Schema {

   /**
    * This is used to store all labels that are XML attributes.
    */
   private LabelMap attributes;
   
   /**
    * This is used to store all labels that are XML elements.
    */
   private LabelMap elements;
      
   /**
    * Constructor for the <code>Schema</code> object. This is used
    * to scan the provided class for annotations that are used to
    * build a schema for an XML file to follow.
    *
    * @param type this is the type that is scanned for a schema
    */
   public Schema(Class type) {           
      this.attributes = new LabelMap();
      this.elements = new LabelMap();
      this.scan(type);
   }       

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. This returns a new map
    * each time the method is called, the goal is to ensure that any
    * object using the label map can manipulate it without changing
    * the core details of the schema, allowing it to be cached.
    *
    * @return map with the details extracted from the schema class
    */
   public LabelMap getAttributes() {
      return new LabelMap(attributes);
   }        

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. This returns a copy of the
    * details extracted from the schema class so this can be cached.
    *
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements() {
      return new LabelMap(elements);
   }

   /**
    * Scan the fields such that the base class is scanned first then
    * all super classes up to the base class <code>Object</code>. All
    * fields from base classes override fields from higher up the
    * inheritance heirarchy. This means that a field annotation can
    * be overridden an may not have values assigned to them.
    *
    * @param type the class to extract fields and annotations from
    */
   private void scan(Class type) {
      Class real = type;
      
      do {
         scan(real, type);
         type = type.getSuperclass();
      }
      while(type != null);    
   }

   /**
    * Scans the provided class for all fields in order to extract any
    * annotations from those fields. Each field extracted from the
    * class is checked for annotations that relate to the XML schema.
    *
    * @param real this is the class the acts as the XML schema
    * @param type the current class in the schema heirarchy to check
    */
   private void scan(Class real, Class type) {
      Field[] list = type.getDeclaredFields();
      
      for(int i = 0; i < list.length; i++) {                       
         scan(list[i]);                      
      }
   }

   /**
    * Scans the provided field for all annotations in order to create
    * a <code>Label</code> to represent that object. Only annotations
    * that relate to the XML schema are considered within the field.
    *
    * @param field the field to scan for XML schema annotations
    */
   private void scan(Field field) {      
      Annotation[] list = field.getDeclaredAnnotations();
      
      for(int i = 0; i < list.length; i++) {
         scan(field, list[i]);                       
      }        
   }

   /**
    * This reflectively checks the annotation to determine the type
    * of annotation it represents. If it represents an XML schema
    * annotation it is used to create a <code>Label</code> which can
    * be used to represent the field within the source object.
    *
    * @param field the field that the annotation comes from
    * @param label the annotation used to model the XML schema
    */
   private void scan(Field field, Annotation label) {
      if(label instanceof Attribute) {
         process(field, label, attributes);
      }
      if(label instanceof ElementList) {
         process(field, label, elements);
      }
      if(label instanceof Element) {
         process(field, label, elements);
      }             
   }
   
   /**
    * This is used when all details from a field have been gathered
    * and a <code>Label</code> implementation needs to be created.
    * This will build a label instance based on the field annotation.
    * If a label with the same name was already inserted then it is
    * ignored and the value for that field will not be serialized.
    *
    * @param field the field the annotation was extracted from
    * @param type the annotation extracted from the field
    * @param map this is used to collect the label instance created
    */
   private void process(Field field, Annotation type, Map map) {
      Label label = LabelFactory.getInstance(field, type);
      String name = label.getName();
      
      if(!map.containsKey(name)) { 
         map.put(name, label);
      }  
   }
}
