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

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Point;

/**
 * MediatrixSlaver
 */
public class MediatrixSlaver extends LineSlaver{

    Point C;
    Vector2 vc;

    public static void createMediatrix(Vector2 p){
        if(Atmp != null){
            NPhysics.currentStage.addActor(new MediatrixSlaver(Atmp,Point.getPoint(p.x, p.y)));
            Atmp = null;
            return;
        }

        Atmp = Point.getPoint(p.x, p.y);
    }
    public MediatrixSlaver(Point P,Point Q){
        super(Q, P);
    }

    @Override
    public void updateCoeficients() {
        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();
        
        vertical = (vq.y - vp.y) == 0;

        m = (vq.y - vp.y) / (vq.x - vp.x);
        m = -1 / m;

        if(vc == null)vc = new Vector2();

        vc.set(new Vector2(vp).sub(vq).scl(.5f).add(vq));
        n = vc.y - m * vc.x;

        if(C == null){
            C = Point.getPoint(vc.x, vc.y);
            addMasterPoint(C);
        }else C.setPosition(vc.x, vc.y);
        
        if(vertical){

            startBuffer.set(vc.x,drawLenght);
            endBuffer.set(vc.x,-drawLenght);
        }else{

            startBuffer.set(drawLenght, drawLenght * m + n);
            endBuffer.set(-drawLenght, -drawLenght *m + n);
        }
        
        collisionCheck();
    }
}