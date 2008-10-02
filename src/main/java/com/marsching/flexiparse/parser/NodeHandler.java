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

package com.marsching.flexiparse.parser;

import com.marsching.flexiparse.configuration.HandlerConfigurationProvider;

/**
 * Instances of this interface are used by parsers to parse XML files.
 * The configuration is provided via the {@link HandlerConfigurationProvider}
 * and actual parsing is handled through the {@link ParsingHandler} interface.
 * 
 * @author Sebastian Marsching
 */
public interface NodeHandler extends HandlerConfigurationProvider,
		ParsingHandler {

}
