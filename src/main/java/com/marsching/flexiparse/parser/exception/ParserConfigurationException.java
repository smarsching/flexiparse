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

package com.marsching.flexiparse.parser.exception;

/**
 * Exception thrown if there is a problem with the configuration of 
 * a parser.
 * 
 * @author Sebastian Marsching
 */
public class ParserConfigurationException extends ParserException {

	private static final long serialVersionUID = -2670349229698335696L;

	public ParserConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserConfigurationException(String message) {
		super(message);
	}

	public ParserConfigurationException(Throwable cause) {
		super(cause);
	}

}
