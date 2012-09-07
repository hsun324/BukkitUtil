package com.gmail.klezst.bukkit.bukkitutil.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AliasMap implements Map<String[], Descriptor>
{
	protected List<Descriptor> descriptors = new ArrayList<Descriptor>();
	
	public int size()
	{
		return descriptors.size();
	}
	public boolean isEmpty()
	{
		return size() <= 0;
	}
	public boolean containsKey(Object key)
	{
		if(key instanceof String)
			for(Descriptor descriptor : descriptors)
				for(String alias : descriptor.getAliases())
					if(alias.equalsIgnoreCase((String) key))
						return true;
		return false;
	}
	public boolean containsValue(Object value)
	{
		return descriptors.contains(value);
	}
	public Descriptor get(Object key)
	{
		if(key instanceof String)
			for(Descriptor descriptor : descriptors)
				for(String alias : descriptor.getAliases())
					if(alias.equalsIgnoreCase((String) key))
						return descriptor;
		return null;
	}
	public Descriptor put(String[] keys, Descriptor value)
	{
		int index = descriptors.indexOf(value);
		if(index == -1)
		{
			descriptors.add(value);
			return null;
		}
		return descriptors.set(index, value);
	}
	public Descriptor remove(Object key)
	{
		if(key instanceof String)
			for(Descriptor descriptor : descriptors)
				for(String alias : descriptor.getAliases())
					if(alias.equalsIgnoreCase((String) key))
						descriptors.remove(descriptor);
		return null;
	}
	public void putAll(Map<? extends String[], ? extends Descriptor> m)
	{
		throw new UnsupportedOperationException();
	}
	public void clear()
	{
		descriptors.clear();
	}
	public Set<String[]> keySet()
	{
		throw new UnsupportedOperationException();
	}
	public Collection<Descriptor> values()
	{
		return descriptors;
	}
	public Set<java.util.Map.Entry<String[], Descriptor>> entrySet()
	{
		throw new UnsupportedOperationException();
	}
}
