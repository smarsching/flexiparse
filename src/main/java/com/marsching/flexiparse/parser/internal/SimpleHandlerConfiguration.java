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

package com.marsching.flexiparse.parser.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpression;

import com.marsching.flexiparse.configuration.HandlerConfiguration;
import com.marsching.flexiparse.configuration.RunOrder;

public class SimpleHandlerConfiguration implements HandlerConfiguration {
    Set<String> followingModules = new HashSet<String>();
    Set<String> precedingModules = new HashSet<String>();
    String identifier;
    String className;
    Set<XPathExpression> expressions = new HashSet<XPathExpression>();
    RunOrder runOrder;
    
    public Collection<String> getFollowingHandlers() {
        return Collections.unmodifiableCollection(followingModules);
    }

    public String getIdentifier() {
        return identifier;
    }

    public Collection<String> getPrecedingHandlers() {
        return Collections.unmodifiableCollection(precedingModules);
    }

    public Collection<XPathExpression> getXPathExpressions() {
        return Collections.unmodifiableCollection(expressions);
    }

    public RunOrder getRunOrder() {
        return runOrder;
    }
    
    public String getClassName() {
        return className;
    }
}