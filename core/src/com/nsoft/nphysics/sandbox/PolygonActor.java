package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.AngleArcActor;
import com.nsoft.nphysics.sandbox.drawables.DiscontLine;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.Option;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dsl.Force;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;
import earcut4j.Earcut;

public class PolygonActor extends Group implements Parent<Point>,ClickIn,Handler,Removeable,Draggable,Form{

	public static ArrayList<PolygonActor> polygonlist = new ArrayList<>();
	private Point initial;
	private ArrayList<Point> points = new ArrayList<>();
	private ArrayList<Segment> segments = new ArrayList<>();
	private ArrayList<ObjectChildren> components = new ArrayList<>();
	private ArrayList<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private boolean end = false;
	private float X,Y,width,height; //BOUNDS
	
	private PolygonDefinition definition;
	private Polygon hitboxPolygon;
	
	public SelectHandle handler = new SelectHandle();  
	
	private Vector2 polygonMassCenter = new Vector2();
	private SimpleArrow gravityArrow;
	private DoubleArrow xaxis;
	private DoubleArrow yaxis;
	private DiscontLine line;
	private AngleArcActor arc;
	
	private DynamicWindow form;
	private static float axisMargin = 20;
	
	private int forceVariableCount;
	private String forceVariableCount_str = "0";
	
	private float physMass;
	
	@Override public SelectHandle getSelectHandleInstance() { return handler; }
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		setDebug(true, true);
		definition = new PolygonDefinition();
		initForm();
		addInput();
		addDragListener();
		line = new DiscontLine(new Vector2(), new Vector2());
		line.setVisible(false);
		addActor(line);
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
	public boolean keyDown(int keycode){
		
		if(keycode == Keys.Q) {
			
			if(!form.isVisible()) showForm();
			return true;
		}
		
		return false;
	}

	private void initForm() {
		
		form = DynamicWindow.createDefaultWindowStructure("Wpolygon");
		form.setSize(450, 450);
		form.setAsForm(this);
		
		form.addOption(Options.createOptionTypeSlider("polygon_phys_state", NDictionary.get("phys_DYNAMIC"),NDictionary.get("phys_KINEMATIC"),NDictionary.get("phys_STATIC")));
		form.addOption(Options.createOptionNumber("polygon_lvel_x"));
		form.addOption(Options.createOptionNumber("polygon_lvel_y"));
		form.addOption(Options.createOptionNumber("polygon_phys_mass"));
		
		form.addOption(Options.createOptionNumber("polygon_phys_density"));
		form.addOption(Options.createOptionNumber("polygon_phys_friction"));
		form.addOption(Options.createOptionNumber("polygon_phys_restitution"));
		
		form.getOption("polygon_phys_density").setValue(definition.density);
		form.getOption("polygon_phys_friction").setValue(definition.friction);
		form.getOption("polygon_phys_restitution").setValue(definition.restitution);
		
		
		VisTable solve_dsl = new VisTable();
		VisLabel dsl_t = new VisLabel(NDictionary.get("dsl_unknowns"));
		dsl_t.setStyle(new LabelStyle(FontManager.subtitle, Color.WHITE));
		VisLabel dsl_n = new VisLabel() {
			
			@Override
			public void act(float delta) {
				
				setText(forceVariableCount_str);
				super.act(delta);
			}
		};
		VisTextButton dsl_b= new VisTextButton(NDictionary.get("dsl_solve"));
		
		solve_dsl.add(dsl_t).expand().align(Align.left);
		solve_dsl.add(dsl_n).prefWidth(50).width(50);
		solve_dsl.add(dsl_b).fillX().expand().padLeft(15);
		
		solve_dsl.pad(5);
		form.addRawTable(solve_dsl);
		
		form.setVisible(false);
		NPhysics.ui.addActor(form);
	}
	public PolygonDefinition getDefinition() {return definition;}
	
