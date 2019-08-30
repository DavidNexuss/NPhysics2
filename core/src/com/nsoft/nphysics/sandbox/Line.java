/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.interfaces.Parent;

/**
 * Line
 */
public class Line extends Actor implements Parent<Point>, Say {


    static final float drawLenght = 200000;

    ArrayList<Point> slavePoints = new ArrayList<>();
    Point masterPointQ,masterPointP;
    
    Vector2 startBuffer = new Vector2();
    Vector2 endBuffer = new Vector2();

    Vector2 q,p;
    //f(x,y) = (yq−yp)x −(xq−xp)y + (xq yp) − (yq xp)= 0
    float a; // = (yq - yp)
    float b; // = -(xq - xp)
    float c; // = (xp * yp) - (yq * xp)
    float m;
    float n;

    static Point A,B;
    public static void createLine(Vector2 p){
        if(A != null && B != null){
            NPhysics.currentStage.addActor(new Line(A,B));
            A = null;
            B = null;
            return;
        }
        if(A != null){
            B = new Point(p.x, p.y, false);
            NPhysics.currentStage.addActor(B);
            return;
        }

        A = new Point(p.x, p.y, false);
        NPhysics.currentStage.addActor(A);
    }
    public Line(Point Q,Point P){

        this.q = new Vector2(Q.getVector());
        this.p = new Vector2(P.getVector());

        masterPointQ = Q;
        masterPointP = P;
        
        masterPointP.setColor(Color.RED);
        masterPointQ.setColor(Color.RED);

        masterPointQ.setObjectParent(this);
        masterPointP.setObjectParent(this);

        updateCoeficients();

    }

    private void updateVectors(){
        p.set(masterPointQ.getVector());
        q.set(masterPointP.getVector());
    }
    private void updateCoeficients(){

        a = q.y - p.y;
        b = -(q.x - p.x);
        c = (q.x * p.y) - (q.y * p.x);

        m = a / -b;
        n = q.y - m * q.x;

        startBuffer.set(drawLenght, drawLenght * m + n);
        endBuffer.set(-drawLenght, -drawLenght *m + n);
    }

    public void addSlavePoint(Point k){

        adjustSlave(k);
        slavePoints.add(k);
        k.addObjectParent(this);
    }
    private void adjustSlave(Point k){
        
        k.setPosition(k.getX(), k.getX() * m + n,false);
    }
    public Vector2 getStartBuffer() {
        return startBuffer;
    }

    public Vector2 getEndBuffer(){
        return endBuffer;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.setColor(Color.BLUE);
        NPhysics.currentStage.shapeline.line(getStartBuffer(), getEndBuffer());
        NPhysics.currentStage.shapeline.end();
    }

    @Override
    public void updatePosition(float x, float y, Point p) {

        if(p == masterPointQ || p == masterPointP){
            updateVectors();
            updateCoeficients();
            for (Point k : slavePoints) {
                adjustSlave(k);
            }
        }else adjustSlave(p);
    }

    @Override
    public ArrayList<Point> getChildList() {
		return null;
	}

    
}