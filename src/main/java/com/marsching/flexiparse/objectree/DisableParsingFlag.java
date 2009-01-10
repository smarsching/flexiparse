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

package com.marsching.flexiparse.objectree;


/**
 * Flag class for controlling the parser using the object tree.
 * If an instance of this class is attached to a node in the object
 * tree during the <code>START</code> phase of the processing of the
 * corresponding XML element, the child elements of the XML element are
 * not processed. If the instance is added later (e.g. while processing one
 * of the child elements or during the <code>END</code> phase), it has no
 * effect.
 * 
 * @author Sebastian Marsching
 */
public class DisableParsingFlag {
    
}
