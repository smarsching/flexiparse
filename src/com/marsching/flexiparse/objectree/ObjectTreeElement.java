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

import java.util.Collection;

/**
 * Represents a node in an object tree. Each node can have child 
 * nodes and attached objects. Usually a node is created for 
 * every element in the origin XML document.
 * 
 * @author Sebastian Marsching
 */
public interface ObjectTreeElement {
	
	/**
	 * Returns the parent of this node.
	 * 
	 * @return parent of this node or <code>null</code> if this is
	 * the root node
	 */
	public ObjectTreeElement getParent();
	
	/**
	 * Returns the root node of the tree this node belongs to.
	 * 
	 * @return root node of the tree
	 */
	public ObjectTreeElement getRoot();
	
	/**
	 * Returns all children of this node.
	 * 
	 * @return a list containing all child nodes of this node
	 */
	public Collection<? extends ObjectTreeElement> getChildren();
	
	/**
	 * Returns objects attached directly to this node.
	 * 
	 * @return Objects attached to this node
	 */
	public Collection<Object> getObjects();
	
	/**
	 * Returns objects of specified type attached directly to
	 * this node.
	 * 
	 * @param <T> type of wanted objects
	 * @param type object type that is returned
	 * @return objects of specified type attached to this node
	 */
	public <T> Collection<T> getObjectsOfType(Class<? extends T> type);
	
	/**
	 * Returns objects attached to this node and all parent nodes
	 * up to the root node of the tree.
	 * 
	 * @param <T> type of wanted objects
	 * @param type object type that is returned
	 * @return objects of specified type attached to this node
	 *   or one of its parents nodes
	 */
	public <T> Collection<T> getObjectsOfTypeFromTopTree(Class<? extends T> type);
	
	/**
	 * Returns objects attached to this node and all child nodes
	 * (recursively). Includes objects attached to sub trees 
	 * (see {@link SubObjectTree}).
	 * 
	 * @param <T> type of wanted objects
	 * @param type object type that is returned
	 * @return objects of specified type attached to this node
	 *   or one of its child nodes
	 */
	public <T> Collection<T> getObjectsOfTypeFromSubTree(Class<? extends T> type);
	
	/**
	 * Attaches the given object to this node.
	 * 
	 * @param object object to  attach
	 */
	public void addObject(Object object);
	
	/**
	 * Removes a object from this node.
	 * 
	 * @param object object to remove
	 * @return <code>true</code> if the specified object
	 * was removed from this node, <code>false</code> otherwise
	 */
	public boolean removeObject(Object object);
	
}
