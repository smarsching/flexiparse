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

package com.marsching.flexiparse.xml2object.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.marsching.flexiparse.configuration.HandlerConfiguration;
import com.marsching.flexiparse.configuration.RunOrder;
import com.marsching.flexiparse.objecttree.ObjectTreeElement;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.NodeHandler;
import com.marsching.flexiparse.parser.exception.ParserException;
import com.marsching.flexiparse.xml2object.configuration.AttributeMappingConfiguration;
import com.marsching.flexiparse.xml2object.configuration.ElementMappingConfiguration;
import com.marsching.flexiparse.xml2object.configuration.MappingConfiguration;
import com.marsching.flexiparse.xml2object.configuration.TextMappingConfiguration;


/**
 * Generic node handler using mapping configurations to create
 * objects in the object tree from XML data.
 * 
 * @author Sebastian Marsching
 */
public class XML2ObjectNodeHandler implements NodeHandler {
    
    private Collection<? extends ElementMappingConfiguration> mappingConfigurations;

    /**
     * Create a new instance using the supplied mapping configurations.
     * These configurations may themselves contain other (non-global)
     * configurations.
     * 
     * @param mappingConfigurations global mapping configuration
     */
    public XML2ObjectNodeHandler(Collection<? extends ElementMappingConfiguration> mappingConfigurations) {
        this.mappingConfigurations = mappingConfigurations;
    }
    
    public HandlerConfiguration getConfiguration() {
        return new HandlerConfiguration() {

            public Collection<String> getFollowingHandlers() {
                return Collections.emptySet();
            }

            public String getIdentifier() {
                return this.getClass().getName();
            }

            public Collection<String> getPrecedingHandlers() {
                return Collections.emptySet();
            }

            public RunOrder getRunOrder() {
                return RunOrder.BOTH;
            }

            public Collection<XPathExpression> getXPathExpressions() {
                XPathFactory xpfac = XPathFactory.newInstance();
                XPath xpath = xpfac.newXPath();
                LinkedList<XPathExpression> expressions = new LinkedList<XPathExpression>();
                try {
                    expressions.add(xpath.compile("//*"));
                    expressions.add(xpath.compile("//@*"));
                    expressions.add(xpath.compile("//text()"));
                } catch (XPathExpressionException e) {
                    throw new RuntimeException("Error while compiling XPath expressions", e);
                }
                return expressions;
            }
            
        };
    }
    
