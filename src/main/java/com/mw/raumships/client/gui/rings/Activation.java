package com.mw.raumships.client.gui.rings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Activation {
	protected abstract List<Vector3f> getVertices();
	protected abstract int getRayGroupCount();
	
	protected abstract Vector3f getTranslation(World world, BlockPos pos);

	protected abstract void check(World world, BlockPos pos, EntityPlayer player, int x, int i);
	protected abstract void brbCheck(List<Ray> brbRayList, Vec3d lookVec, EntityPlayer player, BlockPos pos);
	
	public void onActivated(World world, BlockPos pos, EntityPlayer player, float rotation) {		
		// Last common function, x=a, y=b
		Ray lastRay = null;
		Ray firstRay = null;
		List<Ray> brbRayList = new ArrayList<Ray>();
		Vec3d lookVec = player.getLookVec();
				
		boolean breakLoop = false;
		
		for (int x=1; x<=getVertices().size()/getRayGroupCount(); x++) {
		//for (int x=1; x<=1; x++) {
			Ray currentRay;
			
			// Last run, current ray should be the first one
			if (x == getVertices().size()/getRayGroupCount())
				currentRay = firstRay;
			else 
				currentRay = new Ray( getTransposedRay(x, rotation, world, pos, player) );
			
			// First run, we need to calculate the first right limiter function
			if (lastRay == null) {
				lastRay = firstRay = new Ray( getTransposedRay(0, rotation, world, pos, player) );	
				//brbRayList.add(firstRay);
			}
			
			List<Ray> transverseRays = new ArrayList<Ray>();
			
			for (int i=0; i<getRayGroupCount(); i++) {
				Ray r = new Ray( currentRay.getVert(i), lastRay.getVert(i) );
				transverseRays.add( r );
				
				if ( i == 2 ) {
					brbRayList.add( r );
				}
			}	
			
			for (int i=0; i<getRayGroupCount()-1; i++) {					
				Box box = new Box(currentRay, lastRay, transverseRays.get(i), transverseRays.get(i+1), i);
				
				if (box.checkForPointInBox( new Vector2f( (float)lookVec.x, (float)lookVec.z ) )) {
					check(world, pos, player, x, i);
					
					breakLoop = true;
					break;
				}
			}
			
			if (breakLoop)
				break;
			
			lastRay = currentRay;
		}
		
		brbCheck(brbRayList, lookVec, player, pos);
		
	}
	
	private Vector2f getTransposed(Vector3f v, float rotation, World world, BlockPos pos, EntityPlayer player) {
		DHDVertex current = new DHDVertex(v.x, v.y, v.z);

		return current.rotate( rotation ).localToGlobal(pos, getTranslation(world, pos)).calculateDiffrence(player).getViewport( player.getLookVec() );
	}
	
	// Ray = set of getRayGroupCount() vertices
	private List<Vector2f> getTransposedRay(int rayIndex, float rotation, World world, BlockPos pos, EntityPlayer player) {
		List<Vector2f> out = new ArrayList<Vector2f>();
		
		for (int i=0; i<getRayGroupCount(); i++) {
			out.add( getTransposed( getVertices().get(rayIndex*getRayGroupCount() + i), rotation, world, pos, player) );
		}
		
		return out;
	}
}