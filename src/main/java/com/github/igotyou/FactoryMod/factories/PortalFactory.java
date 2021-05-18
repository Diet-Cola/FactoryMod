package com.github.igotyou.FactoryMod.factories;

import com.github.igotyou.FactoryMod.FactoryMod;
import com.github.igotyou.FactoryMod.events.FactoryActivateEvent;
import com.github.igotyou.FactoryMod.interactionManager.IInteractionManager;
import com.github.igotyou.FactoryMod.powerManager.IPowerManager;
import com.github.igotyou.FactoryMod.repairManager.IRepairManager;
import com.github.igotyou.FactoryMod.structures.MultiBlockStructure;
import com.github.igotyou.FactoryMod.structures.PortalStructure;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class PortalFactory extends Factory {

	private double citadelBreakReduction;
	//For custom nether, if your nether is 4:1 for example, youll want this set to 2
	private double targetLocationMultiplier;
	private String targetWorld;
	private Location targetLocation;

	public PortalFactory(IInteractionManager im, IRepairManager rm, IPowerManager pm, PortalStructure mbs,
						 int updateTime, String name, double citadelBreakReduction, double targetLocationMultiplier,
						 String targetWorld) {
		super(im, rm, pm, mbs, updateTime, name);
		this.citadelBreakReduction = citadelBreakReduction;
		this.targetLocationMultiplier = targetLocationMultiplier;
		this.targetWorld = targetWorld;
		this.targetLocation = new Location(Bukkit.getWorld(targetWorld), (mbs.getCenter().getX() * targetLocationMultiplier),
				 mbs.getCenter().getY(),
				(mbs.getCenter().getZ() * targetLocationMultiplier)).toCenterLocation();
		mbs.setTargetCenter(targetLocation);
	}

	@Override
	public void activate() {
		active = true;
		run();
	}

	@Override
	public void deactivate() {
		active = false;
		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(FactoryMod.getInstance(), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				showPortalOffParticles(player);
			}
		}, 1L, 1L);
		if (active) {
			Bukkit.getScheduler().cancelTask(task);
		}
	}

	@Override
	public void attemptToActivate(Player p, boolean onStartUp) {
		mbs.recheckComplete();
		//dont activate twice
		if (active) {
			return;
		}
		//ensure factory is physically complete
		if (!mbs.isComplete()) {
			p.sendMessage(ChatColor.RED + "This portal is not complete or has been broken!");
			return;
		}
		FactoryActivateEvent fae = new FactoryActivateEvent(this, p);
		Bukkit.getPluginManager().callEvent(fae);
		if (fae.isCancelled()) {
			return;
		}
		activate();
	}

	@Override
	public void run() {
		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(FactoryMod.getInstance(), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				showPortalOnParticles(player);
			}
		}, 1L, 1L);
		if (!active) {
			Bukkit.getScheduler().cancelTask(task);
		}
	}

	public void showPortalOnParticles(Player player) {
		player.spawnParticle(Particle.PORTAL, mbs.getCenter().toCenterLocation().clone().add(0, 1, 0), 50);
	}

	public void showPortalOffParticles(Player player) {
		player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, mbs.getCenter().toCenterLocation().clone().add(0, 1, 0), 10);
	}

	public double getCitadelBreakReduction() {
		return citadelBreakReduction;
	}

	public double getTargetLocationMultiplier() {
		return targetLocationMultiplier;
	}

	public String getTargetWorld() {
		return targetWorld;
	}

	public Location getTargetLocation() {
		return targetLocation;
	}
}
