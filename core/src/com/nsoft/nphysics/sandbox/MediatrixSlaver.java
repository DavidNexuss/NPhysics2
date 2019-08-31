package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.Vector2;

/**
 * MediatrixSlaver
 */
public class MediatrixSlaver extends Line{

    public MediatrixSlaver(Point P,Point Q){
        super(Q, P);
    }

    @Override
    public void updateCoeficients() {
        Vector2 vq = getMasterPoints().get(0).getVector();
        Vector2 vp = getMasterPoints().get(1).getVector();
        
        m = (vq.y - vp.y) / (vq.x - vp.x);
        m = -1 / m;

        Vector2 vc = new Vector2(vp).sub(vq).scl(.5f).add(vq);
        n = vc.y - m * vc.x;

        startBuffer.set(drawLenght, drawLenght * m + n);
        endBuffer.set(-drawLenght, -drawLenght *m + n);

    }
}