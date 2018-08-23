package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class Builder {

	public static DSL solve(PolygonActor a) {
		
		System.out.println("\n\n\n\n\n\n\n");
		DSL l = new DSL();
		
		for (ObjectChildren object : a.getObjectChildrenList()) {
			
			if(object instanceof AxisSupport) {
				
				l.addForce(new Force(object.getRelativePosition(true), Force.NULL_V).setParent(object));
			} else
			if(object instanceof ForceComponent) {
				
				ForceComponent c = (ForceComponent)object;
				Vector2 pos = object.getRelativePosition(true);
				
				if(c.variable == Variable.X) {
					Force f =new Force(pos.x, pos.y, Force.NULL, c.getForce().y).setParent(object);
					f.setVariable(Variable.X);
					l.addForce(f);
				}else
				if(c.variable == Variable.Y) {
					
					Force f =new Force(pos.x, pos.y, c.getForce().x, Force.NULL).setParent(object);
					f.setVariable(Variable.Y);
					l.addForce(f);
				}
				else {
					l.addForce(new Force(pos, c.getForce()).setParent(object));
				}
			}
		}
		
		l.addForce(new Force(Vector2.Zero, new Vector2(0, a.calculateMass() * 9.8f)));
		
		l.solve();
		return l;
	}
}
