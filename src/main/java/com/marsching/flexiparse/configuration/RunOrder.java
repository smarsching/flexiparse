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

package com.marsching.flexiparse.configuration;

/**
 * Specifies the run order for a handler. For the configuration
 * all values are valid. At runtime only START or END represent
 * a valid state.
 * 
 * @author Sebastian Marsching
 */
public enum RunOrder {
	/**
	 * Run handler before processing child nodes
	 */
	START,
	
	/**
	 * Run handler after processing child nodes
	 */
	END,
	
	/**
	 * Run handler before and after processing child nodes.
	 * The state is available through 
	 * {@link com.marsching.flexiparse.parser.HandlerContext#getRunOrder}.
	 */
	BOTH
}
