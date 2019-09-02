package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

/**
 * VectorLineSlaver
 */
public class VectorLineSlaver extends Line {

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