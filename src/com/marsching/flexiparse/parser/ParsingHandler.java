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

import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * Parsing handlers are called by the {@link Parser} for matching
 * XML nodes.
 * 
 * @author Sebastian Marsching
 * @see NodeHandler
 */
public interface ParsingHandler {
	
	/**
	 * Called for every matching node. Used to do parsing and attach
	 * objects to the result object tree.
	 * 
	 * @param context provides context information for the current node
	 * @throws ParserException if any error occurs during the parsing
	 */
	public void handleNode(HandlerContext context) throws ParserException;
	
}
