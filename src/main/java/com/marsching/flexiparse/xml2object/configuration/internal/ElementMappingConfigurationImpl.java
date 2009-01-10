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

import java.util.Collection;

import com.marsching.flexiparse.xml2object.configuration.ElementMappingConfiguration;
import com.marsching.flexiparse.xml2object.configuration.MappingConfiguration;


public class ElementMappingConfigurationImpl extends MappingConfigurationImpl implements ElementMappingConfiguration {
    
    private String elementName;
    private String elementNamespace;
    private boolean deepSearch;
    private Collection<? extends MappingConfiguration> childMappings;
    
    public Collection<? extends MappingConfiguration> getChildMappings() {
        return childMappings;
    }
    
    public void setChildMappings(Collection<? extends MappingConfiguration> childMappings) {
        this.childMappings = childMappings;
    }
    
    public String getElementName() {
        return elementName;
    }
    
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    
    public String getElementNamespace() {
        return elementNamespace;
    }
    
    public void setElementNamespace(String elementNamespace) {
        this.elementNamespace = elementNamespace;
    }
    
    public boolean getDeepSearch() {
        return deepSearch;
    }
    
    public void setDeepSearch(boolean deepSearch) {
        this.deepSearch = deepSearch;
    }
    
}
