package com.gmail.klezst.bukkit.bukkitutil.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.klezst.bukkit.bukkitutil.command.descriptors.ActionDescriptor;
import com.gmail.klezst.bukkit.bukkitutil.command.descriptors.CommandDescriptor;

public class ConfigurationParser
{
	protected final Configuration config;
	public ConfigurationParser(Configuration config)
	{
		if(config == null)
			throw new NullPointerException("config is null");
		this.config = config;
	}
	
	public CommandDescriptor[] parse()
	{
		List<CommandDescriptor> commands = new ArrayList<CommandDescriptor>();
		
		Set<String> roots = config.getKeys(false);
		if(roots != null)
			for(String root : roots)
			{
				if(config.isConfigurationSection(root))
				{
					CommandDescriptor command = new CommandDescriptor(root);
					parseCommand(command, config.getConfigurationSection(root));
				}
			}
		
		return commands.toArray(new CommandDescriptor[0]);
	}

	private boolean parseCommand(CommandDescriptor command, ConfigurationSection config)
	{
		List<String> aliases = config.getStringList("name");
		if(aliases == null)
			return false;
		command.setAliases(aliases.toArray(new String[0]));
		
		String bound = config.getString("class");
		if(bound == null)
			return false;
		command.setBound(bound);
		
		String help = config.getString("help");
		if(help == null)
			return false;
		command.setHelp(help);
		
		boolean result = true;
		Set<String> subs = config.getKeys(false);
		if(subs != null)
			for(String sub : subs)
			{
				if(config.isConfigurationSection(sub))
				{
					ActionDescriptor action = new ActionDescriptor(sub);
					result = parseAction(action, config.getConfigurationSection(sub)) && result;
				}
				else
					result = false;
			}
		else
			result = false;
		return result;
	}

	private boolean parseAction(ActionDescriptor action, ConfigurationSection configurationSection)
	{
		List<String> aliases = config.getStringList("name");
		if(aliases == null)
			return false;
		action.setAliases(aliases.toArray(new String[0]));
		
		String bound = config.getString("method");
		if(bound != null)
		{
			action.setBound(bound);
			action.setRange(new int[]{config.getInt("maxArg", -1), config.getInt("minArg", Integer.MIN_VALUE)});
			return true;
		}
		else
		{
			boolean result = true;
			Set<String> subs = config.getKeys(false);
			if(subs != null)
				for(String sub : subs)
				{
					if(config.isConfigurationSection(sub))
					{
						ActionDescriptor subaction = new ActionDescriptor(sub);
						result = parseAction(subaction, config.getConfigurationSection(sub)) && result;
					}
					else
						result = false;
				}
			else
				return false;
			return result;
		}
	}
}
