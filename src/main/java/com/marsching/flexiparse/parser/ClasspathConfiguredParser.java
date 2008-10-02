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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.xml.sax.InputSource;

import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * Parser implementation that loads the configuration using the context
 * class loader.
 * 
 * @author Sebastian Marsching
 */
public class ClasspathConfiguredParser extends XMLConfiguredParser {
	
	/**
	 * Default constructor, looks for configuration at
	 * "com/marsching/flexiparse/configuration/configuration.xml".
	 * 
	 * @throws ParserException
	 */
	public ClasspathConfiguredParser() throws ParserException {
		this("com/marsching/flexiparse/configuration/configuration.xml");
	}
	
	/**
	 * Creates a parser instance using the specified configuration
	 * location.
	 * 
	 * @param configurationLocation location where to look for 
	 * 	 configuration files
	 * @throws ParserException if an error occurs while loading
	 *   the configuration files
	 */
	public ClasspathConfiguredParser(String configurationLocation) throws ParserException {
		super();
		
		Enumeration<URL> en;
		try {
			en = Thread.currentThread().getContextClassLoader().getResources(configurationLocation);
		} catch (IOException e) {
			throw new ParserConfigurationException("Could not get configuration files from class loader.", e);
		}
		while (en.hasMoreElements()) {
			URL url = en.nextElement();
			InputSource source;
			try {
				source = new InputSource(url.openStream());
			} catch (IOException e) {
				throw new ParserConfigurationException("Could not load configuration from " + url.toString(), e);
			}
			addConfigurationSource(source);
		}
	}
}
