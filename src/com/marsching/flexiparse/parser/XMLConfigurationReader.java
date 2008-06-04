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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.marsching.flexiparse.configuration.HandlerConfiguration;
import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.objectree.ObjectTreeElement;
import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.parser.internal.SimpleNodeHandler;
import com.marsching.flexiparse.util.DOMBasedNamespaceContext;

/**
 * Reads a XML configuration file and returns handler configurations.
 * 
 * @author Sebastian Marsching
 */
public class XMLConfigurationReader {
	private Parser parser;
	
	public XMLConfigurationReader() {
		parser = new SimpleParser();
		final XPathFactory xpfac = XPathFactory.newInstance();
		final XPath xpath = xpfac.newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {

			public String getNamespaceURI(String prefix) {
				if (prefix.equals("c")) {
					return "http://www.marsching.com/2008/flexiparse/configurationNS";
				} else {
					return "";
				}
			}

			public String getPrefix(String namespaceURI) {
				throw new NotImplementedException();
			}

			@SuppressWarnings("unchecked")
			public Iterator getPrefixes(String namespaceURI) {
				throw new NotImplementedException();
			}
			
		});
		NodeHandler configHandler = new NodeHandler() {

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

			public void handleNode(HandlerContext context)
					throws ParserException {
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
			
		};
		parser.addNodeHandler(configHandler);
	}
	
	/**
	 * Parses the file provided by the InputSource and returns all
	 * handlers that are configured in this file.
	 * 
	 * @param source input source to read configuration from
	 * @return all handlers specified in the configuration file
	 * @throws ParserException if an error occurs while parsing
	 *   the configuration
	 */
	public Collection<NodeHandler> readConfiguration(InputSource source) throws ParserException {
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
			throw new ParserConfigurationException("Error while reading configuration from " + source.getSystemId());
		} catch (IOException e) {
			throw new ParserConfigurationException("Error while reading configuration from " + source.getSystemId());
		}
		ObjectTreeElement ote = parser.parse(doc);
		Set<NodeHandler> handlers = new HashSet<NodeHandler>();
		Collection<SimpleHandlerConfiguration> configurations = ote.getObjectsOfTypeFromSubTree(SimpleHandlerConfiguration.class);
		for (SimpleHandlerConfiguration configuration : configurations) {
			ParsingHandler parsingHandler;
			try {
				parsingHandler = Class.forName(configuration.className).asSubclass(ParsingHandler.class).newInstance();
			} catch (InstantiationException e) {
				throw new ParserConfigurationException("Could not instantiate handler class " + configuration.className, e);
			} catch (IllegalAccessException e) {
				throw new ParserConfigurationException("Could not instantiate handler class " + configuration.className, e);
			} catch (ClassNotFoundException e) {
				throw new ParserConfigurationException("Could not load handler class " + configuration.className, e);
			} catch (ClassCastException e) {
				throw new ParserConfigurationException("Hndler class " + configuration.className + " is not an instance of ParsingHandler.", e);
			}
			NodeHandler handler = new SimpleNodeHandler(parsingHandler, configuration);
			handlers.add(handler);
		}
		return Collections.unmodifiableCollection(handlers);
	}
	
	private class SimpleHandlerConfiguration implements HandlerConfiguration {
		private Set<String> followingModules = new HashSet<String>();
		private Set<String> precedingModules = new HashSet<String>();
		private String identifier;
		private String className;
		private Set<XPathExpression> expressions = new HashSet<XPathExpression>();
		private RunOrder runOrder;
		
		public Collection<String> getFollowingHandlers() {
			return Collections.unmodifiableCollection(followingModules);
		}

		public String getIdentifier() {
			return identifier;
		}

		public Collection<String> getPrecedingHandlers() {
			return Collections.unmodifiableCollection(precedingModules);
		}

		public Collection<XPathExpression> getXPathExpressions() {
			return Collections.unmodifiableCollection(expressions);
		}

		public RunOrder getRunOrder() {
			return runOrder;
		}
		
	}
}
