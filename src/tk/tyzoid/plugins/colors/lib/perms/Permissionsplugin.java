package tk.tyzoid.plugins.colors.lib.perms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Permissionsplugin {
	boolean loadPermissions(Plugin permPlugin);
	public String[] getGroups(Player pl);
	public String getGroup(Player pl);
	public String getPrefix(Player pl);
	public String getSuffix(Player pl);
}
