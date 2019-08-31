package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.interfaces.Parent;

/**
 * PointSlaver
 */
public abstract class PointSlaver extends Actor implements Parent<Point>{

    public static ArrayList<PointSlaver> pointSlavers = new ArrayList<>();
    private ArrayList<Point> slavePoints = new ArrayList<>();
    private ArrayList<Point> masterPoints = new ArrayList<>();
    
    PointSlaver(Point ... masters) {
        
        for (Point k : masters) {
            masterPoints.add(k);
            k.setMaster(true);
            k.addObjectParent(this);
        }

        pointSlavers.add(this);
    }

    public void addMasterPoint(Point k){
        masterPoints.add(k);
        k.setMaster(true);
        k.addObjectParent(this);
    }
    public void addSlavePoint(Point k){
        adjustSlave(k);
        slavePoints.add(k);
        k.setSlave(true);
        k.addObjectParent(this);
    }

    ArrayList<Point> getMasterPoints(){
        return masterPoints;
    }
    public abstract void adjustSlave(Point k);
    public abstract void updateCoeficients();

    boolean ignoreSlave = false;
    @Override
    public void updatePosition(float x, float y, Point k) {

        if(ignoreSlave) return;
        
        ignoreSlave = true;
        if(masterPoints.contains(k)){
            updateCoeficients();
            for (Point t : slavePoints) {
                adjustSlave(t);
            }
        }else adjustSlave(k);

        ignoreSlave = false;
    }

    @Override
    public ArrayList<Point> getChildList() {
		return slavePoints;
	}
}