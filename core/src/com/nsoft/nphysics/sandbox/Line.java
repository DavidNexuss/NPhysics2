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
public class Line extends PointSlaver implements  Say {


    static final float drawLenght = 200000;

    Vector2 startBuffer,endBuffer = new Vector2();
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
            B = Point.getPoint(p.x, p.y);
            NPhysics.currentStage.addActor(B);
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
    }

    public void updateCoeficients(){

        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();
        m = (vq.y - vp.y) / (vq.x - vp.x);
        n = vq.y - m * vq.x;

        startBuffer.set(drawLenght, drawLenght * m + n);
        endBuffer.set(-drawLenght, -drawLenght *m + n);
    }

    @Override
    public void adjustSlave(Point k){
        
        k.setPosition(k.getX(), k.getX() * m + n,false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.setColor(Color.BLUE);
        NPhysics.currentStage.shapeline.line(startBuffer, endBuffer);
        NPhysics.currentStage.shapeline.end();
    }
    
}