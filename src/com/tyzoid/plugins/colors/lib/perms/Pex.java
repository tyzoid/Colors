package com.tyzoid.plugins.colors.lib.perms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Pex implements Permissionsplugin {
	@Override
	public boolean loadPermissions(Plugin permPlugin) {
		boolean exists = false;
		if(permPlugin != null && permPlugin instanceof PermissionsEx) {
			exists = true;
		}
		return exists;
	}
	
	@Override
	public String getGroup(Player pl) {
		String[] groups = getGroups(pl);
		return (groups != null && groups.length > 0) ? groups[0] : ((pl.isOp()) ? "Op" : "Default");
	}
	
	@Override
	public String[] getGroups(Player pl) {
		return PermissionsEx.getPermissionManager().getUser(pl.getName()).getGroupsNames();
	}
	
	@Override
	public String getPrefix(Player pl) {
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(pl.getName());
		return (user == null) ? null : user.getPrefix();
	}
	
	@Override
	public String getSuffix(Player pl) {
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(pl.getName());
		return (user == null) ? null : user.getSuffix();
	}
}
