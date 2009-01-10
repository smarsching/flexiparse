/* 
 * fleXiParse - Copyright 2008-2009 Sebastian Marsching
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

import java.util.Collection;

import org.xml.sax.InputSource;

import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * {@link Parser} implementation that is configured using XML 
 * configuration files.
 * 
 * @author Sebastian Marsching
 * @see XMLConfigurationReader
 */
public class XMLConfiguredParser extends SimpleParser {
	private XMLConfigurationReader reader = new XMLConfigurationReader(this);
	
	/**
	 * Adds all handlers specified in the configuration file to this
	 * parser.
	 * 
	 * @param configurationSource input source to read configuration from
	 * @throws ParserException if an error occurs while parsing the
	 *   configuration file
	 */
	public void addConfigurationSource(InputSource configurationSource) throws ParserException {
		reader.readConfiguration(configurationSource);
	}
	
	/**
	 * Adds all handlers specified in all the configuration files to
	 * this parser.
	 * 
	 * @param configurationSources input sources to read configurations from
	 * @throws ParserException if an error occurs while parsing on 
	 *   of the configuration files
	 */
	public void addConfigurationSources(Collection<? extends InputSource> configurationSources) throws ParserException {
		for (InputSource source : configurationSources) {
			reader.readConfiguration(source);
		}		
	}
	
}
