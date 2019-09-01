package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;

/**
 * PointSlaver
 */
public abstract class PointSlaver extends Actor implements Parent<Point>, ClickIn, Removeable{

    public static ArrayList<PointSlaver> pointSlavers = new ArrayList<>();
    
    public static Color base = Color.BLUE;
    public static Color select = new Color(.7f, .7f, 0.2f, 1);

    private ArrayList<Point> slavePoints = new ArrayList<>();
    private ArrayList<Point> masterPoints = new ArrayList<>();
    

    PointSlaver(Point ... masters) {
        
        for (Point k : masters) {
            if(k == null) continue;
            masterPoints.add(k);
            k.setMaster(true);
            k.addObjectParent(this);
        }

        pointSlavers.add(this);
        addInput();
        setColor(base);
        setZIndex(0);
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
    
    @Override
    public SelectHandle getHandler() {
        return Sandbox.mainSelect;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {

        return isInside(x, y) ? this : null;
    }

    @Override
    public void select(int pointer) {
        setColor(select);
    }

    @Override
    public void unselect() {
        setColor(base);
    }

    @Override
    public boolean isInside(float x, float y) {
        if(Point.isThereAPoint(x, y) != null) return false;
        return true;
    }

    @Override
    public boolean remove() {
        
        super.remove();
        for (Point m : masterPoints) {
            
            if(m.getObjectParents().size() > 1) continue;
            m.remove();
        }

        for (Point m : slavePoints) {
            
            if(m.getObjectParents().size() > 1) continue;
            m.remove();
        }

        pointSlavers.remove(this);
        return true;
    }
}