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

import org.w3c.dom.Element;

import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.ParsingHandler;
import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;


public class TextMappingHandler extends MappingHandler implements ParsingHandler {
    
    public void handleNode(HandlerContext context) throws ParserException {
        TextMappingConfigurationImpl config = new TextMappingConfigurationImpl();
        Element element = (Element) context.getNode();
        config.setTargetType(getTargetType(element));
        String targetAttribute = getTargetAttribute(element);
        // "target attribute" attribute is mandatory for child configurations
        if (targetAttribute.length() == 0) {
            throw new ParserConfigurationException("\"target-attribute\" attribute is mandatory for text mappings");
        }
        config.setTargetAttribute(targetAttribute);
        config.setMinOccurs(getMinOccurs(element));
        config.setMaxOccurs(getMaxOccurs(element));
        String ignoreWhiteSpace = element.getAttribute("ignore-white-space");
        config.setIgnoreWhiteSpaceNodes(Boolean.parseBoolean(ignoreWhiteSpace));
        String append = element.getAttribute("append");
        config.setAppend(Boolean.parseBoolean(append));
        context.getObjectTreeElement().addObject(config);
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
