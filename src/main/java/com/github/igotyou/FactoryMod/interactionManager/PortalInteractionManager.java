package com.github.igotyou.FactoryMod.interactionManager;

import com.github.igotyou.FactoryMod.FactoryMod;
import com.github.igotyou.FactoryMod.factories.PortalFactory;
import com.github.igotyou.FactoryMod.structures.PortalStructure;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import vg.civcraft.mc.citadel.ReinforcementLogic;
import vg.civcraft.mc.citadel.model.Reinforcement;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.group.Group;
import vg.civcraft.mc.namelayer.permission.PermissionType;

public class PortalInteractionManager implements IInteractionManager{

	private PortalFactory factory;

	public PortalInteractionManager(PortalFactory factory) {
		this();
		this.factory = factory;
	}

	public PortalInteractionManager() {
	}

	public void setFactory(PortalFactory factory) {
		this.factory = factory;
	}

	@Override
	public void rightClick(Player p, Block b, BlockFace bf) {
		if (!stickInHandCheck(p)) {
			return;
		}
		if (!doesPlayerHavePerms(p, b, "UPGRADE_FACTORY")) {
			return;
		}
		//Open Management GUI
	}

	@Override
	public void leftClick(Player p, Block b, BlockFace bf) {
		if (!stickInHandCheck(p)) {
			return;
		}
		if (!doesPlayerHavePerms(p, b, "USE_FACTORY")) {
			return;
		}
		if (!factory.getMultiBlockStructure().isComplete()) {
			p.sendMessage(ChatColor.RED + "This portal is not complete!");
			return;
		}
		factory.attemptToActivate(p, true);
		p.teleport(factory.getTargetLocation().clone().add(0, 1, 0));
		PortalStructure structure = (PortalStructure) factory.getMultiBlockStructure();
		structure.spawnPlatform(factory.getTargetLocation().clone().getBlock());
		p.sendMessage(ChatColor.GREEN + "Teleporting you!");
	}

	@Override
	public void blockBreak(Player p, Block b) {
		if (p != null && factory.getMultiBlockStructure().getRelevantBlocks().contains(b)) {
			p.sendMessage(ChatColor.RED + "You have broken the portal, it is in disrepair now!");
		}
		if (factory.isActive()) {
			factory.deactivate();
		}
	}

	@Override
	public void redStoneEvent(BlockRedstoneEvent e, Block factoryBlock) {
		//Would redstone really be useful here?
	}

	public boolean stickInHandCheck(Player player) {
		if (player.getInventory().getItemInMainHand().getType() != FactoryMod.getInstance().getManager().getFactoryInteractionMaterial()) {
			return false;
		}
		return true;
	}

	public boolean doesPlayerHavePerms(Player player, Block block, String permission) {
		if (FactoryMod.getInstance().getManager().isCitadelEnabled()) {
			Reinforcement reinforcement = ReinforcementLogic.getReinforcementProtecting(block);
			if (reinforcement != null) {
				Group group = reinforcement.getGroup();
				if (!NameAPI.getGroupManager().hasAccess(group, player.getUniqueId(), PermissionType.getPermission(permission))) {
					player.sendMessage(ChatColor.RED + "You dont have permission to use this portal!");
					return false;
				}
			}
		}
		return true;
	}
}
