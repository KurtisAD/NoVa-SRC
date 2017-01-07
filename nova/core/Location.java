package nova.core;

import net.minecraft.util.math.BlockPos;

public class Location {

	public double x, y, z;
	public int blockX, blockY, blockZ;
	
	public Location(BlockPos pos){
		this.makeLocation(pos.getX(), pos.getY(), pos.getZ());
	}

	public Location(double x, double y, double z)
	{
		this.makeLocation(x, y, z);
	}
	
	public void makeLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        blockX = (int)Math.round(x);
        blockY = (int)Math.round(y);
        blockZ = (int)Math.round(z);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(blockX, blockY, blockZ);
    }


    public String toString()
	{
		return "(X: " + Double.toString(x) + ", Y:" + Double.toString(y) + ", Z:" + Double.toString(z) + ")";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o.getClass() == this.getClass())
		{
			Location loc = (Location)o;
			return loc.x == this.x && loc.y == this.y && loc.z == this.z;
		}
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = hash * 7 + this.blockX;
		hash = hash * 7 + this.blockY;
		hash = hash * 7 + this.blockZ;
		return hash;
	}
	
	public double distanceFromSq(double x, double z)
	{
		return Math.pow(this.x - x, 2) + Math.pow(this.z - z, 2);
	}
}
