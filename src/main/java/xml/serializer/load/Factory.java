/*
 * Factory.java July 2006
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
import xml.serializer.load.exceptions.InstantiationException;

import java.lang.reflect.Modifier;

/**
 * The <code>Factory</code> object provides a base class for factories 
 * used to produce field values from DOM elements. The goal of this 
 * type of factory is to make use of a <code>class</code> attribute to 
 * determine the type of the field value. The attributes must be 
 * assignable to the field class type, that is, it must extend it or
 * implement it if it represents an interface. If the attribute does
 * not exist then the subclass implementation determines the type.
 * 
 */
abstract class Factory {
   
   /**   
    * This is the attribute that is used to determine the real type.
    */
   private static final String TYPE = "class";

   /**
    * This is the field type that the class must be assignable to.
    */
   protected Class field;        

   /**
    * Constructor for the <code>Factory</code> object. This is given 
    * the class type for the field that this factory will determine
    * the actual type for. The actual type must be assignable to the
    * field type to insure that any instance can be set. 
    * 
    * @param field this is the field type to determine the value for
    */
   protected Factory(Class field) {
      this.field = field;           
   }

   /**
    * This is used to get a possible override from the provided node.
    * If the node provided is an element then this checks for a  
    * specific attribute is searched for within the element. If this
    * attribute is present then it is used as the override type.
    * 
    * @param node this is the node used to search for the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception if the override type is not compatible
    */
   public Class getOverride(Node node) throws Exception {
      return getOverride((Element)node);           
   }

   /**
    * This is used to get a possible override from the provided node.
    * If the element provided has a <code>class</code> attribute then
    * that attributes value is used to determine the override type
    * for the field. If the override type is not assignable then this
    * will throw ann exception. 
    * 
    * @param node this is the element used to extract the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception if the override type is not compatible
    */ 
   public Class getOverride(Element node) throws Exception {
      Class type = getConversion(node);

      if(type != null) { 
         if(!isCompatible(field, type)) {
            throw new InstantiationException("Type "+ type + " is not compatible with " + field);
         }
      }         
      return type; 
   }
   
   /**
    * This method is used to set the override class within an element.
    * Implementations of this method can choose to add either a special
    * attribute or a child element to describe the type that should
    * be used as the override for the deserialized field.
    * 
    * @param type this is the class of the field type being serialized
    * @param node the DOM element that is to be given the details
    */
   public void setOverride(Class type, Element node) throws Exception {
      node.setAttribute(TYPE, type.getName());
   }

   /**
    * This performs the conversion from the element node to a type. If
    * there is a <code>class</code> attribute present in the element
    * then that class is loaded from the context class loader.
    * <pre>
    * 
    *    &lt;lement class="demo.Example"&lt;
    *       &lt;property&gt;test&lt;/property&gt;
    *    &lt;/element&gt;
    *    
    * </pre>
    * For example the attribute value <code>demo.Example</code> would 
    * be used as the converted type. This class would be loaded from
    * the context class loader using <code>Class.forName</code>.
    * 
    * @param node this is the element used to extract the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception thrown if the override class cannot be loaded    
    */ 
   public Class getConversion(Element node) throws Exception {
      String name = node.getAttribute(TYPE);
   
      if(name != null && name.length() > 0) {
         node.removeAttribute(TYPE);              
         return Class.forName(name);              
      }      
      return null;
   }

   /**
    * This is used to determine whether the provided base class can be
    * assigned from the issued type. For an override to be compatible
    * with the field type an instance of the override type must be 
    * assignable to the field value. 
    * 
    * @param field this is the field value present the the object    
    * @param type this is the specialized type that will be assigned
    * 
    * @return true if the field type can be assigned the type value
    */
   public boolean isCompatible(Class field, Class type) {
      return field.isAssignableFrom(type);           
   }

   /**
    * This is used to determine whether the type given is instantiable,
    * that is, this determines if an instance of that type can be
    * created. If the type is an interface or an abstract class then 
    * this will return false.
    * 
    * @param type this is the type to check the modifiers of
    * 
    * @return false if the type is an interface or an abstract class
    */
   public boolean isInstantiable(Class type) {
      int modifiers = type.getModifiers();

      if(Modifier.isAbstract(modifiers)) {
         return false;              
      }              
      return !Modifier.isInterface(modifiers);
   }      
}
