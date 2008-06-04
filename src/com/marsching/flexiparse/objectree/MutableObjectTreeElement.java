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
 * {@link ObjectTreeElement} that can be modified. Child nodes may
 * be added to or deleted from this node.
 * 
 * @author Sebastian Marsching
 *
 */
public interface MutableObjectTreeElement extends ObjectTreeElement {

	/**
	 * Registers another node as a child node of this node.
	 * For each child node, this method should only be called once.
	 * This node should be registered as the parent node of the 
	 * node being supplied by the calling code.
	 * 
	 * @param child node to be attached as a child.
	 */
	public void addChild(MutableObjectTreeElement child);
	
	/**
	 * Removes the specified child from this node.
	 * 
	 * @param child node to remove
	 * @return <code>true</code> if child was attached to this
	 * node, <code>false</code> otherwise
	 */
	public boolean removeChild(MutableObjectTreeElement child);
	
	/**
	 * Returns the parent of this node.
	 * 
	 * @return parent of this node or <code>null</code> if this is
	 * the root node
	 */
	public MutableObjectTreeElement getParent();
	
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
	public Collection<? extends MutableObjectTreeElement> getChildren();
	
}
