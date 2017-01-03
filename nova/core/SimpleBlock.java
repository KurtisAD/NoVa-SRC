package nova.core;

import net.minecraft.block.Block;

public class SimpleBlock {
	public int id, metadata;
	
	public SimpleBlock(int id, int metadata){
		this.id = id;
		this.metadata = metadata;
	}
	
	public SimpleBlock(int hash){
		this.metadata = hash & 0xFFF;
		this.id = hash >> 12;
	}
	
	@Override
	public int hashCode(){
		return (id << 12) + metadata;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o.getClass() == this.getClass())
		{
			SimpleBlock bloc = (SimpleBlock)o;
			return this.id == bloc.id && bloc.metadata == this.metadata;
		}
		else
			return false;
	}

	public String getName(){
		Block block = Block.getBlockById(this.id);
		return "[" + Block.REGISTRY.getNameForObject(block) + "] [" + Integer.toString(id) + ":" + Integer.toString(metadata) + "]";
	}
}
