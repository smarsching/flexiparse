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

package com.marsching.flexiparse.xml2object.configuration;


/**
 * Stores configuration for a mapping from an XML text node to a Java type. 
 * 
 * @author Sebastian Marsching
 */
public interface TextMappingConfiguration extends MappingConfiguration {
    
    /**
     * Returns <code>true</code> if several text nodes (in the same context)
     * should be concatenated in order to build the final string and
     * <code>false</code> if following text nodes should be handled
     * independently.
     *  
     * @return flag indicating whether to append text nodes within the same
     *   context.
     */
    boolean getAppend();
    
    /**
     * Returns <code>true</code> if text nodes that contain white space only,
     * should be ignored and <code>false</code> otherwise.
     * 
     * @return flag indicating whether to ignore nodes only containing 
     *   whitespace
     */
    boolean getIgnoreWhiteSpaceNodes();
    
}
