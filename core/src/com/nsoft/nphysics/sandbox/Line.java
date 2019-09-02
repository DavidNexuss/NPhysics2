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
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.interfaces.Parent;


/**
 * CollisionProfile
 */
class CollisionProfile {

    Line A,B;
    Point collision;

    CollisionProfile(Line A,Line B, Point collision){
        this.A = A;
        this.B = B;
        this.collision = collision;
        collision.setColor(Color.TEAL);
        collision.staticPosition = true;
    }

    public int combined(){
        return A.hashCode() * B.hashCode();
    }
}
/**
 * Line
 */
public class Line extends PointSlaver implements  Say {


    public static ArrayList<Line> lineList = new ArrayList<>();
    public static HashMap<Integer,CollisionProfile> map = new HashMap<>();

    static final float drawLenght = 200000;

    Vector2 startBuffer,endBuffer = new Vector2();
    float m;
    float n;
    boolean vertical;

    static Point A,B;
    public static void createLine(Vector2 p){
        if(A != null){
            B = Point.getPoint(p.x, p.y);
            NPhysics.currentStage.addActor(B);

            NPhysics.currentStage.addActor(new Line(A,B));
            A = null;
            B = null;
            return;
        }

        A = Point.getPoint(p.x, p.y);
        NPhysics.currentStage.addActor(A);
    }
    public Line(Point Q,Point P){
        super(Q, P);
        startBuffer = new Vector2();
        endBuffer = new Vector2();
        updateCoeficients();
        lineList.add(this);
    }

    public void updateCoeficients(){

        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();

        vertical = (vq.x - vp.x) == 0;

        m = (vq.y - vp.y) / (vq.x - vp.x);
        n = vq.y - m * vq.x;

        if(vertical){

            startBuffer.set(vp.x,drawLenght);
            endBuffer.set(vp.x,-drawLenght);
        }else{

            startBuffer.set(drawLenght, drawLenght * m + n);
            endBuffer.set(-drawLenght, -drawLenght *m + n);
        }

        collisionCheck();

    }

    public void collisionCheck(){

        for (Line other : lineList) {

            Vector2 vc = new Vector2(

                (other.n - n) / (m - other.m),
                (other.n - n) / (m - other.m) * m + n
            );
            if(map.containsKey(hashCode() * other.hashCode())){
                map.get(hashCode() *other.hashCode()).collision.setPosition(vc.x, vc.y);
            }else{

                map.put(hashCode() * other.hashCode(), new CollisionProfile(this, other, Point.getPoint(vc.x, vc.y)));
            }
        }
    }
    @Override
    public void adjustSlave(Point k){
        
        if(vertical){
            k.setPosition(getMasterPoints().get(0).getVector().x, k.getY());
        }else{

            k.setPosition(k.getX(), k.getX() * m + n);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.setColor(getColor());
        NPhysics.currentStage.shapeline.line(startBuffer, endBuffer);
        NPhysics.currentStage.shapeline.end();
    }

    @Override
    public boolean isInside(float x, float y) {
        
        if(!super.isInside(x, y)) return false;
        float x1 = startBuffer.x;
        float y1 = startBuffer.y;
        float x2 = endBuffer.x;
        float y2 = endBuffer.y;

        float d = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2*y1 - y2*x1);
        d/=Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1));

        return d < 20;
    }

    @Override
    public boolean remove() {

        lineList.remove(this);
        return super.remove();
    }
    
}