	public void updateForceVariableCount() {
		forceVariableCount = 0;
		for (ObjectChildren f : components) {
			
			if(f instanceof ForceComponent) {
				
				forceVariableCount += ((ForceComponent) f).isVariable() ? 1 : 0;
			}
		}
		
		forceVariableCount_str = forceVariableCount + "";
	}
	@Override
	public boolean isInside(float x, float y) {
		
		if (x < X || x > width || y < Y|| y > height) return false;
		
		if(hitboxPolygon.contains(x, y)) return true;
		return false;
	}

	@Override
	public void unselect() {
		
		current = shape;
		UIStage.contextMenu.hide();
	//	xaxis.hide();
	//	yaxis.hide();
		
	}
	@Override
	public void select(int pointer) {
		
		current = mightSelected;
		UIStage.contextMenu.show();
	//	xaxis.show();
	//	yaxis.show();
	}
	
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
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
	final static Color shape = 		   new Color(0.2f, 0.8f, 0.2f, 0.6f);
	final static Color shapeSelected = new Color(0.8f, 0.2f, 0.2f, 0.6f);
	final static Color mightSelected = new Color(0.8f,0.5f,0.2f,0.6f);
	final static Color arcColor = new Color(0.5f, 0.5f, 0.9f, 0.4f);
	
	Color current = shape;
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!isEnded()) return;
		
		NPhysics.currentStage.shapefill.setColor(isLastSelected() ? shapeSelected : current);
		
		Util.renderPolygon(NPhysics.currentStage.shapefill, points, indexes);
		
		NPhysics.currentStage.shapefill.setColor(Color.GRAY);
		NPhysics.currentStage.shapefill.circle(polygonMassCenter.x, polygonMassCenter.y, 5);
		
		if(hookRotation && useAxis) {
			
			NPhysics.currentStage.shapefill.setColor(arcColor);
			NPhysics.currentStage.shapefill.arc(NPhysics.currentStage.getAxisPosition().getX(), NPhysics.currentStage.getAxisPosition().getY(), 100, 0, (line.getDiff().angleRad())*MathUtils.radDeg);
			
		}
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
	public boolean isEnded() {return end;}
	
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
		form.getOption("polygon_phys_mass").setValue(physMass);
		return physMass;
	}
	public float calculateDensity() {
		
		definition.density = physMass / getArea(true);
		form.getOption("polygon_phys_density").setValue(definition.density);
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
		definition.childrens = components;
		
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
	public void end() {
		
		for (Point point : points) {
			
			point.setObjectParent(this);
		}
		
		triangulate();
		end = true;
		
		if(temp == this) {
			
			NPhysics.sandbox.addActor(this);
			temp = null;
		}
		
		polygonlist.add(this);


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
	
	
	public void addComponent(ObjectChildren child) {
		
		components.add(child);
	}
	
	public void removeComponent(ObjectChildren child) {
		
		components.remove(child);
	}
	
	public ArrayList<ObjectChildren> getObjectChildrenList(){
		
		return components;
	}
	@Override
	public boolean remove() {
		
		polygonlist.remove(this);
		for (Point p : points) {
			p.setObjectParent(null);
			p.remove();
		}
		
		for (Segment s : segments) {
			s.remove();
		}
		return super.remove();
	}
	
	public Vector2 getPosition() {return new Vector2(getX(), getY());}

	@Override
	public DynamicWindow getForm() {
		return form;
	}

	@Override
	public void updateValuesFromForm() {
		
		float newDensity = form.getOption("polygon_phys_density").getValue();
		
		float newMass = form.getOption("polygon_phys_mass").getValue();
		
		if( definition.density != newDensity) {
			
			definition.density = newDensity;
			calculateMass();
		
		}else if(physMass != newMass) {
			
			physMass = newMass;
			calculateDensity();
		}
		definition.friction = form.getOption("polygon_phys_friction").getValue();
		definition.restitution = form.getOption("polygon_phys_restitution").getValue();
		
		switch ((int)form.getOption("polygon_phys_state").getValue()) {
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
		
		definition.linearVelocity.set(form.getOption("polygon_lvel_x").getValue(), 
									  form.getOption("polygon_lvel_y").getValue());
		
	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
