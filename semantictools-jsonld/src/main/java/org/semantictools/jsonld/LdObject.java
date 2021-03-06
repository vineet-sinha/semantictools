/*******************************************************************************
 * Copyright 2012 Pearson Education
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.semantictools.jsonld;

import java.util.Iterator;

public interface LdObject extends LdNode {
  
  void setContext(LdContext context);
  
  /**
   * Returns the JSON-LD context that governs this object.
   */
  LdContext getContext();

  /**
   * Returns a non-null identifier for this node.
   * This value is identical to the return value of either {@link #getIRI()} or {@link #getIdentifier()},
   * whichever one is not null.
   */
  String getId();
  
  /**
   * Returns the fully qualified (i.e. "expanded") IRI for the resource, or null if the IRI is not defined.
   * This method returns null if the object has no IRI or if it represents a blank node.
   */
  String getIRI();
  
  /**
   * Returns the &#064;id property for this object if it is a blank node, and null otherwise.
   * This identifier may be generated by the JSON-LD processor during parsing if the JSON-LD
   * document does not contain an identifier (for an embedded object).  Otherwise, this
   * value will be identical to the value returned by {@link #getRawId()} for a blank node.
   */
  String getIdentifier();
  
  /**
   * Returns the exact value of the  &#064;id property within the JSON-LD payload.
   */
  String getRawId();
  
  /**
   * Returns the fully qualified IRI for the type of this resource, or null if the type is not declared.
   */
  String getTypeIRI();
  
  /**
   * Set the fully-qualified IRI for the type of this resource.
   */
  void setTypeIRI(String typeIRI);
  
  /**
   * Returns the raw (unexpanded) type value for this resource, or null if the type is not declared.
   */
  String getRawType();
  
  /**
   * Returns an iterator for the fields in this object.
   */
  Iterator<LdField> fields();
  
  /**
   * Returns the field that "owns" this object, or null if this object is 
   * the top-level node in the JSON-LD document.
   * If this object is an element within an array, then the field will be
   * the field that contains the array.
   */
  LdField owner();
  
  
}
