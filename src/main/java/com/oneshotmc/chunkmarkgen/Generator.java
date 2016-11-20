package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.plot.generator.IndependentPlotGenerator;
import com.intellectualcrafters.plot.object.*;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.block.ScopedLocalBlockQueue;

import java.util.Random;

public class Generator extends IndependentPlotGenerator {

	public Generator(String world) {
		super();
		MainUtil.initCache();
		// TODO Auto-generated constructor stub
	}

	public final static int WALL_HEIGHT = 256;
	public final static short HEIGHT = 16;
	public final static short MAIN_BLOCK = 155; // Plot main filling (stone)
	public final static short OTHER_BLOCK = 173;
	public final static short FLOOR_BLOCK = 2; // Plot top floor (grass)
	public final static short BOTTOM_BLOCK = 7; // Bottom bedrock
	private static PlotManager manager = new ChunkPlotManager();

	private boolean isWall(final int x, final int z, int road_width, int plot_width){
		int size = road_width+plot_width;
		int px = x % size;
		int pz = z % size;
		if(px<0){
			px+=size;
		}
		if(pz<0){
			pz+=size;
		}
		//Since don't start at 0
		//if(px>=0){
		//	px++;
		//}
		//if(pz>=0){
		//	pz++;
		//}
		int lower;
		//Splitting road in half
		if ((road_width % 2) == 0) {
            lower = (road_width / 2) - 1;
        } else {
            lower = (road_width / 2);
        }
		if((lower == px || lower == pz || px == plot_width + lower+1 || pz == plot_width + lower+1)&&
				!(px < lower || px >  plot_width + lower+1 || pz < lower || pz >  plot_width + lower+1)){
			return true;
		}
		return false;
	}
	
	public boolean isOutPlot(final int x, final int z, int road_width, int plot_width){
		int size = road_width+plot_width;
		int px = x % size;
		int pz = z % size;
		if(px<0){
			px+=size;
		}
		if(pz<0){
			pz+=size;
		}
		//Since don't start at 0
		//if(px>=0){
		//	px++;
		//}
		//if(pz>=0){
		//	pz++;
		//}
		int lower;
		//Splitting road in half
		if ((road_width % 2) == 0) {
            lower = (road_width / 2) - 1;
        } else {
            lower = (road_width / 2);
        }
		 if (px < lower+1 || px >  plot_width + lower || pz < lower+1 || pz >  plot_width + lower) {
				return true;
			}
		 return false;
	}
	
	//@Override
	//public void generateChunk(final World world, final RegionWrapper region, final PseudoRandom random, final int X,
		//	final int Z, final BiomeGrid grid) {
		//return;
		// MegaPlotArea pw = (MegaPlotArea)
		// PlotSquared.getPlotArea(world.getName());
		// if (pw.BASE_WORLD == null) {
		// System.out.print("WORLD NOT LOADED: " + X + "," + Z);
		// for (int x = 0; x < 16; x++) {
		// for (int z = 0; z < 16; z++) {
		// for (int y = 0; y < 64; y++) {
		// setBlock(x, y, z, (short) 155);
		// }
		// }
		// }
		// return;
		// }
		// int cx = X % (pw.MCA_WIDTH * 32);
		// int cz = Z % (pw.MCA_WIDTH * 32);
		// if (cx < 0) {
		// cx += (pw.MCA_WIDTH * 32);
		// }
		// if (cz < 0) {
		// cz += (pw.MCA_WIDTH * 32);
		// }
		// cx += pw.mx * 32;
		// cz += pw.mx * 32;
		// Chunk chunk = pw.BASE_WORLD.getChunkAt(cx, cz);
		// chunk.load(false);
		// if (!chunk.isLoaded()) {
		// System.out.print("CHUNK NOT LOADED!!!!!!!!!!!!!");
		// }
		// int xx = X << 4;
		// int zz = Z << 4;
		// for (int x = 0; x < 16; x++) {
		// for (int z = 0; z < 16; z++) {
		// for (int y = 0; y < 256; y++) {
		// Block block = chunk.getBlock(x, y, z);
		// int id = block.getTypeId();
		// if (id != 0) {
		// setBlock(x, y, z, (short) id);
		// byte data = block.getData();
		// if (data != 0) {
		// SetBlockQueue.setData(pw.worldname, xx + x, y, zz + z, data);
		// }
		// }
		// }
		// }
		// }
	//}
	/*
	@Override
	public PlotArea getNewPlotArea(final String world) {
		return new MarkPlotArea(world);
	}

	@Override
	public PlotManager getPlotManager() {
		return manager;
	}

	private List<BukkitPlotPopulator> pop;
	*/
	public void setBlock(final short[][] result, final int x, final int y, final int z, final short blkid) {
		if (result[y >> 4] == null) {
			result[y >> 4] = new short[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}
	/*
	@Override
	public void init(final PlotArea world) {
	}
	*/

	//TODO: confused...

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ChunkMarkGen";
	}

	@Override
	public void generateChunk(ScopedLocalBlockQueue result, PlotArea plotArea, PseudoRandom rand) {
		Location location = result.getMin();
		int realx = location.getX();
		int realz = location.getZ();
		int X = realx >> 4;
		int Z = realz >> 4;
		final boolean other = (X+Z)%2==0;
		final int blockID;
		final MarkPlotWorld pw = (MarkPlotWorld) plotArea;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				result.setBiome(x, z, plotArea.PLOT_BIOME);
				if(isWall(realx+x,realz+z, pw.ROAD_WIDTH,pw.PLOT_WIDTH)){
					for(int y=0; y < WALL_HEIGHT; y++){
						result.setBlock(x, y, z, (short)7,(byte)0);
					}
					continue;
				}
				if(isOutPlot(realx+x,realz+z, pw.ROAD_WIDTH,pw.PLOT_WIDTH)){
					continue;
				}
				result.setBlock(x, 0, z, (short)7,(byte)0);
				for (int y = 1; y < HEIGHT - 1; y++) {
					if(other)
						result.setBlock(x,y,z,(short) 159, (byte) 13);
					else
						result.setBlock(x,y,z,(short) 159, (byte) 5);
				}

				if(x == 0 || x==15 || z== 0 || z==15)
					result.setBlock(x, HEIGHT - 1, z, (short)159,(byte)15);
				else {
					int toSet = rand.random(3);
					byte data;
					switch (toSet) {
						case 0:
							data = 5;
							break;
						case 1:
							data = 7;
							break;
						case 2:
							data = 13;
							break;
						default:
							data = 1;
							break;
					}
					result.setBlock(x, HEIGHT - 1, z, (short) 159, data);
				}
			}
		}
		return;
	}


	@Override
	public PlotArea getNewPlotArea(String world, String id, PlotId min, PlotId max)  {
		return new MarkPlotWorld(world, id, this, min, max);
	}

	@Override
	public PlotManager getNewPlotManager() {
		// TODO Auto-generated method stub
		return new ChunkPlotManager();
	}

	@Override
	public void initialize(PlotArea arg0) {
		// TODO Auto-generated method stub
		
	}


}