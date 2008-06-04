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

package com.marsching.flexiparse.configuration;

import java.util.Collection;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;

import com.marsching.flexiparse.parser.NodeHandler;

/**
 * Contains configuration information for a {@link NodeHandler}.
 * 
 * @author Sebastian Marsching
 * @see NodeHandler
 */
public interface HandlerConfiguration {
	
	/**
	 * Returns a globally unique identifier for the handler.
	 * This identifier is being used to reference this handler
	 * (e.g. in the configuration of other handlers).
	 * 
	 * @return Globally unique id identifying the handler 
	 * associated with this configuration
	 */
	public String getIdentifier();
	
	/**
	 * Returns a list of handlers that, if present, should be
	 * called before the handler associated with this configuration.
	 * This only affects the invocation order for a single node.
	 * If a referenced handler is not triggered on the same node 
	 * the handler associated with this configuration is triggered
	 * on, referencing that handler here will have no effect.
	 * 
	 * @return list containing identifiers of handlers that
	 * - if called on the same node - must be called before this handler
	 */
	public Collection<String> getPrecedingHandlers();
	
	/**
	 * Returns a list of handlers that, if present, should be
	 * called after the handler associated with this configuration.
	 * This only affects the invocation order for a single node.
	 * If a referenced handler is not triggered on the same node 
	 * the handler associated with this configuration is triggered
	 * on, referencing that handler here will have no effect.
	 * 
	 * @return list containing identifiers of handlers that
	 * - if called on the same node - must be called after this handler
	 */
	public Collection<String> getFollowingHandlers();
	
	/**
	 * Returns a list of {@link XPathExpression}s that will be used
	 * to match nodes in the parsed document. If a node in the 
	 * parsed document matches at least one of the XPathExpressions,
	 * the associated handler will be called. The evaluation context
	 * used when evaluating the XPathExpressions is always the 
	 * {@link Document} node.
	 * 
	 * @return list of XPathExpressions used to find matching nodes
	 */
	public Collection<XPathExpression> getXPathExpressions();
	
	/**
	 * Indicates whether to run the handler before or after 
	 * processing the child nodes of the node matched by this
	 * handler.
	 * 
	 * @return run order for this handler
	 */
	public RunOrder getRunOrder();
}