    public void handleNode(HandlerContext context) throws ParserException {
        Node node = context.getNode();
        if (context.getRunOrder() == RunOrder.START) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                handleElementStart(context, (Element) node);
            } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                handleAttribute(context, (Attr) node);
            } else if (node.getNodeType() == Node.TEXT_NODE) {
                handleTextNode(context);
            }
        } else if (context.getRunOrder() == RunOrder.END) {
            XML2ObjectInfo info = null;
            Collection<? extends XML2ObjectInfo> infos = context.getObjectTreeElement().getObjectsOfType(XML2ObjectInfo.class);
            if (infos.isEmpty()) {
                return;
            } else {
                info = infos.iterator().next();
            }
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                handleTextContent(context, (Element) node, info);
                handleElementEnd(context, (Element) node, info);
            }
        }
    }

    private void handleElementStart(HandlerContext context, Element element) {
        ObjectTreeElement ote = context.getObjectTreeElement();
        XML2ObjectInfo parentInfo = findParentInfo(ote);
        ElementMappingConfiguration mappingConfig = null;
        if (parentInfo != null) {
            mappingConfig = findElementMapping(parentInfo.configuration.getChildMappings(), element);
        }
        if (mappingConfig == null) {
            mappingConfig = findElementMapping(mappingConfigurations, element);
        }
        if (mappingConfig == null) {
            // There is no configuration for this element, so it
            // can be safely ignored.
            return;
        }
        XML2ObjectInfo info = new XML2ObjectInfo();
        info.configuration = mappingConfig;
        info.parentInfo = parentInfo;
        ote.addObject(info);
    }

    private void handleElementEnd(HandlerContext context, Element element, XML2ObjectInfo info) throws ParserException {
        ObjectTreeElement ote = context.getObjectTreeElement();
        Object obj = createObjectFromInfo(info);
        if (info.parentInfo != null) {
            String targetAttribute = info.configuration.getTargetAttribute(); 
            if (targetAttribute.length() > 0) {
                int count = 0;
                if (info.parentInfo.nodeCount.containsKey(info.configuration)) {
                    count = info.parentInfo.nodeCount.get(info.configuration);
                }
                count++;
                if (count > info.configuration.getMaxOccurs() && info.configuration.getMaxOccurs() != -1) {
                    throw new ParserException("Max occurs execeeded for element " + info.configuration.getElementName() + " [" + info.configuration.getElementNamespace() + "]");
                }
                info.parentInfo.nodeCount.put(info.configuration, count);
                storeAttribute(info.parentInfo.objectAttributes, targetAttribute, obj);
            }
        } else {
            ote.addObject(obj);
        }
    }

    private void handleAttribute(HandlerContext context, Attr attr) throws ParserException {
        ObjectTreeElement ote = context.getObjectTreeElement();
        XML2ObjectInfo parentInfo = null;
        Collection<? extends XML2ObjectInfo> infos = ote.getObjectsOfType(XML2ObjectInfo.class);
        if (infos.isEmpty()) {
            // If there is no parent info, object mapping is not active
            // for the attribute's element and the attribute can be safely
            // ignored.
            return;
        } else {
            parentInfo = infos.iterator().next();
        }

        AttributeMappingConfiguration mappingConfig = null;
        mappingConfig = findAttributeMapping(parentInfo.configuration.getChildMappings(), attr);
        if (mappingConfig == null) {
            // There is no configuration for this attribute, so it
            // can be safely ignored.
            return;
        }
        Object obj = createObjectFromString(mappingConfig.getTargetType(), attr.getValue());
        // Max occurs do not have to be checked, as an attribute can only
        // appear once per element
        storeAttribute(parentInfo.objectAttributes, mappingConfig.getTargetAttribute(), obj);
    }
    
    private void handleTextNode(HandlerContext context) {
        ObjectTreeElement ote = context.getObjectTreeElement();
        XML2ObjectInfo parentInfo = null;
        Collection<? extends XML2ObjectInfo> infos = ote.getObjectsOfType(XML2ObjectInfo.class);
        if (infos.isEmpty()) {
            // If there is no parent info, object mapping is not active
            // for the attribute's element and the attribute can be safely
            // ignored.
            return;
        } else {
            parentInfo = infos.iterator().next();
        }
        parentInfo.textContent.add(context.getNode().getNodeValue());
    }
    
    private void handleTextContent(HandlerContext context, Element element, XML2ObjectInfo info) throws ParserException {
        if (info.textContent == null) {
            return;
        }
        TextMappingConfiguration config = null;
        for (MappingConfiguration mc : info.configuration.getChildMappings()) {
            if (mc instanceof TextMappingConfiguration) {
                config = (TextMappingConfiguration) mc;
                break;
            }
        }
        if (config == null) {
            return;
        }
        boolean ignoreWhiteSpace = config.getIgnoreWhiteSpaceNodes();
        int count = 0;
        if (config.getAppend()) {
            StringBuffer concat = null;
            for (String str : info.textContent) {
                if (!ignoreWhiteSpace || str.trim().length() != 0) {
                    if (concat == null) {
                        concat = new StringBuffer();
                    }
                    concat.append(str);
                    count++;
                }
            }
            if (concat != null) {
                Object obj = createObjectFromString(config.getTargetType(), concat.toString());
                storeAttribute(info.objectAttributes, config.getTargetAttribute(), obj);
            }
        } else {
            for (String str : info.textContent) {
                if (!ignoreWhiteSpace || str.trim().length() != 0) {
                    Object obj = createObjectFromString(config.getTargetType(), str);
                    storeAttribute(info.objectAttributes, config.getTargetAttribute(), obj);
                    count++;
                }
            }
        }
        if (config.getMaxOccurs() != -1 && count > config.getMaxOccurs()) {
            throw new ParserException("Found " + count + " text nodes for element " + info.configuration.getElementName() + " [" + info.configuration.getElementNamespace() + "] but expected a maximum of " + config.getMaxOccurs() + " nodes");
        }
    }
    
    private ElementMappingConfiguration findElementMapping(Collection<? extends MappingConfiguration> mappings, Element element) {
        String elementName = element.getLocalName();
        String elementNamespace = element.getNamespaceURI();
        if (elementNamespace == null) {
            elementNamespace = "";
        }
        for (MappingConfiguration mapping : mappings) {
            if (mapping instanceof ElementMappingConfiguration) {
                ElementMappingConfiguration elementMapping = (ElementMappingConfiguration) mapping;
                if (elementMapping.getElementName().equals(elementName)
                        && elementMapping.getElementNamespace().equals(elementNamespace)) {
                    return elementMapping;
                }
            }
        }
        return null;
    }

    private AttributeMappingConfiguration findAttributeMapping(Collection<? extends MappingConfiguration> mappings, Attr attr) {
        String attrName = attr.getLocalName();
        String attrNamespace = attr.getNamespaceURI();
        if (attrNamespace == null) {
            attrNamespace = "";
        }
        for (MappingConfiguration mapping : mappings) {
            if (mapping instanceof AttributeMappingConfiguration) {
                AttributeMappingConfiguration attributeMapping = (AttributeMappingConfiguration) mapping;
                if (attributeMapping.getAttributeName().equals(attrName)
                        && attributeMapping.getAttributeNamespace().equals(attrNamespace)) {
                    return attributeMapping;
                }
            }
        }
        return null;
    }

    private Object createObjectFromInfo(XML2ObjectInfo info) throws ParserException {
        ElementMappingConfiguration mappingConfig = info.configuration;
        String typeName = expandShortTypes(mappingConfig.getTargetType());
        Class<?> type = loadType(typeName);
        Object obj = null;
        // Handle special "!parent" attribute replacing the object itself
        if (info.objectAttributes.containsKey("!parent")) {
            obj = info.objectAttributes.get("!parent");
            if (!type.isAssignableFrom(obj.getClass())) {
                throw new ParserException("Type " + typeName + " is not assignable from type " + obj.getClass().getName() + " used by !parent special target attribute");
            }
        }
        // Check that required attributes are present
        for (MappingConfiguration mc : mappingConfig.getChildMappings()) {
            if (mc.getMinOccurs() > 0
                    && !info.objectAttributes.containsKey(mc.getTargetAttribute())) {
                throw new ParserException("Required attribute " + mc.getTargetAttribute() + " not present for instance of " + typeName);
            }
        }
        if (obj == null) {
            try {
                obj = type.newInstance();
            } catch (InstantiationException e) {
                throw new ParserException("Could not create instance of " + typeName, e);
            } catch (IllegalAccessException e) {
                throw new ParserException("Could not create instance of " + typeName, e);
            }
        }
        for (String attributeName : info.objectAttributes.keySet()) {
            Object attributeValue = info.objectAttributes.get(attributeName);
            if (attributeName.equals("!collectionentry")) {
                if (obj instanceof Collection) {
                    @SuppressWarnings("unchecked")
                    Collection<Object> collection = (Collection<Object>) obj;
                    @SuppressWarnings("unchecked")
                    Collection<Object> values = (Collection<Object>) attributeValue;
                    collection.addAll(values);
                } else {
                    throw new ParserException("Special target attribute !collectionentry used but type " + typeName + " does not implement java.util.Collection");
                }
            } else if (attributeName.equals("!mapentry")) {
                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> map = (Map<Object, Object>) obj;
                    @SuppressWarnings("unchecked")
                    Collection<Object> values = (Collection<Object>) attributeValue;
                    for (Object o : values) {
                        if (o instanceof Map.Entry<?, ?>) {
                            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                            map.put(entry.getKey(), entry.getValue());
                        }
                    }
                } else {
                    throw new ParserException("Special target attribute !mapentry used but type " + typeName + " does not implement java.util.Map");
                }
            } else if (attributeName.equals("!parent")) {
                // Ignore, this has already been handled above
            } else {
                callSetter(type, obj, attributeName, attributeValue);
            }
        }
        return obj;
    }
    
    private Object createObjectFromString(String type, String str) throws ParserException {
        type = expandShortTypes(type);
        if (type.equals("java.lang.String")) {
            return str;
        }
        Class<?> objType = loadType(type);
        if (Enum.class.isAssignableFrom(objType)) {
            try {
                @SuppressWarnings("unchecked")
                Enum enumValue = Enum.valueOf(objType.asSubclass(Enum.class), str);
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new ParserException("Invalid value " + str + " specified for enum type " + type);
            }
        }
        for (Constructor<?> constructor : objType.getConstructors()) {
            if (constructor.getParameterTypes().length == 1
                    && constructor.getParameterTypes()[0].isAssignableFrom(String.class)) {
                try {
                    return constructor.newInstance(str);
                } catch (IllegalArgumentException e) {
                    throw new ParserException("Could not create instance of " + type, e);
                } catch (InstantiationException e) {
                    throw new ParserException("Could not create instance of " + type, e);
                } catch (IllegalAccessException e) {
                    throw new ParserException("Could not create instance of " + type, e);
                } catch (InvocationTargetException e) {
                    throw new ParserException("Could not create instance of " + type, e);
                }
            }
        }
        throw new ParserException("No constructor taking a single String argument found for " + type);
    }
    
    private XML2ObjectInfo findParentInfo(ObjectTreeElement ote) {
        XML2ObjectInfo parentInfo = null;
        ObjectTreeElement parentOte = ote.getParent();
        while (parentOte != null) {
            Collection<? extends XML2ObjectInfo> infos = parentOte.getObjectsOfType(XML2ObjectInfo.class);
            if (!infos.isEmpty()) {
                parentInfo = infos.iterator().next();
                // Make sure that only direct parent element is used when
                // deep search is not active
                if (!parentInfo.configuration.getDeepSearch() && parentOte != ote.getParent()) {
                    parentInfo = null;
                }
                break;
            }
            parentOte = parentOte.getParent();
        }
        return parentInfo;
    }
    
    private String expandShortTypes(String type) {
        return expandShortTypes(type, true);
    }
    
    private String expandShortTypes(String type, boolean enableSpecialTypes) {
        if (type.equals("boolean")) {
            type = "java.lang.Boolean";
        } else if (type.equals("byte")) {
            type = "java.lang.Byte";
        } else if (type.equals("char")) {
            type = "java.lang.Character";
        } else if (type.equals("double")) {
            type = "java.lang.Double";
        } else if (type.equals("float")) {
            type = "java.lang.Float";
        } else if (type.equals("int")) {
            type = "java.lang.Integer";
        } else if (type.equals("long")) {
            type = "java.lang.Long";
        } else if (type.equals("short")) {
            type = "java.lang.Short";
        } else if (enableSpecialTypes && type.equals("!mapentry")) {
            // Special internal type for map entries
            type = "com.marsching.flexiparse.xml2object.parser.internal.MapEntry";
        }
        return type;
    }
    
    private Class<?> loadType(String type) throws ParserException {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new ParserException("Could not create instance of " + type, e);
        }
    }
    
    private void callSetter(Class<?> type, Object obj, String name, Object value) throws ParserException {
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        for (Method m : type.getMethods()) {
            if (!m.getName().equals(methodName)) {
                continue;
            }
            if (m.getParameterTypes().length == 1
                    && assignableTypes(m.getParameterTypes()[0], value.getClass())) {
                try {
                    m.invoke(obj, value);
                    return;
                } catch (IllegalArgumentException e) {
                    throw new ParserException("Error while trying to set attribute " + name + " on instance of " + type.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new ParserException("Error while trying to set attribute " + name + " on instance of " + type.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new ParserException("Error while trying to set attribute " + name + " on instance of " + type.getName(), e);
                }
            }
        }
        throw new ParserException("Could not find setter method for attribute " + name + " in type " + type.getName());
    }
    
    /**
     * Checks whether object of type class2 can be assigned to object of 
     * type class2.
     * 
     * @param class1 type of target object 
     * @param class2 type of source object
     * @return <code>true</code> if and only if class2 can be assigned to class1
     */
    private boolean assignableTypes(Class<?> class1, Class<?> class2) {
        if (class1.isPrimitive() && class2.isPrimitive()) {
            return class1.equals(class2);
        } else if (!class1.isPrimitive() && !class2.isPrimitive()) {
            return class1.isAssignableFrom(class2);
        } else {
            Class<?> primitiveClass;
            Class<?> complexClass;
            if (class1.isPrimitive()) {
                primitiveClass = class1;
                complexClass = class2;
            } else {
                primitiveClass = class2;
                complexClass = class1;
            }
            String primitiveName = primitiveClass.getName();
            String complexName = complexClass.getName();
            if (expandShortTypes(primitiveName, false).equals(complexName)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void storeAttribute(Map<String, Object> attributesMap, String attributeName, Object attributeValue) {
        if (attributeName.equals("!mapentry") || attributeName.equals("!collectionentry")) {
            // There can be multiple values for this special
            // attribute, so store them in a collection
            @SuppressWarnings("unchecked")
            Collection<Object> collection = (Collection<Object>) attributesMap.get(attributeName);
            if (collection == null) {
                collection = new LinkedList<Object>();
                attributesMap.put(attributeName, collection);
            }
            collection.add(attributeValue);
        } else {
            attributesMap.put(attributeName, attributeValue);
        }
    }
    
    private class XML2ObjectInfo {
        public XML2ObjectInfo parentInfo = null;
        public ElementMappingConfiguration configuration;
        public HashMap<String, Object> objectAttributes = new HashMap<String, Object>();
        public List<String> textContent = new LinkedList<String>();
        public HashMap<MappingConfiguration, Integer> nodeCount = new HashMap<MappingConfiguration, Integer>();
    }
    
}
