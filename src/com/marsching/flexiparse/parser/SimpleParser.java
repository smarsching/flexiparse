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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.objectree.DefaultMutableObjectTreeElement;
import com.marsching.flexiparse.objectree.MutableObjectTreeElement;
import com.marsching.flexiparse.objectree.ObjectTreeElement;
import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.parser.internal.Map2D;

/**
 * Simple implementation of the {@link Parser} interface. Parses a
 * XML document using the parsers manually attached to this parser.
 * 
 * @author Sebastian Marsching
 */
public class SimpleParser implements Parser {
	private MutableObjectTreeElement currentNode = null;
	private Map<Node, ? extends Collection<NodeHandler>> nodeToStartNodeHandlers = null;
	private Map<Node, ? extends Collection<NodeHandler>> nodeToEndNodeHandlers = null;

	/**
	 * Stores node handlers configured for this parser
	 */
	protected LinkedHashSet<NodeHandler> nodeHandlers = new LinkedHashSet<NodeHandler>();
	
	/**
	 * Document builder instance used for XML operations by this parser
	 */
	protected DocumentBuilder documentBuilder;
	
	/**
	 * Creates a parser without any handlers.
	 */
	public SimpleParser() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setXIncludeAware(true);
			documentBuilder =  dbf.newDocumentBuilder();
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			throw new RuntimeException("Could not create DocumentBuilder.", e);
		}
	}
	
	public ObjectTreeElement parse(Document doc, Object... rootObjects) throws ParserException {
		Map<XPathExpression, ? extends Collection<NodeHandler>> xpathMap = prepareXPath(nodeHandlers);
		nodeToStartNodeHandlers = createNodeToHandlerMap(doc, xpathMap, RunOrder.START);
		nodeToEndNodeHandlers = createNodeToHandlerMap(doc, xpathMap, RunOrder.END);
		currentNode = new DefaultMutableObjectTreeElement(null);
		for (Object o : rootObjects) {
		    currentNode.addObject(o);
		}
		Element rootElement = doc.getDocumentElement();
		handleNode(rootElement);
		return currentNode;
	}
	
	private void handleNode(Node node) throws ParserException {
		Collection<NodeHandler> startNodeHandlers = nodeToStartNodeHandlers.get(node);
		if (startNodeHandlers == null) {
			startNodeHandlers = Collections.emptySet();
		}
		Collection<NodeHandler> endNodeHandlers = nodeToEndNodeHandlers.get(node);
		if (endNodeHandlers == null) {
			endNodeHandlers = Collections.emptySet();
		}
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			MutableObjectTreeElement newNode = new DefaultMutableObjectTreeElement(currentNode);
			if (currentNode != null) {
				currentNode.addChild(newNode);
			}
			currentNode = newNode;
			try {
				callNodeHandlers(node, startNodeHandlers, RunOrder.START);
				NodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					handleNode(children.item(i));
				}
				callNodeHandlers(node, endNodeHandlers, RunOrder.END);
			} finally {
				if (currentNode.getParent() != null) {
					currentNode = currentNode.getParent();
				}
			}
		} else {
			callNodeHandlers(node, startNodeHandlers, RunOrder.START);
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				handleNode(children.item(i));
			}
			callNodeHandlers(node, endNodeHandlers, RunOrder.END);
		}
	}
	
	private void callNodeHandlers(Node node, Collection<NodeHandler> handlers, RunOrder runOrder) throws ParserException {
		SimpleHandlerContext context = new SimpleHandlerContext();
		context.node = node;
		context.objectTreeElement = currentNode;
		context.runOrder = runOrder;
		if (handlers != null) {
			handlers = getHandlerOrder(handlers);
			for (NodeHandler handler : handlers) {
				handler.handleNode(context);
			}
		}
		
	}
	
	/**
	 * Determines the order in which the handlers should be called.
	 * 
	 * @param handlers Handlers to determine order for
	 * @return handlers ordered according to the configuration
	 * @throws ParserConfigurationException if handlers have a cyclic
	 *   dependency
	 */
	protected Collection<NodeHandler> getHandlerOrder(
			Collection<NodeHandler> handlers) throws ParserConfigurationException {
		ArrayList<NodeHandler> orderedHandlers = new ArrayList<NodeHandler>();
		ArrayList<String> order = new ArrayList<String>();
		Map2D<String, String, Boolean> orderMatrix = new Map2D<String, String, Boolean>(false);
		// Fill matrix
		for (NodeHandler handler : handlers) {
			String handlerId = handler.getConfiguration().getIdentifier();
			for (String s : handler.getConfiguration().getFollowingHandlers()) {
				orderMatrix.set(handlerId, s, true);
			}
			for (String s : handler.getConfiguration().getPrecedingHandlers()) {
				orderMatrix.set(s, handlerId, true);
			}
			if (handler.getConfiguration().getPrecedingHandlers().size() == 0
					&& handler.getConfiguration().getFollowingHandlers().size() == 0) {
				order.add(handlerId);
			}
		}
		// Find module that does not have any "following" requirements
		while (orderMatrix.getRows().size() != 0) {
			int startSize = orderMatrix.getRows().size();
			for (String handlerId : orderMatrix.getRowKeys()) {
				if (!orderMatrix.getRow(handlerId).containsValue(true)) {
					order.add(handlerId);
					orderMatrix.deleteRow(handlerId);
					orderMatrix.deleteColumn(handlerId);
				}
			}
			if (orderMatrix.getRows().size() == startSize) {
				throw new ParserConfigurationException("Cannot handle cyclic module dependencies.");
			}
		}
		
		Collections.reverse(order);
		for (int i = 0; i < order.size(); i++) {
			for (NodeHandler handler : handlers) {
				if (handler.getConfiguration().getIdentifier().equals(order.get(i))) {
					orderedHandlers.add(handler);
				}
			}
		}
		
		return orderedHandlers;
	}

	private class SimpleHandlerContext implements HandlerContext {
		private Node node;
		private ObjectTreeElement objectTreeElement;
		private RunOrder runOrder;
		
		public Node getNode() {
			return node;
		}

		public ObjectTreeElement getObjectTreeElement() {
			return objectTreeElement;
		}
		
		public RunOrder getRunOrder() {
			return runOrder;
		}
	}

	public void addNodeHandler(NodeHandler nodeHandler) {
		nodeHandlers.add(nodeHandler);
	}

	public Collection<? extends NodeHandler> getNodeHandlers() {
		return Collections.unmodifiableCollection(nodeHandlers);
	}

	public boolean removeNodeHandler(NodeHandler nodeHandler) {
		return nodeHandlers.remove(nodeHandler);
	}
	
	private Map<XPathExpression, ? extends Collection<NodeHandler>> prepareXPath(Collection<NodeHandler> nodeHandlers) throws ParserConfigurationException {
		Map<XPathExpression, Set<NodeHandler>> xpathMap = new LinkedHashMap<XPathExpression, Set<NodeHandler>>();
		for (NodeHandler handler : nodeHandlers) {
			for (XPathExpression xpe : handler.getConfiguration().getXPathExpressions()) {
				Set<NodeHandler> handlerSet = xpathMap.get(xpe);
				if (handlerSet == null) {
					handlerSet = new HashSet<NodeHandler>();
					xpathMap.put(xpe, handlerSet);
				}
				handlerSet.add(handler);
			}
		}
		return xpathMap;
	}
	
	private Map<Node, ? extends Collection<NodeHandler>> createNodeToHandlerMap(Document doc, Map<XPathExpression, ? extends Collection<NodeHandler>> xpathMap, RunOrder runOrder) {
		Map<Node, Set<NodeHandler>> nodeToHandler = new HashMap<Node, Set<NodeHandler>>();
		for (XPathExpression xpe : xpathMap.keySet()) {
			try {
				NodeList list = (NodeList) xpe.evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					Set<NodeHandler> handlerSet = nodeToHandler.get(node);
					if (handlerSet == null) {
						handlerSet = new HashSet<NodeHandler>();
						nodeToHandler.put(node, handlerSet);
					}
					for (NodeHandler handler : xpathMap.get(xpe)) {
						if (handler.getConfiguration().getRunOrder() == RunOrder.BOTH
								|| handler.getConfiguration().getRunOrder() == runOrder) {
							handlerSet.add(handler);
						}
					}
				}
			} catch (XPathExpressionException e) {
				throw new RuntimeException("Unexpected XPath exception for expression \"" + xpe + "\": " + e.getMessage(), e);
			}
		}
		return nodeToHandler;
	}
	
	public ObjectTreeElement parse(File file, Object... rootObjects) throws ParserException {
		try {
			return parse(new FileInputStream(file), rootObjects);
		} catch (FileNotFoundException e) {
			throw new ParserException("File \"" + file.getAbsolutePath() + "\" could not be found.", e);
		}
	}
	
	public ObjectTreeElement parse(InputStream inputStream, Object... rootObjects) throws ParserException {
		return parse(new InputSource(inputStream), rootObjects);
	}
	
	public ObjectTreeElement parse(InputSource inputSource, Object... rootObjects) throws ParserException {
		try {
			return parse(documentBuilder.parse(inputSource), rootObjects);
		} catch (SAXException e) {
			throw new ParserException("Error while parsing document " + inputSource.toString(), e);
		} catch (IOException e) {
			throw new ParserException("Error while reading document " + inputSource.toString(), e);
		}
	}
}
