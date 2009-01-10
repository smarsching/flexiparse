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
 * Common base interface for all mapping configurations.  
 * 
 * @author Sebastian Marsching
 */
public interface MappingConfiguration {
    
    /**
     * Returns the name of the type the data matched by this configuration
     * is mapped to. This might be the fully qualified class name of an
     * arbitrary Java type, one of the Java primitive types or the
     * special type "!mapentry", which will create an instance of Map.Entry.
     * 
     * @return name of the Java target type of this mapping
     */
    String getTargetType();
    
    /**
     * Returns the name of the attribute that should be set to the value
     * represented by the data matching this mapping. For global mappings
     * (which have no parents and therefore no object with attributes that
     * can be set) this method returns the empty string.
     * 
     * @return name of the target attribute or empty string for global mappings
     */
    String getTargetAttribute();
    
    /**
     * Returns the minimum number of times a node matched by this
     * mapping has to appear. For global mappings this is always zero, as
     * such elements are always optional.
     * 
     * @return Minimum occurrences for the node match by this mapping
     */
    int getMinOccurs();
    
    /**
     * Returns the maximum number of times a node matched by this
     * mapping can appear. For attribute mappings this is always one.
     * A value of -1 specifies that the maximum is unlimited.
     * 
     * @return Maximum occurrences for the node match by this mapping
     */
    int getMaxOccurs();
    
}
