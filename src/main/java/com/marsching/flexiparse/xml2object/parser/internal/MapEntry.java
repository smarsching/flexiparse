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

package com.marsching.flexiparse.xml2object.parser.internal;

import java.util.Map.Entry;


public class MapEntry<K, V> implements Entry<K, V> {
    
    private K key;
    private V value;
    
    public K getKey() {
        return key;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
    
    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    
}
