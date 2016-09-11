package com.oneshotmc.chunkmarkgen;

import com.intellectualcrafters.configuration.ConfigurationSection;
import com.intellectualcrafters.plot.config.ConfigurationNode;
import com.intellectualcrafters.plot.generator.GridPlotWorld;
import com.intellectualcrafters.plot.object.PlotId;

public class MarkPlotWorld extends GridPlotWorld{
	public int ROAD_WIDTH;
    public int PLOT_WIDTH;
    public static int PLOT_HEIGHT;
	public MarkPlotWorld(String worldname, String id, Generator generator, PlotId min, PlotId max) {
		super(worldname, worldname, generator, max, max);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadDefaultConfiguration(ConfigurationSection config) {
		super.loadDefaultConfiguration(config);
		ALLOW_SIGNS = false;
		PLOT_WIDTH=16*32;
		ROAD_WIDTH=16*16;
		PLOT_HEIGHT=16;
	}

	@Override
    public ConfigurationNode[] getSettingNodes() {
        return new ConfigurationNode[0];
    }
	
	@Override
    public void loadConfiguration(final ConfigurationSection config) {
		//super.loadDefaultConfiguration(config);
		ALLOW_SIGNS = false;
		PLOT_WIDTH=16*32;
		ROAD_WIDTH=16*16;
		PLOT_HEIGHT=16;
    }

}
