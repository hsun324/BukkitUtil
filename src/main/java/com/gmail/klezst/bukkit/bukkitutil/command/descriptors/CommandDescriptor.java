package com.gmail.klezst.bukkit.bukkitutil.command.descriptors;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.gmail.klezst.bukkit.bukkitutil.command.AliasMap;
import com.gmail.klezst.bukkit.bukkitutil.command.Descriptor;

public class CommandDescriptor implements Descriptor
{
	protected final String commandName;
	protected final AliasMap actions = new AliasMap();
	protected String[] aliases = new String[0];
	private String bound = null;
	private String help = null;
	
	private Class<?> boundClass = null;
	
	public CommandDescriptor(String name)
	{
		commandName = name;
	}
	public String getName()
	{
		return commandName;
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
	
	public String getBound()
	{
		return bound;
	}
	public void setBound(String bound)
	{
		try
		{
			boundClass = Class.forName(bound);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException("A plugin attempted to use a nonexistent " + bound + " as a base class.", e);
		}
		this.bound = bound;
	}
	
	public Class<?> getBoundClass()
	{
		return boundClass;
	}
	
	public String getHelp()
	{
		return help;
	}
	public void setHelp(String help)
	{
		this.help = help;
	}

	public boolean activate(CommandSender sender, Command command, String name,
			List<Descriptor> actionTree, String[] args)
	{
		return false;
	}
}
