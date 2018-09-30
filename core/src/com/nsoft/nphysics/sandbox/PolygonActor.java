package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.DiscontLine;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

import earcut4j.Earcut;

public class PolygonActor extends PhysicalActor<PolygonDefinition> implements Removeable{

	private Point initial;
	private ArrayList<Segment> segments = new ArrayList<>();
	private ArrayList<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private float X,Y,width,height; //BOUNDS
	
	private Polygon hitboxPolygon; 
	
	private Vector2 polygonMassCenter = new Vector2();
	private SimpleArrow gravityArrow;
	private DoubleArrow xaxis;
	private DoubleArrow yaxis;
	private DiscontLine line;
	
	private ArrowLabel angleLabel;
	private static float axisMargin = 20;
	
	private int forceVariableCount;
	
	private float physMass;
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		line = new DiscontLine(new Vector2(), new Vector2());
		line.setVisible(false);
		addActor(line);
		
		angleLabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		angleLabel.setColor(Color.BLUE);
	}

	public void initDefinition() {
		
		definition = new PolygonDefinition();
	}
	
	public PolygonActor createCopy(Vector2 offset) {
		
		PolygonActor newpolygon = new PolygonActor();
		Vector2 center = definition.getCenter(false);
		
		Point first = null;
		for (Point p : points) {
			
			Point pi = new Point(p.getX() - center.x + offset.x, p.getY() - center.y + offset.y, false);
			if(first == null) {
				
				first = pi;
			}
			newpolygon.addPoint(pi);
			NPhysics.currentStage.addActor(pi);
		}
		
		newpolygon.addPoint(first);
		
		DynamicWindow.dumpConfiguration(this, newpolygon);
		
		return newpolygon;
	}

	
	public void updateForceVariableCount() {
		forceVariableCount = 0;
		for (ObjectChildren f : getComponents()) {
			
			if(f instanceof ForceComponent) {
				
				forceVariableCount += ((ForceComponent) f).isVariable() ? 1 : 0;
			}
		}
	}
	@Override
	public boolean isInside(float x, float y) {
		
		if (x < X || x > width || y < Y|| y > height) return false;
		if(hitboxPolygon.contains(x, y)) return true;
		return false;
	}
	
	final Vector2 origin = new Vector2(0, 0);
	final Vector2 start = new Vector2(0, 0);
	@Override
	public void addDragListener() {


		ClickIn Pointer = this;
		DragListener d = new DragListener() {
		    
			public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	doDrag(true,x,y,event);
		    }
		    
		    @Override
		    public void dragStart(InputEvent event, float x, float y, int pointer) {
		    	
		    	start.set(NPhysics.currentStage.getUnproject(false));
		    	origin.set(NPhysics.currentStage.getUnproject(false));
		    	if(!isSelected()) getHandler().setSelected(Pointer);
				
		    	sumx = 0;
		    	sumy = 0;
		    	
		    	for (Point p : points) {
					
		    		p.originx = p.getX();
		    		p.originy = p.getY();
				}
		    	
		    }
		    
		    @Override
		    public void dragStop(InputEvent event, float x, float y, int pointer) {
		    	
		    	updatePosition(0, 0, null);
		    }
		};
		
		d.setTapSquareSize(1);
		addListener(d);
	}
	
	float sumx = 0;
	float sumy = 0;
	@Override
	public void doDrag(boolean pool, float x, float y,InputEvent event) {
		
		Vector2 v2 = new Vector2(NPhysics.currentStage.getUnproject(false)).sub(start);
		for (Point p : points) {

			if (NPhysics.currentStage.isSnapping()) {
				
				p.setPosition(DragStage.snapGrid(p.originx + sumx),DragStage.snapGrid(p.originy + sumy));
				
			}else {
				p.moveBy(v2.x, v2.y);
				updatePosition(0, 0, null);
			}
			
		}
		
		start.set(NPhysics.currentStage.getUnproject(false));
		

		sumx = start.x - origin.x;
		sumy = start.y - origin.y;

	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		if(!isEnded()) return;
		
		Util.renderPolygon(NPhysics.currentStage.shapefill, points, indexes);
		
		NPhysics.currentStage.shapefill.setColor(Color.GRAY);
		NPhysics.currentStage.shapefill.circle(polygonMassCenter.x, polygonMassCenter.y, 5);
		
		if(hookRotation && useAxis) {
			
			angleLabel.setVisible(true);
			NPhysics.currentStage.shapefill.setColor(arcColor);
			float angle = (line.getDiff().angleRad())*MathUtils.radDeg;
			if(angle < 0) {
				angle = Math.abs(angle);
				angle = 360 - angle;
			}
			angleLabel.setText(((int)(angle / 5))*5 + "º");
			angleLabel.setPosition(new Vector2(NPhysics.currentStage.getAxisPosition().getVector()).add(new Vector2(120, 0).rotate(angle)));
			NPhysics.currentStage.shapefill.arc(NPhysics.currentStage.getAxisPosition().getX(), NPhysics.currentStage.getAxisPosition().getY(), 100, 0, angle);
		}else angleLabel.setVisible(false);
		super.draw(batch, parentAlpha);
		
	}
	public PolygonActor addPoint(Point p){
		
		if(isEnded()) return this;
		
		if(points.size() == 0) initial = p;
		
		for (Point pa : points) {
			
			if(pa == p && p != initial)return this;
		}
		
		if(p == initial && points.size() > 1) {
			
			Segment c = new Segment(points.get(points.size() - 1),p);
			segments.add(c);
			NPhysics.sandbox.addActor(c);
			
			end(); 
			return this;
		}
		
		if(points.size() != 0) {
			
			Segment c = new Segment(points.get(points.size() - 1), p);
			segments.add(c);
			NPhysics.sandbox.addActor(c);
		}
		
		points.add(p);
		
		return this;
	}
	
	public ArrayList<Point> getPointList() {return points;}
	
	private void createBuffer() {
		
		buffer = new double[points.size()*2];
		
		for (int i = 0; i < points.size()*2; i+=2) {
				
			buffer[i] = points.get(i/2).getX();
			buffer[i + 1] = points.get(i/2).getY();
		}
	}
	private void triangulate() {
		
		createBuffer();
		
		indexes = (ArrayList<Integer>) Earcut.earcut(buffer);
	}
	
	private void calculaeBounds() {
		
		X = Float.MAX_VALUE;
		Y = Float.MAX_VALUE;
		height = Float.MIN_VALUE;
		width = Float.MIN_VALUE;
		
		for (Point p : points) {
			
			float px = p.getX();
			float py = p.getY();
			
			if(px < X) X = px;
			if(px > width) width = px;
			if(py < Y) Y = py;
			if(py > height) height = py;
		}
	}

	private void createHitBox() {
		calculaeBounds();
		hitboxPolygon = new Polygon(definition.getRawVertices());
	}
	private void calculateHitBox() {
		
		calculaeBounds();
		hitboxPolygon.setVertices(definition.getRawVertices());
		xaxis.setPosition(new PositionVector(X,Y - axisMargin), new PositionVector(width,Y - axisMargin));
		yaxis.setPosition(new PositionVector(X - axisMargin,Y), new PositionVector(X - axisMargin,height));
		
		calculateMass();
		
	}
	
	public float getArea(boolean m2) {
		
		if(m2) return hitboxPolygon.area() / (30*30);
		else return hitboxPolygon.area();
	}
	public float calculateMass() {
		
		physMass = definition.density * getArea(true);
		setValue("polygon_phys_mass", physMass);
		return physMass;
	}
	public float calculateDensity() {
		
		definition.density = physMass / getArea(true);
		setValue("polygon_phys_density", definition.density);
		return definition.density;
	}
	private void createDefinition() {
		
		definition.vertices.clear();
		definition.indexes.clear();
		
		definition.indexes = indexes;
		for (Point p : points) {
			
			definition.vertices.add(new PositionVector(p.getX(), p.getY()));
		}
		
		definition.init();
		definition.childrens = getComponents();
		
		polygonMassCenter.set(definition.getCenter(false));
		
	}
	
	private void createArrow() {
		
		if(gravityArrow == null) {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow = new SimpleArrow(start, new Vector2(start).add(0,-Math.abs(hitboxPolygon.area() * Util.UNIT * 9.8f / SimulationStage.ForceMultiplier / (30*30))));
			addActor(gravityArrow);
			gravityArrow.setColor(Color.BLUE);
		}else {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow.setStart(start);
			gravityArrow.setEnd(new Vector2(start).add(0,-Math.abs(hitboxPolygon.area() * Util.UNIT * 9.8f / SimulationStage.ForceMultiplier / (30*30))));
			gravityArrow.updateVertexArray();
		}
	}
	@Override
	public void end() {
		
		super.end();
		for (Point point : points) {
			
			point.setObjectParent(this);
		}
		
		triangulate();
		
		if(temp == this) {
			
			NPhysics.sandbox.addActor(this);
			temp = null;
		}

		createDefinition();
		createHitBox();
		createArrow();
		xaxis = new DoubleArrow(new PositionVector(X,Y - axisMargin), new PositionVector(width,Y - axisMargin));
		yaxis = new DoubleArrow(new PositionVector(X - axisMargin,Y), new PositionVector(X - axisMargin,height));
		
		addActor(xaxis);
		addActor(yaxis);
		

		calculateMass();
	}

	boolean hookRotation;
	private float angle;
	private boolean useAxis;
	public void hookRotation(boolean hook,boolean useAxisAsPivot) {
		
		if(!isEnded()) throw new IllegalStateException("");
		hookRotation = hook;
		useAxis = useAxisAsPivot;
		if(hookRotation) {
			tempCenter.set(useAxisAsPivot ? NPhysics.currentStage.getAxisPosition().getVector() : definition.getCenter(false));
			line.setPositionA(new Vector2(tempCenter).sub(getPosition()));
			line.hook(hookRotation);
			line.setOffset(getPosition());
			line.setVisible(true);
			
			for (Point p : points) {
				
				p.initial = new Vector2(p.getVector());
			}
			
			line.act(Gdx.graphics.getDeltaTime());
		}else {
			
			line.setVisible(false);
			angle = line.getDiff().angleRad();
		}
	}
	
	Vector2 tempCenter = new Vector2();
	@Override
	public void act(float delta) {
		
		if(hookRotation) {
			
			rotateVertices(tempCenter, Math.round((line.getDiff().angleRad() - angle)* MathUtils.radDeg / 5)*5  * MathUtils.degRad);
			if(!isSelected()) hookRotation(false,useAxis);
		}
		super.act(delta);
	}
	public void rotateVertices(Vector2 pivot,float angleRad){
		
		for (Point p : points) {
			
			Vector2 pos = p.initial;
			Vector2 npos = Util.rotPivot(pivot, pos, angleRad);
			p.setPosition(npos.x, npos.y,false);
		}
		
		updatePosition();
	}
	
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(!isEnded())return;
		triangulate();

		createDefinition();
		calculateHitBox();
		createArrow();
		if(p != null) angle = 0 ;
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return isEnded() ? points : null;
	}
	
	@Override
	public boolean remove() {
		
		for (Segment s : segments) {
			s.remove();
		}
		return super.remove();
	}
	
	public Vector2 getPosition() {return new Vector2(getX(), getY());}

	@Override
	public void updateValuesFromForm() {
		
		float newDensity = getValue("polygon_phys_density");
		
		float newMass = getValue("polygon_phys_mass");
		
		if( definition.density != newDensity) {
			
			definition.density = newDensity;
			calculateMass();
		
		}else if(physMass != newMass) {
			
			physMass = newMass;
			calculateDensity();
		}
		definition.friction = getValue("polygon_phys_friction");
		definition.restitution = getValue("polygon_phys_restitution");
		
		switch ((int)getValue("polygon_phys_state")) {
		case 2:
			definition.type = BodyType.StaticBody;
			break;
		case 1:
			definition.type = BodyType.KinematicBody;
			break;
		case 0:
			definition.type = BodyType.DynamicBody;
			break;
		default:
			throw new IllegalStateException();
		}
		
		definition.linearVelocity.set(getValue("polygon_lvel_x"), 
									  getValue("polygon_lvel_y"));
		
	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
