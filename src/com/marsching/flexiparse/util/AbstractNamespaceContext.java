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

package com.marsching.flexiparse.util;

import java.util.Collections;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Base class providing common code for all implementations of
 * {@link NamespaceContext}.
 * 
 * @author Sebastian Marsching
 */
public abstract class AbstractNamespaceContext implements NamespaceContext {
	
	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("Null is not allowed for prefix parameter");
		} else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		} else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
			return XMLConstants.XML_NS_URI;
		} else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return XMLConstants.NULL_NS_URI;
		} else {
			return XMLConstants.NULL_NS_URI;
		}
	}

	public String getPrefix(String namespaceURI) {
		if (namespaceURI == null) {
			throw new IllegalArgumentException("Null is not allowed for namespaceURI parameter");
		} else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
			return XMLConstants.XMLNS_ATTRIBUTE;
		} else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
			return XMLConstants.XML_NS_PREFIX;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator getPrefixes(String namespaceURI) {
		if (namespaceURI == null) {
			throw new IllegalArgumentException("Null is not allowed for namespaceURI parameter");
		} else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
			return Collections.singleton(XMLConstants.XMLNS_ATTRIBUTE).iterator();
		} else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
			return Collections.singleton(XMLConstants.XML_NS_PREFIX).iterator();
		} else {
			return Collections.emptySet().iterator();
		}
	}

}
