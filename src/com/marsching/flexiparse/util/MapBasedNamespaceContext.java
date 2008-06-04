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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * Implementation of {@link NamespaceContext} backed by a 
 * prefix to URI map.
 * 
 * @author Sebastian Marsching
 */
public class MapBasedNamespaceContext extends AbstractNamespaceContext {
	private Map<? extends String, ? extends String> mapping;
	
	/**
	 * Create an instance using the supplied map
	 * 
	 * @param mapping map mapping prefixes to namespace URIs
	 */
	public MapBasedNamespaceContext(Map<? extends String, ? extends String> mapping) {
		super();
		this.mapping = mapping;
	}
	
	@Override
	public String getNamespaceURI(String prefix) {
		String uri = mapping.get(prefix);
		if (uri == null) {
			uri = super.getNamespaceURI(prefix);
		}
		return uri;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		for (String prefix : mapping.keySet()) {
			if (mapping.get(prefix).equals(namespaceURI)) {
				return prefix;
			}
		}
		return super.getPrefix(namespaceURI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		LinkedHashSet<String> prefixes = new LinkedHashSet<String>();
		for (Iterator i = super.getPrefixes(namespaceURI); i.hasNext();) {
			String prefix = (String) i.next();
			prefixes.add(prefix);
		}
		for (String prefix : mapping.keySet()) {
			if (mapping.get(prefix).equals(namespaceURI)) {
				prefixes.add(prefix);
			}
		}
		return prefixes.iterator();
	}

}
