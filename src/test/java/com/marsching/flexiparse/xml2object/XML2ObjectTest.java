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

package com.marsching.flexiparse.xml2object;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.marsching.flexiparse.objectree.ObjectTreeElement;
import com.marsching.flexiparse.parser.ClasspathConfiguredParser;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.xml2object.internal.TestObjectA;

/**
 * Tests for the automatic XML to Java object mapping support.
 * 
 * @author Sebastian Marsching
 */
public class XML2ObjectTest {
    @Test
    public void testSimpleAttribute() throws ParserException {
        ClasspathConfiguredParser parser = new ClasspathConfiguredParser("com/marsching/flexiparse/xml2object/test_simple_attribute_config.xml");
        ObjectTreeElement root = parser.parse(this.getClass().getClassLoader().getResourceAsStream("com/marsching/flexiparse/xml2object/test_simple_attribute_document.xml"));
        Collection<TestObjectA> coll = root.getObjectsOfTypeFromSubTree(TestObjectA.class);
        TestObjectA a = coll.iterator().next();
        assertEquals("test1", a.getA());
        assertEquals("test2", a.getB().toString());
    }
    
    @Test(expected=ParserException.class)
    public void testMissingRequiredAttribute() throws ParserException {
        ClasspathConfiguredParser parser = new ClasspathConfiguredParser("com/marsching/flexiparse/xml2object/test_missing_required_attribute_config.xml");
        ObjectTreeElement root = parser.parse(this.getClass().getClassLoader().getResourceAsStream("com/marsching/flexiparse/xml2object/test_missing_required_attribute_document.xml"));
        Collection<TestObjectA> coll = root.getObjectsOfTypeFromSubTree(TestObjectA.class);
        @SuppressWarnings("unused")
        TestObjectA a = coll.iterator().next();
    }
    
    @Test
    public void testStringMap() throws ParserException {
        ClasspathConfiguredParser parser = new ClasspathConfiguredParser("com/marsching/flexiparse/xml2object/test_string_map_config.xml");
        ObjectTreeElement root = parser.parse(this.getClass().getClassLoader().getResourceAsStream("com/marsching/flexiparse/xml2object/test_string_map_document.xml"));
        @SuppressWarnings("unchecked")
        Map<String, String> map = root.getObjectsOfTypeFromSubTree(Map.class).iterator().next();
        assertEquals("foo1", map.get("test1"));
        assertEquals("foo2", map.get("test2"));
    }
    
    @Test
    public void testStringCollection() throws ParserException {
        ClasspathConfiguredParser parser = new ClasspathConfiguredParser("com/marsching/flexiparse/xml2object/test_string_collection_config.xml");
        ObjectTreeElement root = parser.parse(this.getClass().getClassLoader().getResourceAsStream("com/marsching/flexiparse/xml2object/test_string_collection_document.xml"));
        @SuppressWarnings("unchecked")
        Collection<String> coll = root.getObjectsOfTypeFromSubTree(Collection.class).iterator().next();
        assertTrue(coll.contains("foo1"));
        assertTrue(coll.contains("foo2"));
    }
    
}
