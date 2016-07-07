package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.plot.generator.IndependentPlotGenerator;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotManager;
import com.intellectualcrafters.plot.object.PseudoRandom;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.PlotChunk;

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

	/*
	@Override
	public short[][] generateExtBlockSections(final World world, final Random r, final int X, final int Z,
			final BiomeGrid biomes) {
		final boolean other = (X+Z)%2==0;
		int realx = X * 16;
		int realz = Z * 16;
		final int blockID;
		final MarkPlotArea pw = (MarkPlotArea) PS.get().getPlotArea(world.getName());
		final short[][] result = new short[16][];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				if(isWall(realx+x,realz+z, pw.ROAD_WIDTH,pw.PLOT_WIDTH)){
					for(int y=0; y < WALL_HEIGHT; y++){
						setBlock(result, x, y, z, (short)7);
					}
					continue;
				}
				if(isOutPlot(realx+x,realz+z, pw.ROAD_WIDTH,pw.PLOT_WIDTH)){
					continue;
				}
				setBlock(result, x, 0, z, (short)7);
				for (int y = 1; y < HEIGHT; y++) {
					if(other)
						setBlock(result, x, y, z, OTHER_BLOCK);
					else
						setBlock(result,x,y,z, MAIN_BLOCK);
					/*
					else{
						setBlock(result, x, y, z, (short) 0);
					}
					
				}
			}
		}
		return result;

	}
	*/
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
	public void generateChunk(PlotChunk<?> result, PlotArea area, PseudoRandom rand) {
		int X = result.getX();
		int Z = result.getZ();
		final boolean other = (X+Z)%2==0;
		int realx = X * 16;
		int realz = Z * 16;
		final int blockID;
		final MarkPlotWorld pw = (MarkPlotWorld) area;
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				result.setBiome(x, z, area.PLOT_BIOME);
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
				for (int y = 1; y < HEIGHT; y++) {
					if(other)
						result.setBlock(x, y, z, OTHER_BLOCK,(byte)0);
					else
						result.setBlock(x,y,z, MAIN_BLOCK,(byte)0);
					/*
					else{
						setBlock(result, x, y, z, (short) 0);
					}
					*/
				}
			}
		}
		return;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ChunkMarkGen";
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