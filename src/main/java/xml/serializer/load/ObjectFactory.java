/*
 * ObjectFactory.java July 2006
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

/**
 * The <code>ObjectFactory</code> is the most basic factory. This will
 * basically check to see if there is an override type within the DOM
 * node provided, if there is then that is instantiated, otherwise the
 * field type is instantiated. Any type created must have a default
 * no argument constructor. If the override type is an abstract class
 * or an interface then this factory throws an exception.
 *  
 */
final class ObjectFactory extends Factory {

   /**
    * Constructor for the <code>ObjectFactory</code> class. This is
    * given the field class that this should create object instances
    * of. If the field type is abstract then the DOM node must have
    * the <code>class</code> attribute to specify the override.
    * 
    * @param field this is the field type of the object
    */
   public ObjectFactory(Class field) {
      super(field);           
   }        

   /**
    * This method will instantiate an object of the field type, or if
    * there is a <code>class</code> attribute in the DOM element, an
    * object of the override type. If the resulting type is abstract
    * or an interface then this method will throw an exception.
    * 
    * @param node this is the node to check for the override
    * 
    * @return this returns an instance of the resulting type
    */         
   public Object getInstance(Node node) throws Exception {
      Class type = getOverride(node);
    
      if(type == null) { 
         type = field;                           
      } 
      if(!isInstantiable(type)) {
         throw new InstantiationException("Cannot instantiate " + field);
      }
      return type.newInstance();
   }        
}
