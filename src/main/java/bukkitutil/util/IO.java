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

package bukkitutil.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides convenience functions for I/O.
 * @author Klezst
 *
 */
public class IO {
    /**
     * Extracts the files specified by <code>names</code> from a plugin's
     * jarfile into the plugin's default data directory as defined by the
     * bukkit <code>PluginManager</code>.
     * 
     * <p> The <code>extract(plugin, names)</code> method for class
     * <code>IO</code> has the same effect as:
     * <pre><code> extract(plugin, plugin.dataFolder(), names) </code></pre>
     * 
     * @param plugin
     *            plugin to extract from
     * @param names
     *            names of files to extract
     * 
     * @throws NullPointerException
     *             if <code>names</code> is <code>null</code>.
     * @throws IOException
     *             if some I/O error occurs
     * 
     * @author Klezst
     * @author sk89q
     */
    public static void extract(JavaPlugin plugin, String... names)
	    throws IOException, NullPointerException {
    	extract(plugin, plugin.getDataFolder(), names);
    }
    
    /**
     * Extracts the files specified by <code>names</code> from a plugin's
     * jarfile into the directory as specified by <code>destination</code>.
     * 
     * @param plugin
     *            plugin to extract from
     * @param destination
     *            the extracted file's destination directory
     * @param names
     *            names of files to extract
     * 
     * @throws NullPointerException
     *             If name is null.
     * @throws IOException
     *             If an I/O error occurs.
     * 
     * @author Klezst
     * @author sk89q
     */
    public static void extract(JavaPlugin plugin, File destination, String... names)
	    throws IOException, NullPointerException {
		// Create directory.
	    destination.mkdirs();
		
		for (String name : names) {
		    // Check if file already exists.
		    // throws NullPointerException if name is null.
		    File actual = new File(destination, name);
		    if (!actual.exists()) {
				// Get input.
				InputStream input = plugin.getResource(name);
				if (input == null) {
				    throw new IOException(
					    "Unable to get InputStream for INTERNAL file "
						    + name
						    + ". Please contact plugin developer.");
				}
		
				// Get & write to output
				FileOutputStream output = null;
				try {
				    output = new FileOutputStream(actual);
				    byte[] buf = new byte[8192];
				    int length = 0;
				    while ((length = input.read(buf)) > 0) {
				    	output.write(buf, 0, length); // throws IOException, if an I/O error occurs.
				    }
				}
		
				// Close files.
				finally {
				    try {
				    	input.close(); // throws IOException, if an I/O error occurs.
				    } finally {
						if (output != null) {
						    output.close(); // throws IOException, if an I/O error occurs.
						}
				    }
				}
		    }
		}
    }
}
