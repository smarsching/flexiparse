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

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.marsching.flexiparse.objectree.ObjectTreeElement;
import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * Parsers parse XML files and produce object trees .
 * 
 * @author Sebastian Marsching
 * @see NodeHandler
 * @see ParsingHandler
 * @see ObjectTreeElement
 */
public interface Parser {
	
	/**
	 * Parses a DOM document.
	 * @param document DOM document
	 * @param rootObjects objects that should be attached to the root node
	 *   of the object tree before parsing the document
	 * @return Root node of the resulting object tree
	 * @throws ParserException if an error occurs during the parsing process
	 */
	public ObjectTreeElement parse(Document document, Object... rootObjects) throws ParserException;
	
	/**
	 * Adds a node handler to this parser. A node handler is called
	 * for the configured XML nodes and can add objects to the
	 * object tree.
	 * 
	 * @param nodeHandler handler to add to the parser
	 */
	public void addNodeHandler(NodeHandler nodeHandler);
	
	/**
	 * Removes a handler from this parser.
	 * 
	 * @param nodeHandler handler to remove from this parser
	 * @return <code>true</code> if the handler was removed,
	 *   <code>false</code> otherwise
	 */
	public boolean removeNodeHandler(NodeHandler nodeHandler);
	
	/**
	 * Returns all node handlers configured for this parser.
	 * 
	 * @return node handlers for this parser
	 */
	public Collection<? extends NodeHandler> getNodeHandlers();
	
	/**
	 * Parses a DOM document.
	 * @param file file containing the xml document to be parsed
	 * @param rootObjects objects that should be attached to the root node
     *   of the object tree before parsing the document
	 * @return Root node of the resulting object tree
	 * @throws ParserException if an error occurs during the parsing process
	 */
	public ObjectTreeElement parse(File file, Object... rootObjects) throws ParserException;
	
	/**
	 * Parses a XML document provided by a input stream.
	 * @param inputStream stream to read from
	 * @param rootObjects objects that should be attached to the root node
     *   of the object tree before parsing the document
	 * @return Root node of the resulting object tree
	 * @throws ParserException if an error occurs during the parsing process
	 */
	public ObjectTreeElement parse(InputStream inputStream, Object... rootObjects) throws ParserException;
	
	/**
	 * Parses a XML document obtained from an input source.
	 * @param inputSource source providing the XML document
	 * @param rootObjects objects that should be attached to the root node
     *   of the object tree before parsing the document
	 * @return Root node of the resulting object tree
	 * @throws ParserException if an error occurs during the parsing process
	 */
	public ObjectTreeElement parse(InputSource inputSource, Object... rootObjects) throws ParserException;
	
}
