/*
	BukkitUtil
	Copyright (C) 2011 Klezst

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bukkitutil.compatibility;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import bukkitutil.BukkitUtil;

/**
 * Controls communication with permissions plugins.
 * 
 * @author Klezst
 */
public class Permission {
    public static net.milkbowl.vault.permission.Permission permission = null;
    static {
	try {
	    permission = Bukkit
		    .getServer()
		    .getServicesManager()
		    .getRegistration(
			    net.milkbowl.vault.permission.Permission.class)
		    .getProvider();
	} catch (NullPointerException e) {
	    // We must catch this exception, because Java doesn't throw the NullPointerException to the method caller.
	    // Instead a NullPointerException will be thrown by the method they called.
	}

    }

    /**
     * Returns true, if sender has the permission node. If no permission plugin is loaded, will return true if the player is an op.
     * NOTE: There is a difference between has(CommandSender, String) and hasPermission(CommandSender, String).
     * 
     * @param sender
     *            Who to check for permission.
     * @param node
     *            The permission needed.
     * 
     * @return True, if sender has the permission node. If no permission plugin is loaded, will return true if the player is an op.
     * 
     * @author Klezst
     */
    public static boolean has(CommandSender sender, String node) {
	try {
	    return hasPermission(sender, node);
	} catch (NullPointerException e) {
	    BukkitUtil.log(Level.WARNING,
		    "Vault does not have a permissions plugin enabled.");
	    return sender.isOp();
	}
    }
    
    /**
     * Returns true, if sender has the permission node.
     * NOTE: There is a difference between has(CommandSender, String) and hasPermission(CommandSender, String).
     * 
     * @param sender
     *            Who to check for permission.
     * @param node
     *            The permission needed.
     * 
     * @return True, if sender has the permission node.
     * 
     * @throws NullPointerException
     *             If no permission is loaded.
     * 
     * @author Klezst
     */
    public static boolean hasPermission(CommandSender sender, String node)
	    throws NullPointerException {
	return permission.has(sender, node);
    }

    /**
     * Returns true, if Vault has loaded a permissions plugin.
     * 
     * @return true, if Vault has loaded a permissions plugin.
     * 
     * @author Klezst
     */
    public static boolean isEnabled() {
	if (permission == null) {
	    return false;
	}
	return permission.isEnabled();
    }
}
