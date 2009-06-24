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

import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.marsching.flexiparse.objecttree.ObjectTreeElement;
import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.parser.internal.BaseConfigurationNodeHandler;
import com.marsching.flexiparse.parser.internal.SimpleHandlerConfiguration;
import com.marsching.flexiparse.parser.internal.SimpleNodeHandler;
import com.marsching.flexiparse.xml2object.configuration.ElementMappingConfiguration;

/**
 * Reads a XML configuration file and returns handler configurations.
 * 
 * @author Sebastian Marsching
 */
public class XMLConfigurationReader {
	private Parser parser;
	private Parser targetParser;
	
	/**
	 * Constructs a new configuration reader that will configure the supplied
	 * parser when the {@link #readConfiguration(InputSource)} method is called.
	 * 
	 * @param parser parser that will be configured by this reader
	 */
	public XMLConfigurationReader(Parser parser) {
		this(parser, false);
	}
	
	private XMLConfigurationReader(Parser targetParser, boolean baseOnly) {
	    this.targetParser = targetParser;
	    this.parser = new SimpleParser();
        NodeHandler configHandler = new BaseConfigurationNodeHandler();
        this.parser.addNodeHandler(configHandler);
	    if (!baseOnly) {
	        XMLConfigurationReader baseReader = new XMLConfigurationReader(this.parser, true);
	        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
	        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            try {
                baseReader.readConfiguration(new InputSource(this.getClass().getClassLoader().getResource("com/marsching/flexiparse/xml2object/configuration/internal/flexiparse-xml2object-configuration.xml").toExternalForm()));
            } catch (ParserException e) {
                throw new RuntimeException("Unexpected error while reading internal configuration file", e);
            } finally {
                Thread.currentThread().setContextClassLoader(ccl);
            }
	    }
	}
	
	/**
	 * Parses the file provided by the InputSource and configures the
	 * parser attached to this reader using the configuration data in the file.
	 * 
	 * @param source input source to read configuration from
	 * @throws ParserException if an error occurs while parsing
	 *   the configuration
	 */
	public void readConfiguration(InputSource source) throws ParserException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setXIncludeAware(true);
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			throw new RuntimeException("Could not create DocumentBuilder.");
		}
		Document doc;
		try {
			doc = db.parse(source);
		} catch (SAXException e) {
			throw new ParserConfigurationException("Error while reading configuration from " + source.getSystemId(), e);
		} catch (IOException e) {
			throw new ParserConfigurationException("Error while reading configuration from " + source.getSystemId(), e);
		}
		ObjectTreeElement ote = parser.parse(doc);
		Collection<SimpleHandlerConfiguration> configurations = ote.getObjectsOfTypeFromSubTree(SimpleHandlerConfiguration.class);
		for (SimpleHandlerConfiguration configuration : configurations) {
			ParsingHandler parsingHandler;
			String className = configuration.getClassName();
			try {
				parsingHandler = Class.forName(className, true, Thread.currentThread().getContextClassLoader()).asSubclass(ParsingHandler.class).newInstance();
			} catch (InstantiationException e) {
				throw new ParserConfigurationException("Could not instantiate handler class " + className, e);
			} catch (IllegalAccessException e) {
				throw new ParserConfigurationException("Could not instantiate handler class " + className, e);
			} catch (ClassNotFoundException e) {
				throw new ParserConfigurationException("Could not load handler class " + className, e);
			} catch (ClassCastException e) {
				throw new ParserConfigurationException("Hndler class " + className + " is not an instance of ParsingHandler.", e);
			}
			NodeHandler handler = new SimpleNodeHandler(parsingHandler, configuration);
			targetParser.addNodeHandler(handler);
		}
		for (ObjectTreeElement ote2 : ote.getChildren().iterator().next().getChildren()) {
		    Collection<ElementMappingConfiguration> confs = ote2.getObjectsOfType(ElementMappingConfiguration.class); 
		    for (ElementMappingConfiguration c : confs) {
		        targetParser.addElementMappingConfiguration(c);
		    }
		}
	}
	
}
