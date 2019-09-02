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

package com.nsoft.nphysics.sandbox.math;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.Point;

/**
 * ArcSlaver
 */

public class ArcSlaver extends PointSlaver implements Say{

    // Q , P Arcs
    // C Center

    MediatrixSlaver mediatrix;
    Vector2 bufferQ,bufferP,bufferC;

    float radius,start,degrees;
    Point Q,P,C;

    static Point aq,ap;

    public static void createArcSlaver(Vector2 vc){

        if(aq == null){
            aq = Point.getPoint(vc.x, vc.y);
            return;
        }
        if(ap == null){
            ap = Point.getPoint(vc.x, vc.y);
            return;
        }

        NPhysics.currentStage.addActor(new ArcSlaver(aq, ap, Point.getPoint(vc.x, vc.y)));
        ap = null;
        aq = null;
    }
    public ArcSlaver(Point Q,Point P,Point C){
        super(Q, P);
        this.Q = Q;
        this.P = P;
        this.C = C;

        bufferQ = new Vector2();
        bufferP = new Vector2();
        bufferC = new Vector2();

        mediatrix = new MediatrixSlaver(P, Q);
        mediatrix.addSlavePoint(C);

        addMasterPoint(C);
        NPhysics.currentStage.addActor(mediatrix);
        updateCoeficients();

    }
    @Override
    public void adjustSlave(Point k) {

        float w = k.getVector().sub(bufferC).angle();
       // System.out.println(w);

        w = Math.max(Math.min(start + degrees, w), start);

        k.setPosition(MathUtils.cos(w * MathUtils.degRad) * radius + bufferC.x, 
                      MathUtils.sin(w * MathUtils.degRad) * radius + bufferC.y);
    }

    @Override
    public void updateCoeficients() {

        bufferQ.set(Q.getVector());
        bufferP.set(P.getVector());
        bufferC.set(C.getVector());

        Vector2 pc = new Vector2(bufferP).sub(bufferC);
        Vector2 qc = new Vector2(bufferQ).sub(bufferC);

        radius = new Vector2(bufferC).sub(bufferP).len();
        start = qc.angle();
        degrees = pc.angle() - start;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.setColor(getColor());
        NPhysics.currentStage.shapeline.arc(bufferC.x, bufferC.y, radius, start, degrees,32);
        NPhysics.currentStage.shapeline.end();
    }

    @Override
    public boolean isInside(float x, float y) {
        
        if(!super.isInside(x, y)) return false;
        
        float r = (new Vector2(x,y).sub(bufferC)).len();

        float factor = 40 * NPhysics.currentStage.getZoom();
        return r < radius + factor && r > radius - factor;
    }
}