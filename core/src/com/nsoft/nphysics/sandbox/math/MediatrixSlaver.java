package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;

/**
 * MediatrixSlaver
 */
public class MediatrixSlaver extends Line{



    static Point A,B;
    Point C;
    Vector2 vc;

    public static void createMediatrix(Vector2 p){
        if(A != null){
            B = Point.getPoint(p.x, p.y);
            NPhysics.currentStage.addActor(B);

            NPhysics.currentStage.addActor(new MediatrixSlaver(A,B));
            A = null;
            B = null;
            return;
        }

        A = Point.getPoint(p.x, p.y);
        NPhysics.currentStage.addActor(A);
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