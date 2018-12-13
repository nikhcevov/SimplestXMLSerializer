/*
 * SchemaCache.java July 2006
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

import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>SchemaCache</code> is used to cache schema objects so 
 * that the overhead of reflectively interrogating each class is not
 * required each time an object of the class type is serialized or
 * deserialized. This is esentially a typedef for the generic type.
 * 
 */
final class SchemaCache extends ConcurrentHashMap<Class, Schema> {

   /**
    * Constructor for the <code>SchemaCache</code> object. This is
    * a concurrent hash map that maps class types to the XML schema
    * objects they represent. To ensure the cache can be used by
    * multiple threads this extends the concurrent hash map.
    */
   public SchemaCache() {
      super();
   }
}
