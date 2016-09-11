package com.oneshotmc.chunkmarkgen.deprecated;

import lombok.Data;

@Data
public class ChunkLoc {
	public ChunkLoc(int x, int z){
		this.x=x;
		this.z=z;
	}
	int x;
	int z;
}
