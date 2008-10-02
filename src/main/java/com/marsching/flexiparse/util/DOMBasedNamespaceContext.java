package com.marsching.flexiparse.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Provides namespace information from a DOM document.
 * 
 * @author Sebastian Marsching
 */
public class DOMBasedNamespaceContext extends AbstractNamespaceContext {
	private class NamespaceContentHandler extends DefaultHandler {
		private int count;
		private NamespaceSupport nsSupport = new NamespaceSupport();
		private boolean firstDeclFlag = true;
		
		public NamespaceContentHandler(int elementPosition) {
			super();
			count = elementPosition;
			nsSupport.pushContext();
		}

		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (count > 0) {
				nsSupport.popContext();
			}
		}

		public void startElement(String uri, String localName, String name,
				Attributes atts) throws SAXException {
			if (count > 0) {
		        if (firstDeclFlag) {
	                  nsSupport.pushContext();
		        } else {
		            firstDeclFlag = true;
		        }
				count--;
			}

			
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			if (count > 0) {
				if (firstDeclFlag) {
					nsSupport.pushContext();
					firstDeclFlag = false;
				}
				nsSupport.declarePrefix(prefix, uri);
			}
		}
	}

	private NamespaceSupport nss;
	
	/**
	 * Creates a namespace context that provides the namespace
	 * information valid for the supplied node.
	 * 
	 * @param contextNode node whose namespace context should be used
	 */
	public DOMBasedNamespaceContext(Node contextNode) {
		int elementPosition = calculateElementPosition(contextNode);
		SAXResult result = new SAXResult();
		NamespaceContentHandler nch = new NamespaceContentHandler(elementPosition);
		result.setHandler(nch);
		try {
			Transformer identityTransformer;
			identityTransformer = TransformerFactory.newInstance().newTransformer();
			identityTransformer.transform(new DOMSource(contextNode.getOwnerDocument()), result);
		} catch (TransformerException e) {
			throw new RuntimeException("Unexpected TransformerException: " + e.getMessage(), e);
		}
		this.nss = nch.nsSupport;
	}
	
	private int calculateElementPosition(Node node) {
		// Find next element node
		while (node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getParentNode();
		}
		Element element = (Element) node;
		int count = 0;
		while (element.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
			Node sibling = element.getPreviousSibling();
			while (sibling != null) {
				if (sibling.getNodeType() == Node.ELEMENT_NODE) {
					count += countElementsInSubtree((Element) sibling);
				}
				sibling = sibling.getPreviousSibling();
			}
			// Count parent node
			count++;
			element = (Element) element.getParentNode();
		}
		return count;
	}

	private int countElementsInSubtree(Element element) {
		int count = 1;
		NodeList list = element.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				count += countElementsInSubtree((Element) n);
			}
		}
		return count;
	}

	public String getNamespaceURI(String prefix) {
		String uri = nss.getURI(prefix);
		if (uri == null) {
			uri = super.getNamespaceURI(prefix);
		}
		return uri;
	}

	public String getPrefix(String namespaceURI) {
		String prefix;
		if (namespaceURI.equals(nss.getURI(""))) {
			prefix = XMLConstants.DEFAULT_NS_PREFIX;
		} else {
			prefix = nss.getPrefix(namespaceURI);
		}
		if (prefix == null) {
			prefix = super.getPrefix(namespaceURI);
		}
		return prefix;
	}

	@SuppressWarnings("unchecked")
	public Iterator getPrefixes(String namespaceURI) {
		LinkedHashSet<String> prefixes = new LinkedHashSet<String>();
		for (Iterator i = super.getPrefixes(namespaceURI);i.hasNext();) {
			String prefix = (String) i.next();
			prefixes.add(prefix);
		}
		prefixes.addAll(Collections.list(nss.getPrefixes(namespaceURI)));
		if (namespaceURI.equals(nss.getURI(""))) {
			prefixes.add(XMLConstants.DEFAULT_NS_PREFIX);
		}
		return prefixes.iterator();
	}

}
