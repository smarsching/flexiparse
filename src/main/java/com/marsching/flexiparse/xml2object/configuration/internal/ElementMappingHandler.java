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

import java.util.LinkedList;

import org.w3c.dom.Element;

import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.objecttree.ObjectTreeElement;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.ParsingHandler;
import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.xml2object.configuration.ElementMappingConfiguration;
import com.marsching.flexiparse.xml2object.configuration.MappingConfiguration;


public class ElementMappingHandler extends MappingHandler implements ParsingHandler {

    public void handleNode(HandlerContext context) throws ParserException {
        if (context.getRunOrder() == RunOrder.START) {
            handleStart(context);
        } else if (context.getRunOrder() == RunOrder.END) {
            handleEnd(context);
        }
    }

    private void handleStart(HandlerContext context) throws ParserException {
        ElementMappingConfigurationImpl config = new ElementMappingConfigurationImpl();
        Element element = (Element) context.getNode();
        config.setTargetType(getTargetType(element));
        config.setTargetTypeClassLoader(getTargetTypeClassLoader(context));
        String targetAttribute = getTargetAttribute(element);
        // "target attribute" attribute is mandatory for child configurations
        if (!context.getObjectTreeElement().getObjectsOfTypeFromTopTree(ElementMappingConfiguration.class).isEmpty()
                && targetAttribute.length() == 0) {
            throw new ParserConfigurationException("target-attribute attribute is mandatory for child element configurations");
        }
        config.setTargetAttribute(targetAttribute);
        config.setMinOccurs(getMinOccurs(element));
        config.setMaxOccurs(getMaxOccurs(element));
        String elementQName = element.getAttribute("name");
        String elementName = convertQNameToLocalName(elementQName);
        String elementNamespace = convertQNameToNamespace(elementQName, element);
        if (elementName.length() == 0) {
            throw new ParserConfigurationException("name attribute has to be set for element mapping configuration");
        }
        config.setElementName(elementName);
        config.setElementNamespace(elementNamespace);
        String deepSearch = element.getAttribute("deep-search");
        if (deepSearch.length() == 0) {
            config.setDeepSearch(false);
        } else if (deepSearch.equals("true")) {
            config.setDeepSearch(true);
        } else if (deepSearch.equals("false")) {
            config.setDeepSearch(false);
        } else {
            throw new ParserConfigurationException("deep-search attribute has to be set to either \"true\" or \"false\"");
        }
        context.getObjectTreeElement().addObject(config);
    }
    
    private void handleEnd(HandlerContext context) throws ParserException {
        ElementMappingConfigurationImpl config = context.getObjectTreeElement().getObjectsOfType(ElementMappingConfigurationImpl.class).iterator().next();
        LinkedList<MappingConfiguration> children = new LinkedList<MappingConfiguration>();
        for (ObjectTreeElement childElement : context.getObjectTreeElement().getChildren()) {
            children.addAll(childElement.getObjectsOfType(MappingConfiguration.class));
        }
        config.setChildMappings(children);
    }
    
    private int getMinOccurs(Element element) throws ParserConfigurationException {
        String occurrence = element.getAttribute("occurrence");
        if (occurrence.equals("1")) {
            return 1;
        } else if (occurrence.equals("0..1")) {
            return 0;
        } else if (occurrence.equals("0..n")) {
            return 0;
        } else if (occurrence.equals("1..n")) {
            return 1;
        } else if (occurrence.length() == 0) {
            return 0;
        } else {
            throw new ParserConfigurationException("occurrence attribute has invalid value");
        }
    }
    
    private int getMaxOccurs(Element element) throws ParserConfigurationException {
        String occurrence = element.getAttribute("occurrence");
        if (occurrence.equals("1")) {
            return 1;
        } else if (occurrence.equals("0..1")) {
            return 1;
        } else if (occurrence.equals("0..n")) {
            return -1;
        } else if (occurrence.equals("1..n")) {
            return -1;
        } else if (occurrence.length() == 0) {
            return 1;
        } else {
            throw new ParserConfigurationException("occurrence attribute has invalid value");
        }
    }
    
}
