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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;

/**
 * TangentSlaver
 */
public class TangentSlaver extends Line {


    static Point Qtmp;

    public static void createTangentSlaver(Vector2 vc){

        if(Qtmp == null){
            Qtmp = Point.getPoint(vc.x, vc.y);
            return;
        }

        NPhysics.currentStage.addActor(new TangentSlaver(Qtmp, Point.getPoint(vc.x, vc.y)));
        Qtmp = null;
    }
    public TangentSlaver(Point Q, Point C){
        super(Q, C);
    }

    @Override
    public void updateCoeficients() {
        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();
        
        vertical = (vq.y - vp.y) == 0;

        m = (vq.y - vp.y) / (vq.x - vp.x);
        m = -1 / m;

        n = vp.y -m *vp.x;

        if(vertical){

            startBuffer.set(vp.x,drawLenght);
            endBuffer.set(vp.x,-drawLenght);
        }else{

            startBuffer.set(drawLenght, drawLenght * m + n);
            endBuffer.set(-drawLenght, -drawLenght *m + n);
        }
        
        collisionCheck();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();

        NPhysics.currentStage.shapeline.setColor(Color.GRAY);
        NPhysics.currentStage.shapeline.begin(ShapeType.Line);
        NPhysics.currentStage.shapeline.line(vq.x, vq.y, vp.x, vp.y);
        NPhysics.currentStage.shapeline.end();
    }
}