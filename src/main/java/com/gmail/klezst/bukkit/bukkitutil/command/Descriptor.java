package com.gmail.klezst.bukkit.bukkitutil.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.gmail.klezst.bukkit.bukkitutil.command.descriptors.ActionDescriptor;

public interface Descriptor
{
	public String getName();
	public String[] getAliases();
	public void setAliases(String[] aliases);
	public Descriptor[] getActions();
	public void addAction(ActionDescriptor action);
	public void removeAction(String name);
	
	public String getBound();
	public void setBound(String bound);
	
	public boolean activate(CommandSender sender, Command command, String name, List<Descriptor> actionTree, String[] args);
}
