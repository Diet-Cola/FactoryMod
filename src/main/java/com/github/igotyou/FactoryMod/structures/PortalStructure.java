package com.github.igotyou.FactoryMod.structures;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import vg.civcraft.mc.civmodcore.world.WorldUtils;

public class PortalStructure extends MultiBlockStructure{

	private Location center;
	private List<Block> adjacentBlocks;
	private Location barrel;
	private boolean complete;
	private Location targetCenter;

	public PortalStructure(List<Location> blocks) {
		this(blocks.get(0).getBlock());
	}

	public PortalStructure(Block center) {
		if (center.getType() == Material.LODESTONE) {
			this.center = center.getLocation();
			if (this.center.getBlock().getRelative(BlockFace.DOWN).getType() == Material.BARREL) {
				this.barrel = this.center.getBlock().getRelative(BlockFace.DOWN).getLocation();
			}
			List<Block> adjacentBlocks = searchForBlockOnSides(center, Material.CRYING_OBSIDIAN);
			if (adjacentBlocks.size() != 4) {
				return;
			}
			this.adjacentBlocks = adjacentBlocks;
			if (this.center != null && this.adjacentBlocks.size() == 4) {
				this.complete = true;
				return;
			}
		}
		this.complete = false;
	}

	@Override
	public boolean isComplete() {
		return this.complete;
	}

	@Override
	public List<Location> getAllBlocks() {
		List<Location> allBlocks = new ArrayList<>();
		allBlocks.add(this.center);
		allBlocks.add(this.barrel);
		for (Block b : this.adjacentBlocks) {
			allBlocks.add(b.getLocation());
		}
		allBlocks.add(targetCenter);
		return allBlocks;
	}

	@Override
	public void recheckComplete() {
		this.complete = this.center != null &&
						this.center.getBlock().getType() == Material.LODESTONE &&
						this.barrel != null && this.barrel.getBlock().getType() == Material.BARREL &&
						searchForBlockOnSides(center.getBlock(), Material.CRYING_OBSIDIAN).size() == 4;
	}

	@Override
	public Location getCenter() {
		return this.center;
	}

	public Location getBarrel() {
		return barrel;
	}

	public List<Block> getAdjacentBlocks() {
		return adjacentBlocks;
	}

	@Override
	public List<Block> getRelevantBlocks() {
		List<Block> allBlocks = new ArrayList<>();
		allBlocks.add(this.center.getBlock());
		allBlocks.add(this.barrel.getBlock());
		allBlocks.addAll(this.adjacentBlocks);
		return allBlocks;
	}

	@Override
	public boolean relevantBlocksDestroyed() {
		return this.center.getBlock().getType() != Material.LODESTONE &&
				this.barrel.getBlock().getType() != Material.BARREL &&
				searchForBlockOnSides(this.center.getBlock(), Material.CRYING_OBSIDIAN).size() == 0;
	}

	public void setTargetCenter(Location location) {
		this.targetCenter = location;
	}

	public Location getTargetCenter() {
		return targetCenter;
	}

	public void spawnPlatform(Block block) {
		block.setType(Material.LODESTONE);
		block.getRelative(BlockFace.DOWN).setType(Material.BARREL);
		for (BlockFace face : WorldUtils.PLANAR_SIDES) {
			block.getRelative(face).setType(Material.CRYING_OBSIDIAN);
		}
	}
}
