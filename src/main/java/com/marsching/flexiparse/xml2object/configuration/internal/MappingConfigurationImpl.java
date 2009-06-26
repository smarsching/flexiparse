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

import com.marsching.flexiparse.xml2object.configuration.MappingConfiguration;


public class MappingConfigurationImpl implements MappingConfiguration {
    
    protected int maxOccurs;
    protected int minOccurs;
    protected String targetAttribute;
    protected String targetType;
    protected ClassLoader targetTypeClassLoader;
    
    public int getMaxOccurs() {
        return maxOccurs;
    }
    
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }
    
    public int getMinOccurs() {
        return minOccurs;
    }
    
    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }
    
    public String getTargetAttribute() {
        return targetAttribute;
    }
    
    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public ClassLoader getTargetTypeClassLoader() {
        return targetTypeClassLoader;
    }
    
    public void setTargetTypeClassLoader(ClassLoader targetTypeClassLoader) {
        this.targetTypeClassLoader = targetTypeClassLoader;
    }
    
}
