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
import com.nsoft.nphysics.sandbox.Point;

/**
 * VectorLineSlaver
 */
public class VectorLineSlaver extends LineSlaver {

    Vector2 dir;
    public VectorLineSlaver(Point Q, Vector2 dir) {
        super(Q, null);
        this.dir = new Vector2(dir);
        updateCoeficients();
    }

    @Override
    public void updateCoeficients() {
        if(dir == null) return;

        Vector2 vq = getMasterPoints().get(0).getVector();
        
        vertical = dir.x == 0;
        m = (dir.y / dir.x);
        n = vq.y - m* vq.x;
        if(vertical){
            startBuffer.set(vq.x,drawLenght);
            endBuffer.set(vq.x,-drawLenght);
        }else{
            startBuffer.set(drawLenght, drawLenght * m + n);
            endBuffer.set(-drawLenght, -drawLenght *m + n);
        }

        collisionCheck();
    }
    
}