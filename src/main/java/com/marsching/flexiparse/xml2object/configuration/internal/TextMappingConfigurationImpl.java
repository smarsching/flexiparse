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

import com.marsching.flexiparse.xml2object.configuration.TextMappingConfiguration;


public class TextMappingConfigurationImpl extends MappingConfigurationImpl implements TextMappingConfiguration {
    
    private boolean append = false;
    private boolean ignoreWhiteSpace = false;
    
    public boolean getAppend() {
        return append;
    }
    
    public void setAppend(boolean append) {
        this.append = append;
    }
    
    public boolean getIgnoreWhiteSpaceNodes() {
        return ignoreWhiteSpace;
    }
    
    public void setIgnoreWhiteSpaceNodes(boolean ignoreWhiteSpace) {
        this.ignoreWhiteSpace = ignoreWhiteSpace;
    }
    
}
