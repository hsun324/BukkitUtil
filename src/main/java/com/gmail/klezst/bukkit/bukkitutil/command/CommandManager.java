package com.gmail.klezst.bukkit.bukkitutil.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.SimplePluginManager;

import com.gmail.klezst.bukkit.bukkitutil.command.descriptors.ActionDescriptor;
import com.gmail.klezst.bukkit.bukkitutil.command.descriptors.CommandDescriptor;

public class CommandManager
{

	public static class OPluginCommand extends Command
	{

		protected OPluginCommand(String name, String description, String usageMessage, List<String> aliases)
		{
			super(name, description, usageMessage, aliases);
		}

		@Override
		public boolean execute(CommandSender sender, String name, String[] args)
		{
			Descriptor desc = registeredCommands.get(name);
			if(desc != null && desc instanceof CommandDescriptor)
			{
				CommandDescriptor commandDesc = (CommandDescriptor) desc;
				Class<?> boundClass = commandDesc.getBoundClass();
				
				List<Descriptor> tree = new ArrayList<Descriptor>();
				tree.add(desc);
				
				for(Descriptor subDesc : commandDesc.getActions())
					if(subDesc != null && subDesc instanceof ActionDescriptor)
						for(String alias : subDesc.getAliases())
							if(alias.equalsIgnoreCase(args[0]) || alias.equalsIgnoreCase("default"))
							{
								List<Descriptor> subTree = new ArrayList<Descriptor>(tree);
								tree.add(subDesc);
								
								executeAction(boundClass, sender, name, args, 1, (ActionDescriptor) subDesc, subTree);
							}
				return true;
			}
			return false;
		}

		private void executeAction(Class<?> boundClass, CommandSender sender, String name, String[] args, int index, ActionDescriptor desc, List<Descriptor> tree)
		{
			if(desc.getBound() != null)
			{
				try
				{
					Method boundMethod = boundClass.getMethod(desc.getBound(), new Class<?>[]{sender.getClass(), this.getClass(),
						name.getClass(), tree.getClass(), args.getClass()});
					
					boundMethod.invoke(null, new Object[]{sender, this, name, tree, args});
				}
				catch (NoSuchMethodException e)
				{
					throw new RuntimeException("A plugin had registered an action part of the command " + 
							name + " to the nonexistant method " + desc.getBound() + ".", e);
				}
				catch (SecurityException e)
				{
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					throw new RuntimeException("The plugin action method " + desc.getBound() + " has had a fatal error.", e);
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				for(Descriptor subDesc : desc.getActions())
					if(subDesc != null && subDesc instanceof ActionDescriptor)
						for(String alias : subDesc.getAliases())
							if(alias.equalsIgnoreCase(args[0]) || alias.equalsIgnoreCase("default"))
							{
								List<Descriptor> subTree = new ArrayList<Descriptor>(tree);
								tree.add(subDesc);
								
								executeAction(boundClass, sender, name, args, 1, (ActionDescriptor) subDesc, subTree);
								return;
							}
			}
		}
	}

	protected static AliasMap registeredCommands = new AliasMap();
	protected static CommandMap commandMap = null;
	
	public static void addCommands(Configuration config)
	{
		ConfigurationParser parser = new ConfigurationParser(config);
		CommandDescriptor[] commands = parser.parse();
		
		for(CommandDescriptor command : commands)
		{
			commandHandlerInit();
			List<String> aliases = Arrays.asList(command.getAliases());
			aliases.remove(0);
			
			Command bukkitCommand = new OPluginCommand(command.getAliases()[0], "", "", aliases);
			commandMap.register("_", bukkitCommand);
			registeredCommands.put(command.getAliases(), command);
		}
	}
	
	private static void commandHandlerInit()
	{
		if(commandMap != null)
			return;
		try
		{
			Field field = SimplePluginManager.class.getDeclaredField("commandMap");
			field.setAccessible(true);
			commandMap = (CommandMap)(field.get(Bukkit.getServer().getPluginManager()));
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}
