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

package com.marsching.flexiparse.parser.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;

import com.marsching.flexiparse.configuration.HandlerConfiguration;
import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.NodeHandler;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.util.DOMBasedNamespaceContext;


public class BaseConfigurationNodeHandler implements NodeHandler {
    
    private XPathFactory xpfac;
    private XPath xpath;
    
    public BaseConfigurationNodeHandler() {
        xpfac = XPathFactory.newInstance();
        xpath = xpfac.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {

            public String getNamespaceURI(String prefix) {
                if (prefix.equals("c")) {
                    return "http://www.marsching.com/2008/flexiparse/configurationNS";
                } else {
                    return "";
                }
            }

            public String getPrefix(String namespaceURI) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unchecked")
            public Iterator getPrefixes(String namespaceURI) {
                throw new UnsupportedOperationException();
            }
            
        });
    }
    
    public HandlerConfiguration getConfiguration() {
        return new HandlerConfiguration() {

            public List<String> getFollowingHandlers() {
                return Collections.emptyList();
            }

            public String getIdentifier() {
                return "com.marsching.flexiparse.parser.XMLConfigurationReader.configHandler";
            }

            public List<String> getPrecedingHandlers() {
                return Collections.emptyList();
            }

            public List<XPathExpression> getXPathExpressions() {
                ArrayList<XPathExpression> list = new ArrayList<XPathExpression>();
                try {
                    list.add(xpath.compile("/c:configuration/c:handler"));
                    list.add(xpath.compile("/c:configuration/c:handler/c:match"));
                    list.add(xpath.compile("/c:configuration/c:handler/c:preceding-module"));
                    list.add(xpath.compile("/c:configuration/c:handler/c:following-module"));
                } catch (XPathExpressionException e) {
                    // No exception should happen here
                    throw new RuntimeException("Unexpected exception", e);
                }
                return list;
            }

            public RunOrder getRunOrder() {
                return RunOrder.START;
            }
            
        };
    }
    
    public void handleNode(HandlerContext context) throws ParserException {
        Element element = (Element) context.getNode();
        if (element.getTagName().equals("handler")) {
            SimpleHandlerConfiguration conf = new SimpleHandlerConfiguration();
            if (element.hasAttribute("class")) {
                conf.className = element.getAttribute("class");
            } else {
                throw new ParserException("Required attribute \"class\" missing on element \"handler\".");
            }
            if (element.hasAttribute("id")) {
                conf.identifier = element.getAttribute("id");
            } else {
                conf.identifier = conf.className;
            }
            if (element.hasAttribute("run-order")) {
                String runOrder = element.getAttribute("run-order");
                if (runOrder.equals("start")) {
                    conf.runOrder = RunOrder.START;
                } else if (runOrder.equals("end")) {
                    conf.runOrder = RunOrder.END;
                } else if (runOrder.equals("both")) {
                    conf.runOrder = RunOrder.BOTH;
                } else {
                    throw new ParserException("Attribute \"run-order\" is set to \"" + runOrder + "\" but has to be \"start\" or \"end\"");
                }
            } else {
                conf.runOrder = RunOrder.START;
            }
            context.getObjectTreeElement().addObject(conf);
        } else if (element.getTagName().equals("preceding-module")) {
            SimpleHandlerConfiguration conf = context.getObjectTreeElement().getObjectsOfTypeFromTopTree(SimpleHandlerConfiguration.class).iterator().next();
            conf.precedingModules.add(element.getTextContent());
        } else if (element.getTagName().equals("following-module")) {
            SimpleHandlerConfiguration conf = context.getObjectTreeElement().getObjectsOfTypeFromTopTree(SimpleHandlerConfiguration.class).iterator().next();
            conf.followingModules.add(element.getTextContent());
        } else if (element.getTagName().equals("match")) {
            SimpleHandlerConfiguration conf = context.getObjectTreeElement().getObjectsOfTypeFromTopTree(SimpleHandlerConfiguration.class).iterator().next();
            XPath xpath = xpfac.newXPath();
            xpath.setNamespaceContext(new DOMBasedNamespaceContext(element));
            XPathExpression expr;
            try {
                expr = xpath.compile(element.getTextContent());
            } catch (XPathExpressionException e) {
                throw new ParserException("Could not compile XPath expression \"" + element.getTextContent() + "\": " + e.getMessage(), e);
            }
            conf.expressions.add(expr);
        }
    }
    
}
