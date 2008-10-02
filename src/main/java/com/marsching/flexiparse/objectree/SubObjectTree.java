/* 
 * fleXiParse - Copyright 2008 Sebastian Marsching
 * 
 * This file is part of fleXiParse.
 * 
 * fleXiParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * fleXiParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with fleXiParse.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.marsching.flexiparse.objectree;

/**
 * Interface implemented by objects that provide a subtree of 
 * {@link ObjectTreeElement}s. If an object attached to a 
 * {@link ObjectTreeElement} implements this interface, the
 * {@link ObjectTreeElement#getObjectsOfTypeFromSubTree(Class)}
 * method will include the subtree provided by this object in 
 * its search.
 * 
 * @author Sebastian Marsching
 */
public interface SubObjectTree {
	
	/**
	 * Returns the root node of the subtree provided by this object.
	 * 
	 * @return Root node of subtree
	 */
	ObjectTreeElement getRoot();
	
}
