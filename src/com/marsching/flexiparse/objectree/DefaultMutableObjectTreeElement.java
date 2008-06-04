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

package com.marsching.flexiparse.objectree;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Default implementation of the {@link MutableObjectTreeElement}
 * interface. Uses {@link LinkedHashSet}s to store children nodes 
 * and attached objects.
 * 
 * @author Sebastian Marsching
 */
public class DefaultMutableObjectTreeElement implements
		MutableObjectTreeElement {
	
	private Set<MutableObjectTreeElement> children = new LinkedHashSet<MutableObjectTreeElement>();
	private Set<Object> objects = new LinkedHashSet<Object>();
	private MutableObjectTreeElement parent;
	
	/**
	 * Creates a new instance setting the parent property using
	 * the supplied argument. The node will not automatically be
	 * registered as a child on the parent object. This action has
	 * to be performed by the code using this implementation.
	 * 
	 * @param parent object representing the parent node of this node
	 * or <code>null</code> to create a root node
	 */
	public DefaultMutableObjectTreeElement(MutableObjectTreeElement parent) {
		this.parent = parent;
	}
	
	public void addChild(MutableObjectTreeElement child) {
		if (!children.contains(child)) {
			children.add(child);
		}
	}
	
	public boolean removeChild(MutableObjectTreeElement child) {
		return children.remove(child);
	}

	public void addObject(Object object) {
		objects.add(object);
	}

	public Collection<? extends MutableObjectTreeElement> getChildren() {
		return Collections.unmodifiableCollection(children);
	}

	public <T> T getFirstObjectOfType(Class<? extends T> type) {
		Iterator<T> i = getObjectsOfType(type).iterator();
		if (i.hasNext()) {
			return i.next();
		} else {
			return null;
		}
		
	}

	public <T> T getFirstObjectOfTypeFromTree(Class<? extends T> type) {
		Iterator<T> i = getObjectsOfTypeFromTopTree(type).iterator();
		if (i.hasNext()) {
			return i.next();
		} else {
			return null;
		}
	}

	public Collection<Object> getObjects() {
		return Collections.unmodifiableCollection(objects);
	}

	public <T> Collection<T> getObjectsOfType(Class<? extends T> type) {
		LinkedList<T> list = new LinkedList<T>();
		for (Object o : objects) {
			if (type.isAssignableFrom(o.getClass())) {
				T t = type.cast(o);
				list.add(t);
			}
		}
		return Collections.unmodifiableCollection(list);
	}

	public <T> Collection<T> getObjectsOfTypeFromTopTree(Class<? extends T> type) {
		LinkedHashSet<T> set = new LinkedHashSet<T>();
		set.addAll(getObjectsOfType(type));
		if (parent != null) {
			set.addAll(parent.getObjectsOfTypeFromTopTree(type));
		}
		return Collections.unmodifiableCollection(set);
	}

	public MutableObjectTreeElement getParent() {
		return parent;
	}

	public boolean removeObject(Object object) {
		return objects.remove(object);
	}

	public <T> Collection<T> getObjectsOfTypeFromSubTree(Class<? extends T> type) {
		LinkedHashSet<T> set = new LinkedHashSet<T>();
		for (Object o : objects) {
			if (type.isAssignableFrom(o.getClass())) {
				T t = type.cast(o);
				set.add(t);
			}
			if (o instanceof SubObjectTree) {
				SubObjectTree subTree = (SubObjectTree) o;
				if (subTree.getRoot() != null) {
					set.addAll(subTree.getRoot().getObjectsOfTypeFromSubTree(type));
				}
			}
		}
		for (ObjectTreeElement o : children) {
			set.addAll(o.getObjectsOfTypeFromSubTree(type));
		}
		return Collections.unmodifiableCollection(set);
	}

    public ObjectTreeElement getRoot() {
        if (this.getParent() == null) {
            return this;
        } else {
            return getParent().getRoot();
        }
    }

}
