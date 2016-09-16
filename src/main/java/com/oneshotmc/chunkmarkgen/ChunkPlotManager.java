package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.plot.generator.GridPlotManager;
import com.intellectualcrafters.plot.object.*;
import com.intellectualcrafters.plot.util.ChunkManager;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.TaskManager;
import com.plotsquared.bukkit.generator.BukkitPlotGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import sun.java2d.xr.MutableInteger;

import java.util.ArrayList;
import java.util.List;

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
		final Location bot = getPlotBottomLocAbs(plot.getArea(), plot.getId());
		final Location top = getPlotTopLocAbs(plot.getArea(), plot.getId());
		final World world = Bukkit.getWorld(plotArea.worldname);
		final List<ChunkLoc> chunks = new ArrayList<>();
		for (int x = bot.getX() >> 4; x <= (top.getX() >> 4); x++) {
			for (int z = bot.getZ() >> 4; z <= (top.getZ() >> 4); z++) {
				chunks.add(new ChunkLoc(x, z));
			}
		}
		final MutableInteger id = new MutableInteger(0);
		id.setValue(TaskManager.runTaskRepeat(new Runnable() {
			@Override
			public void run() {
				if (chunks.size() == 0) {
					Bukkit.getScheduler().cancelTask(id.getValue());
					whenDone.run();
					return;
				}
				final long start = System.currentTimeMillis();
				while (((System.currentTimeMillis() - start) < 25) && (chunks.size() > 0)) {
					final ChunkLoc loc = chunks.remove(0);
					if (world.loadChunk(loc.x, loc.z, false)) {
						world.regenerateChunk(loc.x,loc.z);
						MainUtil.update(plotArea.worldname,loc);
					}
				}
			}
		}, 1));
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
	
	private boolean setFilling(PlotArea plotArea, PlotId plotid, PlotBlock[] plotBlock) {
		MarkPlotWorld pw = (MarkPlotWorld) plotArea;
		final Location pos1 = getPlotBottomLocAbs(plotArea, plotid);
        final Location pos2 = getPlotTopLocAbs(plotArea, plotid);
        pos1.setY(1);
        pos2.setY(MarkPlotWorld.PLOT_HEIGHT -2);
		MainUtil.setCuboidAsync(plotArea.worldname, pos1, pos2, plotBlock);
		World world = Bukkit.getWorld(plotArea.worldname);
		for(int cx = pos1.getX() >> 4; cx < pos2.getX() >> 4; cx++){
			for(int cz = pos1.getZ() >> 4; cz < pos2.getZ() >> 4; cz++){
				world.unloadChunk(cx,cz);
			}
		}
		unloadChunks(plotArea,pos1,pos2);
        return true;
	}

	private void unloadChunks(PlotArea plotArea, Location pos1, Location pos2){
		World world = Bukkit.getWorld(plotArea.worldname);
		for(int cx = pos1.getX() >> 4; cx < pos2.getX() >> 4; cx++){
			for(int cz = pos1.getZ() >> 4; cz < pos2.getZ() >> 4; cz++){
				world.unloadChunk(cx,cz);
			}
		}
	}

	private boolean setFloor(PlotArea plotArea, PlotId plotid, PlotBlock[] plotBlock) {
		MarkPlotWorld pw = (MarkPlotWorld) plotArea;
		final Location pos1 = getPlotBottomLocAbs(plotArea, plotid);
        final Location pos2 = getPlotTopLocAbs(plotArea, plotid);
        pos1.setY(MarkPlotWorld.PLOT_HEIGHT-1);
        pos2.setY(MarkPlotWorld.PLOT_HEIGHT -1);
		MainUtil.setCuboidAsync(plotArea.worldname, pos1, pos2, plotBlock);
		World world = Bukkit.getWorld(plotArea.worldname);
		unloadChunks(plotArea,pos1,pos2);
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
