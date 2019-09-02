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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Point;

/**
 * CircleSlaver
 */
public class CircleSlaver extends PointSlaver {

    Point C;
    float radius;
    Vector2 centerBuffer;


    static Point Atmp;
    public static void createCircle(Vector2 p){
        
        if(Atmp != null){
            NPhysics.currentStage.addActor(new CircleSlaver(Atmp,Point.getPoint(p.x, p.y)));
            Atmp = null;
            return;
        }

        Atmp = Point.getPoint(p.x, p.y);
    }
    
    public CircleSlaver(Point C,Point Q){
        super(C,Q);
        this.C = C;
        centerBuffer = new Vector2();
        updateCoeficients();
    }
    @Override
    public void adjustSlave(Point k) {
        float w = k.getVector().sub(centerBuffer).angle();
        k.setPosition(MathUtils.cos(w * MathUtils.degRad) * radius + centerBuffer.x, 
                      MathUtils.sin(w * MathUtils.degRad) * radius + centerBuffer.y);
        
    }

    @Override
    public void updateCoeficients() {
        centerBuffer.set(C.getVector());
        radius = C.getVector().sub(getMasterPoints().get(1).getVector()).len();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.setColor(getColor());
        NPhysics.currentStage.shapeline.circle(centerBuffer.x, centerBuffer.y, radius);
        NPhysics.currentStage.shapeline.end();
    }

    @Override
    public boolean isInside(float x, float y) {
        
        if(!super.isInside(x, y)) return false;
        
        float r = (new Vector2(x,y).sub(centerBuffer)).len();

        float factor = 40 * NPhysics.currentStage.getZoom();
        return r < radius + factor && r > radius - factor;
    }
    
}