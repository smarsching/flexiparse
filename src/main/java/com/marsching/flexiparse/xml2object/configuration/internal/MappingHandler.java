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

package com.marsching.flexiparse.xml2object.configuration.internal;

import java.util.Collection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.util.DOMBasedNamespaceContext;


public abstract class MappingHandler {
    
    protected String getTargetAttribute(Element element) {
        return element.getAttribute("target-attribute");
    }
    
    protected String getTargetType(Element element) {
        String targetType = element.getAttribute("target-type");
        if (targetType.length() == 0) {
            targetType = "java.lang.String";
        }
        return targetType;
    }
    
    protected ClassLoader getTargetTypeClassLoader(HandlerContext context) {
        Collection<ClassLoader> classLoaders = context.getObjectTreeElement().getRoot().getObjectsOfType(ClassLoader.class);
        if (classLoaders.size() != 1) {
            throw new RuntimeException("Found more or less than one class loader attached to the root!");
        }
        return classLoaders.iterator().next();
    }
    
    protected String convertQNameToLocalName(String qName) {
        int colonPos = qName.indexOf(':');
        if (colonPos == -1) {
            return qName;
        } else {
            return qName.substring(colonPos + 1);
        }
    }
    
    protected String convertQNameToNamespace(String qName, Node contextNode) {
        int colonPos = qName.indexOf(':');
        if (colonPos == -1) {
            return "";
        } else {
            DOMBasedNamespaceContext nsContext = new DOMBasedNamespaceContext(contextNode);
            return nsContext.getNamespaceURI(qName.substring(0, colonPos));
        }
    }
    
}
