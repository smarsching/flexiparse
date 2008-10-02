package com.marsching.flexiparse.parser.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Map2D <T1, T2, T3> {
	T3 defaultValue = null;
	HashMap<T1, HashMap<T2, T3>> map = new HashMap<T1, HashMap<T2,T3>>();
	
	public Map2D() {
		
	}
	
	public Map2D(T3 defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void set(T1 i, T2 j, T3 v) {
		HashMap<T2, T3> subMap = map.get(i);
		if (subMap == null) {
			subMap = new HashMap<T2, T3>();
			map.put(i, subMap);
		}
		subMap.put(j, v);
	}
	
	public T3 get(T1 i, T2 j) {
		HashMap<T2, T3> subMap = map.get(i);
		if (subMap == null) {
			return defaultValue;
		}
		T3 v = subMap.get(j);
		if (v == null) {
			v = defaultValue;
		}
		return v;
	}
	
	public Map<T2, T3> getRow(T1 i) {
		Map<T2, T3> subMap = map.get(i);
		if (subMap == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(subMap);
	}
	
	public Map<T1, T3> getColumn(T2 j) {
		HashMap<T1, T3> resultMap = new HashMap<T1, T3>();
		for (T1 i : map.keySet()) {
			Map<T2, T3> subMap = map.get(i);
			if (subMap.containsKey(j)) {
				resultMap.put(i, subMap.get(j));
			}
		}
		return resultMap;
	}
	
	public void deleteRow(T1 i) {
		map.remove(i);
	}
	
	public void deleteColumn(T2 j) {
		for (Map<T2, T3> subMap : map.values()) {
			subMap.remove(j);
		}
	}
	
	public Collection<Map<T2, T3>> getRows() {
		HashSet<Map<T2, T3>> rows = new HashSet<Map<T2, T3>>();
		for (Map<T2, T3> row : map.values()) {
			rows.add(row);
		}
		return Collections.unmodifiableCollection(rows);
	}
	
	public Collection<T1> getRowKeys() {
		return Collections.unmodifiableCollection(map.keySet());
	}
}
