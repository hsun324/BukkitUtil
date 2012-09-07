package com.gmail.klezst.bukkit.bukkitutil.command.descriptors;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.gmail.klezst.bukkit.bukkitutil.command.AliasMap;
import com.gmail.klezst.bukkit.bukkitutil.command.Descriptor;

public class ActionDescriptor implements Descriptor
{
	protected final String actionName;
	protected final AliasMap actions = new AliasMap();
	protected String[] aliases = new String[0];
	protected int[] range = new int[]{0, -1};
	private String bound;
	
	public ActionDescriptor(String name)
	{
		actionName = name;
	}
	public String getName()
	{
		return actionName;
	}

	public String[] getAliases()
	{
		return aliases;
	}
	
	public void setAliases(String[] aliases)
	{
		this.aliases = aliases;
	}

	public Descriptor[] getActions()
	{
		return actions.values().toArray(new Descriptor[0]);
	}

	public void addAction(ActionDescriptor action)
	{
		actions.put(action.getAliases(), action);
	}

	public void removeAction(String name)
	{
		actions.remove(name);
	}
	public int[] getRange()
	{
		return range;
	}
	public void setRange(int[] range)
	{
		if(range.length != 2)
			throw new RuntimeException("range doesn't have exactly two elements");
		this.range = range;
	}
	
	public String getBound()
	{
		return bound;
	}
	public void setBound(String bound)
	{
		this.bound = bound;
	}

	public boolean activate(CommandSender sender, Command command, String name,
			List<Descriptor> actionTree, String[] args)
	{
		return false;
	}
}
