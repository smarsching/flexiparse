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

import java.util.Collection;

/**
 * Stores the configuration for a mapping of an XML element to a
 * Java type.
 * 
 * @author Sebastian Marsching
 */
public interface ElementMappingConfiguration extends MappingConfiguration {
    
    /**
     * Returns the (local) name of the XML element matched by this
     * configuration.
     * 
     * @return XML element name
     */
    String getElementName();
    
    /**
     * Returns the namespace URI of the XML element matched by this
     * configuration.
     * 
     * @return XML element namespace URI
     */
    String getElementNamespace();
    
    /**
     * Returns configurations for child mappings. These configurations
     * are used (in addition to the global mappings) to set attributes
     * of the Java target type of this mapping.
     * 
     * @return Mapping configurations configured "within" this configuration.
     */
    Collection<? extends MappingConfiguration> getChildMappings();
    
    /**
     * Specifies, whether to look for child elements only one level
     * below the parent element (matching this mapping) or also in deeper
     * levels.
     * 
     * @return flag indicating whether deep search is active for this mapping
     */
    boolean getDeepSearch();
    
}
