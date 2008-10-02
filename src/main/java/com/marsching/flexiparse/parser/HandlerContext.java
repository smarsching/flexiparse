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

package com.marsching.flexiparse.parser;

import org.w3c.dom.Node;

import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.objectree.ObjectTreeElement;

/**
 * Provides context information to a {@link ParsingHandler}.
 * 
 * @author Sebastian Marsching
 *
 */
public interface HandlerContext {
	/**
	 * Returns current node. That is the node that was matched
	 * by the XPath expression.
	 * 
	 * @return context XML node
	 */	
	public Node getNode();
	
	/**
	 * Returns the current object tree node. This is the node that
	 * is associated with the current XML node, if the node is of
	 * element type, and the parent element of the current node
	 * otherwise.
	 * 
	 * @return context object tree node
	 */
	public ObjectTreeElement getObjectTreeElement();
	
	/**
	 * Returns the current processing state (child nodes not yet
	 * processed or child nodes already processed).
	 * 
	 * @return current processing state
	 */
	public RunOrder getRunOrder();
}
