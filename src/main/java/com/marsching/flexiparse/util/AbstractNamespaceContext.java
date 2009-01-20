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
	
    /**
     * <p>Get Namespace URI bound to a prefix in the current scope.</p>
     *
     * <p>When requesting a Namespace URI by prefix, the following
     * table describes the returned Namespace URI value for all
     * possible prefix values:</p>
     *
     * <table border="2" rules="all" cellpadding="4">
     *   <thead>
     *     <tr>
     *       <td align="center" colspan="2">
     *         <code>getNamespaceURI(prefix)</code>
     *         return value for specified prefixes
     *       </td>
     *     </tr>
     *     <tr>
     *       <td>prefix parameter</td>
     *       <td>Namespace URI return value</td>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td><code>DEFAULT_NS_PREFIX</code> ("")</td>
     *       <td>default Namespace URI in the current scope or
     *         <code>{@link
     *         javax.xml.XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}
     *         </code>
     *         when there is no default Namespace URI in the current scope</td>
     *     </tr>
     *     <tr>
     *       <td>bound prefix</td>
     *       <td>Namespace URI bound to prefix in current scope</td>
     *     </tr>
     *     <tr>
     *       <td>unbound prefix</td>
     *       <td>
     *         <code>{@link
     *         javax.xml.XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}
     *         </code>
     *       </td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
     *       <td><code>XMLConstants.XML_NS_URI</code>
     *           ("http://www.w3.org/XML/1998/namespace")</td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
     *       <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code>
     *         ("http://www.w3.org/2000/xmlns/")</td>
     *     </tr>
     *     <tr>
     *       <td><code>null</code></td>
     *       <td><code>IllegalArgumentException</code> is thrown</td>
     *     </tr>
     *    </tbody>
     * </table>
     *
     * @param prefix prefix to look up
     *
     * @return Namespace URI bound to prefix in the current scope
     *
     * @throws IllegalArgumentException When <code>prefix</code> is
     *   <code>null</code>
     */
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

	/**
     * <p>Get prefix bound to Namespace URI in the current scope.</p>
     *
     * <p>To get all prefixes bound to a Namespace URI in the current
     * scope, use {@link #getPrefixes(String namespaceURI)}.</p>
     *
     * <p>When requesting a prefix by Namespace URI, the following
     * table describes the returned prefix value for all Namespace URI
     * values:</p>
     *
     * <table border="2" rules="all" cellpadding="4">
     *   <thead>
     *     <tr>
     *       <th align="center" colspan="2">
     *         <code>getPrefix(namespaceURI)</code> return value for
     *         specified Namespace URIs
     *       </th>
     *     </tr>
     *     <tr>
     *       <th>Namespace URI parameter</th>
     *       <th>prefix value returned</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td>&lt;default Namespace URI&gt;</td>
     *       <td><code>XMLConstants.DEFAULT_NS_PREFIX</code> ("")
     *       </td>
     *     </tr>
     *     <tr>
     *       <td>bound Namespace URI</td>
     *       <td>prefix bound to Namespace URI in the current scope,
     *           if multiple prefixes are bound to the Namespace URI in
     *           the current scope, a single arbitrary prefix, whose
     *           choice is implementation dependent, is returned</td>
     *     </tr>
     *     <tr>
     *       <td>unbound Namespace URI</td>
     *       <td><code>null</code></td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XML_NS_URI</code>
     *           ("http://www.w3.org/XML/1998/namespace")</td>
     *       <td><code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code>
     *           ("http://www.w3.org/2000/xmlns/")</td>
     *       <td><code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
     *     </tr>
     *     <tr>
     *       <td><code>null</code></td>
     *       <td><code>IllegalArgumentException</code> is thrown</td>
     *     </tr>
     *   </tbody>
     * </table>
     *
     * @param namespaceURI URI of Namespace to lookup
     *
     * @return prefix bound to Namespace URI in current context
     *
     * @throws IllegalArgumentException When <code>namespaceURI</code> is
     *   <code>null</code>
     */
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

	/**
     * <p>Get all prefixes bound to a Namespace URI in the current
     * scope.</p>
     *
     * <p>An Iterator over String elements is returned in an arbitrary,
     * <strong>implementation dependent</strong>, order.</p>
     *
     * <p><strong>The <code>Iterator</code> is
     * <em>not</em> modifiable.  e.g. the
     * <code>remove()</code> method will throw
     * <code>UnsupportedOperationException</code>.</strong></p>
     *
     * <p>When requesting prefixes by Namespace URI, the following
     * table describes the returned prefixes value for all Namespace
     * URI values:</p>
     *
     * <table border="2" rules="all" cellpadding="4">
     *   <thead>
     *     <tr>
     *       <th align="center" colspan="2"><code>
     *         getPrefixes(namespaceURI)</code> return value for
     *         specified Namespace URIs</th>
     *     </tr>
     *     <tr>
     *       <th>Namespace URI parameter</th>
     *       <th>prefixes value returned</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td>bound Namespace URI,
     *         including the &lt;default Namespace URI&gt;</td>
     *       <td>
     *         <code>Iterator</code> over prefixes bound to Namespace URI in
     *         the current scope in an arbitrary,
     *         <strong>implementation dependent</strong>,
     *         order
     *       </td>
     *     </tr>
     *     <tr>
     *       <td>unbound Namespace URI</td>
     *       <td>empty <code>Iterator</code></td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XML_NS_URI</code>
     *           ("http://www.w3.org/XML/1998/namespace")</td>
     *       <td><code>Iterator</code> with one element set to
     *         <code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
     *     </tr>
     *     <tr>
     *       <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code>
     *           ("http://www.w3.org/2000/xmlns/")</td>
     *       <td><code>Iterator</code> with one element set to
     *         <code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
     *     </tr>
     *     <tr>
     *       <td><code>null</code></td>
     *       <td><code>IllegalArgumentException</code> is thrown</td>
     *     </tr>
     *   </tbody>
     * </table>
     *
     * @param namespaceURI URI of Namespace to lookup
     *
     * @return <code>Iterator</code> for all prefixes bound to the
     *   Namespace URI in the current scope
     *
     * @throws IllegalArgumentException When <code>namespaceURI</code> is
     *   <code>null</code>
     */
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
