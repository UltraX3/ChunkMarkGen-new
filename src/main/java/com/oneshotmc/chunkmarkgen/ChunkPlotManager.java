package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.plot.generator.GridPlotManager;
import com.intellectualcrafters.plot.object.*;
import com.intellectualcrafters.plot.util.ChunkManager;
import com.intellectualcrafters.plot.util.block.LocalBlockQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ChunkPlotManager extends GridPlotManager {

	@Override
	public boolean claimPlot(PlotArea arg0, Plot arg1) {
		return true;
	}

	@Override
	public boolean clearPlot(final PlotArea plotArea, final Plot plot, final Runnable whenDone) {
		/*
		 * Location bottom = MainUtil.getPlotBottomLocAbs(PlotArea.worldname,
		 * plot.getId()); Location top =
		 * MainUtil.getPlotTopLocAbs(PlotArea.worldname, plot.getId()); final
		 * World world = BukkitUtil.getWorld(PlotArea.worldname);
		 * ChunkManager.chunkTask(bottom, top, new RunnableVal<int[]>() {
		 * 
		 * @Override public void run() { world.regenerateChunk(value[0],
		 * value[1]); } }, whenDone, 5); return true;
		 */

		final HashSet<RegionWrapper> regions = plot.getRegions();
		Runnable run = new Runnable() {
			@Override
			public void run() {
				if (regions.isEmpty()) {
					whenDone.run();
					return;
				}
				Iterator<RegionWrapper> iterator = regions.iterator();
				RegionWrapper region = iterator.next();
				iterator.remove();
				Location pos1 = new Location(plot.getArea().worldname, region.minX, region.minY, region.minZ);
				Location pos2 = new Location(plot.getArea().worldname, region.maxX, region.maxY, region.maxZ);
				ChunkManager.manager.regenerateRegion(pos1, pos2, false, this);
			}
		};
		run.run();
		return true;
	}

	@Override
	public boolean createRoadEast(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean createRoadSouth(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean createRoadSouthEast(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean finishPlotMerge(PlotArea arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean finishPlotUnlink(PlotArea arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Location getPlotBottomLocAbs(PlotArea pw, PlotId id) {
		final MarkPlotWorld mpw = (MarkPlotWorld) pw;
		Location loc = getPlotTopLocAbs(pw, id).add(-mpw.PLOT_WIDTH, 0, -mpw.PLOT_WIDTH).add(1, 0, 1);
		loc.setY(1);
		return loc;
	}

	@Override
	public String[] getPlotComponents(PlotArea arg0, PlotId arg1) {
		// TODO Auto-generated method stub
		return new String[] {"floor", "filling"};
	}

	@Override
	public PlotId getPlotId(PlotArea pw, int x, int y, int z) {
		MarkPlotWorld mpw = (MarkPlotWorld) pw;
		int size = mpw.PLOT_WIDTH + mpw.ROAD_WIDTH;
		int X = x / size;
		int Z = z / size;
		if (x >= 0) {
			X++;
		}
		if (z >= 0) {
			Z++;
		}
		int px = x % size;
		int pz = z % size;
		if (px < 0) {
			px += size;
		}
		if (pz < 0) {
			pz += size;
		}
		// Since don't start at 0
		// if(px>=0){
		// px++;
		// }
		// if(pz>=0){
		// pz++;
		// }
		int lower;
		// Splitting road in half
		if ((mpw.ROAD_WIDTH % 2) == 0) {
			lower = (mpw.ROAD_WIDTH / 2) - 1;
		} else {
			lower = (mpw.ROAD_WIDTH / 2);
		}
		// if(lower == px || lower == pz || px == plot_width + lower+1 || pz ==
		// plot_width + lower+1)
		if (px < lower + 1 || px > mpw.PLOT_WIDTH + lower || pz < lower + 1 || pz > mpw.PLOT_WIDTH + lower) {
			return null;
		}
		// Since we know it MUST be in plot boundries
		return new PlotId(X, Z);
	}

	@Override
	public Location getPlotTopLocAbs(PlotArea pw, PlotId id) {
		int px = id.x;
		// Should be id.z :p - No it shouldn't, there are only two axis for plot
		// ids, it's not the same system as world block coordinates.
		int pz = id.y;
		final MarkPlotWorld mpw = (MarkPlotWorld) pw;
		final int x = (px * (mpw.ROAD_WIDTH + mpw.PLOT_WIDTH)) - ((int) Math.floor(mpw.ROAD_WIDTH / 2)) - 1;
		final int z = (pz * (mpw.ROAD_WIDTH + mpw.PLOT_WIDTH)) - ((int) Math.floor(mpw.ROAD_WIDTH / 2)) - 1;
		return new Location(pw.worldname, x, 256, z);
	}

	@Override
	public Location getSignLoc(PlotArea pw, Plot plot) {

		MarkPlotWorld mpw = (MarkPlotWorld) pw;
		final Location bot = getPlotBottomLocAbs(pw, plot.getId());
		return new com.intellectualcrafters.plot.object.Location(pw.worldname, bot.getX() - 1, mpw.PLOT_HEIGHT + 1,
				bot.getZ() - 2);

		//return null;
	}

	@Override
	public boolean removeRoadEast(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRoadSouth(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRoadSouthEast(PlotArea arg0, Plot arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setComponent(PlotArea plotArea, PlotId plotid, String comp, PlotBlock[] blocks) {
		switch (comp) {

		case "floor":
			setFloor(plotArea, plotid, blocks);
			return true;
			
		case "filling":
			setFilling(plotArea, plotid, blocks);

		}
		return false;

	}
	
	private boolean setFilling(PlotArea plotArea, PlotId plotId, PlotBlock[] blocks) {
		Plot plot = plotArea.getPlotAbs(plotId);
		LocalBlockQueue queue = plotArea.getQueue(false);
		if (plot.isBasePlot()) {
			MarkPlotWorld mpw = ((MarkPlotWorld) plotArea);
			for (RegionWrapper region : plot.getRegions()) {
				Location pos1 = getPlotBottomLocAbs(plotArea,plotId);
				Location pos2 = getPlotTopLocAbs(plotArea, plotId).clone();
				pos2.setY(MarkPlotWorld.PLOT_HEIGHT-2);
				queue.setCuboid(pos1, pos2, blocks);
			}
		}
		queue.enqueue();
		return true;
	}

	private boolean setFloor(PlotArea plotArea, PlotId plotId, PlotBlock[] blocks) {
		Plot plot = plotArea.getPlotAbs(plotId);
		LocalBlockQueue queue = plotArea.getQueue(false);
		if (plot.isBasePlot()) {
			for (RegionWrapper region : plot.getRegions()) {
				final Location pos1 = getPlotBottomLocAbs(plotArea, plotId).clone();
				pos1.setY(MarkPlotWorld.PLOT_HEIGHT-1);
				final Location pos2 = getPlotTopLocAbs(plotArea, plotId).clone();
				pos2.setY(MarkPlotWorld.PLOT_HEIGHT-1);
				queue.setCuboid(pos1, pos2, blocks);
			}
		}
		queue.enqueue();
		return true;
	}

	@Override
	public boolean startPlotMerge(PlotArea arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean startPlotUnlink(PlotArea arg0, ArrayList<PlotId> arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean unclaimPlot(PlotArea arg0, Plot arg1, Runnable arg2) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public PlotId getPlotIdAbs(PlotArea pw, int x, int y, int z) {
		return getPlotId(pw, x, y, z);
	}

}
