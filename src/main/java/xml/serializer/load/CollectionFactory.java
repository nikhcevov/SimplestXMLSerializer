/*
 * CollectionFactory.java July 2006
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

import org.w3c.dom.Node;
import xml.serializer.load.exceptions.InstantiationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * The <code>CollectionFactory</code> is used to create collection
 * instances that are compatible with the field type. This performs
 * resolution of the collection class by firstly checking the DOM
 * element for a <code>class</code> attribute. If the attribute 
 * exists its value acts as the fully qualified class name for the
 * collection to be instantiated.  
 * 
 */
final class CollectionFactory extends Factory {

   /**
    * Constructor for the <code>CollectionFactory</code> object. This
    * is given the field type as taken from the owning object. The
    * given type is used to determine the collection instance created.
    * 
    * @param field this is the class for the owning object
    */
   public CollectionFactory(Class field) {
      super(field);           
   }        

   /**
    * Creates the collection to use. If there is a <code>class</code>
    * attribute then this is used to represent the fully qualified 
    * class name of the collection type to be instantiated. If not 
    * and the type is abstract then the type is choosen from one of
    * the types within the Java Collections framework.
    * 
    * @param node this is the DOM node representing the list
    * 
    * @return this is the collection instantiated for the field
    */         
   public Collection getInstance(Node node) throws Exception {
      Class type = getOverride(node);
     
      if(type != null) {              
         return getInstance(type);                       
      }              
      if(!isInstantiable(field)) {
         field = getConversion(field);
      }
      if(isCollection(field)) {
         throw new InstantiationException("Type is not a collection " + field);
      }
      return (Collection)field.newInstance();
   }

   /**
    * This creates a <code>Collection</code> instance from the class
    * provided. If the type provided is abstract or an interface then
    * a suitable match is searched from within the Java Collections
    * framework. If the type is concrete and a collection then its
    * default no argument constructor is used to create the instance.
    *
    * @param type the type used to instantiate the collection
    *
    * @return this returns a compatible collection instance
    *
    * @throws Exception if the collection cannot be instantiated
    */
   public Collection getInstance(Class type) throws Exception {
      if(!isInstantiable(type)) {
         throw new InstantiationException("Could not instantiate class " + type);
      }
      if(isCollection(type)) {
         throw new InstantiationException("Type is not a collection " + type);
      }
      return (Collection)type.newInstance();
   }

   /**
    * This is used to convert the provided type to a collection type
    * from the Java Collections framework. This will check to see if
    * the type is a <code>List</code> or <code>Set</code> and return
    * an <code>ArrayList</code> or <code>HashSet</code> type. If no
    * suitable match can be found this throws an exception.
    *
    * @param type this is the type that is to be converted
    *
    * @return a collection that is assignable to the provided type
    */
   private Class getConversion(Class type) throws Exception {
      if(type.isAssignableFrom(ArrayList.class)) {
         return ArrayList.class;
      }
      if(type.isAssignableFrom(HashSet.class)) {
         return HashSet.class;
      }
      if(type.isAssignableFrom(TreeSet.class)) {
         return TreeSet.class;
      }
      throw new InstantiationException("Cannot instantiate " + type);
   }

   /**
    * This determines whether the type provided is a collection type.
    * If the type is assignable to a <code>Collection</code> then 
    * this returns true, otherwise this returns false.
    * 
    * @param type given to determine whether it is a collection  
    * 
    * @return true if the provided type is a collection type
    */
   private boolean isCollection(Class type) {
      return !Collection.class.isAssignableFrom(type);
   }
}
