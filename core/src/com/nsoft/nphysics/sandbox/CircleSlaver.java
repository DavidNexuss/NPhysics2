package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;

/**
 * CircleSlaver
 */
public class CircleSlaver extends PointSlaver {

    Point C;
    float radius;
    Vector2 centerBuffer;


    static Point A,B;
    public static void createCircle(Vector2 p){

        if(A != null && B != null){
            NPhysics.currentStage.addActor(new CircleSlaver(A,B));
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
        NPhysics.currentStage.shapeline.setColor(Color.BLUE);
        NPhysics.currentStage.shapeline.circle(centerBuffer.x, centerBuffer.y, radius);
        NPhysics.currentStage.shapeline.end();
    }

    
}