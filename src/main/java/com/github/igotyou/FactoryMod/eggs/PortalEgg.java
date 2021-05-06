package com.github.igotyou.FactoryMod.eggs;

import com.github.igotyou.FactoryMod.factories.Factory;
import com.github.igotyou.FactoryMod.factories.PortalFactory;
import com.github.igotyou.FactoryMod.interactionManager.PortalInteractionManager;
import com.github.igotyou.FactoryMod.structures.MultiBlockStructure;
import com.github.igotyou.FactoryMod.structures.PortalStructure;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

public class PortalEgg implements IFactoryEgg{

	private String name;
	private String targetWorld;
	private double returnRate;
	private double citadelBreakReduction;
	private ItemMap setupCost;
	private double targetLocationMultipler;

	public PortalEgg(String name, String targetWorld, double returnRate, double citadelBreakReduction, ItemMap setupCost, double targetLocationMultipler) {
		this.name = name;
		this.targetWorld = targetWorld;
		this.returnRate = returnRate;
		this.citadelBreakReduction = citadelBreakReduction;
		this.setupCost = setupCost;
		this.targetLocationMultipler = targetLocationMultipler;
	}

	@Override
	public Factory hatch(MultiBlockStructure mbs, Player p) {
		PortalStructure structure = (PortalStructure) mbs;
		PortalInteractionManager interactionManager = new PortalInteractionManager();
		PortalFactory factory = new PortalFactory(interactionManager, null, null, structure, 0, name, citadelBreakReduction, targetLocationMultipler, targetWorld);
		interactionManager.setFactory(factory);
		return factory;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getReturnRate() {
		return returnRate;
	}

	@Override
	public Class<? extends MultiBlockStructure> getMultiBlockStructure() {
		return PortalStructure.class;
	}

	public double getCitadelBreakReduction() {
		return citadelBreakReduction;
	}

	public ItemMap getSetupCost() {
		return setupCost;
	}

	public double getTargetLocationMultipler() {
		return targetLocationMultipler;
	}

	public Factory revive(List<Location> blocks) {
		PortalStructure structure = new PortalStructure(blocks);
		PortalInteractionManager interactionManager = new PortalInteractionManager();
		PortalFactory portalFactory = new PortalFactory(interactionManager, null, null, structure, 0, name, citadelBreakReduction, targetLocationMultipler, targetWorld);
		interactionManager.setFactory(portalFactory);
		return portalFactory;
	}
}
