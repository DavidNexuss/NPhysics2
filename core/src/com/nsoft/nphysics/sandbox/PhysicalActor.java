package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.drawables.DiscontLine;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Related;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.CircleDefinition;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;
import com.nsoft.nphysics.simulation.dynamic.SolveJob;

/**
 * Classe encarregada de definir un cos abstracte que només consta d'una forma i un pes a la fase
 * Sandbox.
 * Necessita una classe Genèrica que defineixi quin ha de ser el procediment per crear la seva representació a la simulació
 * @see Definició Circular{@link CircleDefinition}
 * @see Definició poligon tancat {@link PolygonDefinition}
 * 
 * Hereda Group per poder passar la crida de dibuix i la de act als seus fills, ObjectChildrens. A part també hereda les funcions de renderització
 * Listeners i Animacions d'Actor
 * -Implementa {@link Form} per poder definir un formulari i modificar propietats dins l'aplicació.
 * -Implementa {@link Handler} per poder manejar objectes a ser seleccionats, Eg els seus ObjectChildrens
 * -Implementa {@link ClickIn} per poder ser manejat com a objecte candidat a ser seleccionat per el Handler Superior.
 * -Implementa {@link Parent} per poder rebre actualitzacions de transformacions dels punts associats a l'objecte, tot i que aquesta classe defineix
 * 	de forma abstracta la seva participació
 * -Implementa {@link Removeable} per poder ser seleccionat com a objecte candidat a ser destruit de la simulació ja que inclou un procediment per fer-ho
 * @author Usuari
 *
 * @param <D>
 */
public abstract class PhysicalActor<D extends ObjectDefinition> extends Group implements Form,Handler,ClickIn,Draggable,Parent<Point>,Removeable,Say{

	//Colors ja definits de forma estàtica per evitar ser instantiats dins del bucle de renderitzat i augmentar el rendiment.
	final static Color shape = 		   new Color(0.2f, 0.8f, 0.2f, 0.6f);
	final static Color shapeSelected = new Color(0.8f, 0.2f, 0.2f, 0.6f);
	final static Color mightSelected = new Color(0.8f,0.5f,0.2f,0.6f);
	final static Color arcColor = 	   new Color(0.5f, 0.5f, 0.9f, 0.4f);
	
	private static String grade = com.nsoft.nphysics.Options.names.grade;
	private DynamicWindow form; //Formulari
	private SelectHandle handler; //Objecte encarregat de manejar solicituds de selecció del handler superior.
	
	private boolean isEnded = false;
	private Color currentColor = shape;
	
	private Vector2 polygonMassCenter = new Vector2();
	private SimpleArrow gravityArrow;
	private DiscontLine line;
	
	private ArrowLabel angleLabel;
	
	boolean hookRotation;
	private float angle;
	private boolean useAxis;
	
	private ArrayList<ObjectChildren> components = new ArrayList<>(); //Llista de components associats a aquest objecte
	private ArrayList<Related> relateds = new ArrayList<>();
	
	D definition; //Definició del objecte per a la simulació
	
	ForceComponent unknown;
	
	private float physMass;
	
	protected ArrayList<Point> points = new ArrayList<>();
	
	public PhysicalActor() {
		
		handler = new SelectHandle();
		initDefinition();
		initForm();

		setDebug(true, true);
		addInput();
		addDragListener();
		
		line = new DiscontLine(new Vector2(), new Vector2());
		line.setVisible(false);
		addActor(line);
		
		angleLabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		angleLabel.setColor(Color.BLUE);
	}
	
	public void addRelated(Related r) {
		
		relateds.add(r);
		r.addRelation(this);
	}
	public void removeFromRelatedList(Related r) {
		
		relateds.remove(r);
	}
	public Vector2 getPosition() {return new Vector2(getX(), getY());}
	
	@Override
	public SelectHandle getSelectHandleInstance() {

		return handler;
	}
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	
	abstract void initDefinition();
	public boolean isEnded() {return isEnded;}
	public void end() {
		
		if(isEnded) throw new IllegalStateException(); //Necessari per controlar que aquesta funció només es pugui cridar un sol cop
		isEnded = true;
		for (Point point : points) {
			point.setObjectParent(this);
		}
		
		NPhysics.sandbox.polygonlist.add(this);
		
		polygonMassCenter.set(definition.getCenter(false));

		createArrow();
		
		definition.childrens = getComponents();
		
		calculateMass();
	}
	
	public abstract PhysicalActor<D> addPoint(Point p);
	
	public D getDefinition() {return definition;}
	
	private void initForm() {
		
		form = DynamicWindow.createDefaultWindowStructure(NDictionary.get("Wpolygon"),this);
		form.setSize(450, 450);
		
		form.addText(NDictionary.get("polygon_sim"));
		form.addOption(Options.createCheckBoxOption("polygon_isbullet"));
		form.addOption(Options.createOptionTypeSlider("polygon_phys_state", NDictionary.get("phys_DYNAMIC"),NDictionary.get("phys_KINEMATIC"),NDictionary.get("phys_STATIC")));
		form.addSeparator();
		
		form.addText(NDictionary.get("polygon_lvel"));
		form.addOption(Options.createOptionNumber("polygon_lvel_x"));
		form.addOption(Options.createOptionNumber("polygon_lvel_y"));
		form.addSeparator();
		
		form.addText(NDictionary.get("polygon_phys"));
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
				
				setText(""); 
				super.act(delta);
			}
		};
		VisTextButton dsl_b= new VisTextButton(NDictionary.get("dsl_solve"));
		PhysicalActor<?> pointer = this;
		dsl_b.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(unknown != null && unknown.isVariable()) {
					SolveJob j = new SolveJob(pointer, unknown);
					j.start();
				}
			}
		});
		
		solve_dsl.add(dsl_t).expand().align(Align.left);
		solve_dsl.add(dsl_n).prefWidth(50).width(50);
		solve_dsl.add(dsl_b).fillX().expand().padLeft(15);
		
		solve_dsl.pad(5);
		form.addRawTable(solve_dsl);
		
		form.setVisible(false);
		NPhysics.ui.addActor(form);
	}

	final Vector2 origin = new Vector2(0, 0);
	final Vector2 start = new Vector2(0, 0);
	
	//Defineix com a de ser tractat l'event d'arrastrament
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
				p.updatePosition();
				updatePosition(0, 0, null);
			}
			
		}
		
		start.set(NPhysics.currentStage.getUnproject(false));
		

		sumx = start.x - origin.x;
		sumy = start.y - origin.y;

	}
	Vector2 tempCenter = new Vector2();
	
	//Funció necessària per poder rotar el cos entorn a un eix
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
	public void hookMenu() {}
	//S'implementa per poder llançar el opcionari
	public boolean keyDown(int keycode){
		
		if(keycode == Keys.Q) {
			
			if(!form.isVisible()) {
				hookMenu();
				showForm();
			}
			return true;
		}
		
		return false;
	}

	@Override
	public void act(float delta) {
		
		super.act(delta);
		if(hookRotation) {
			
			rotateVertices(tempCenter, Math.round((line.getDiff().angleRad() - angle)* MathUtils.radDeg / 5)*5  * MathUtils.degRad);
			if(!isSelected()) hookRotation(false,useAxis);
		}
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);

		if(isEnded) {
			
			NPhysics.currentStage.shapefill.setColor(Color.GRAY);
			NPhysics.currentStage.shapefill.circle(polygonMassCenter.x, polygonMassCenter.y, 5);
		}
		
		if(hookRotation && useAxis) {
			
			angleLabel.setVisible(true);
			NPhysics.currentStage.shapefill.setColor(arcColor);
			float angle = (line.getDiff().angleRad())*MathUtils.radDeg;
			if(angle < 0) {
				angle = Math.abs(angle);
				angle = 360 - angle;
			}
			angleLabel.setText((int)(Math.round(angle / 5f)*5f) + grade);
			angleLabel.setPosition(new Vector2(NPhysics.currentStage.getAxisPosition().getVector()).add(new Vector2(120, 0).rotate(angle)));
			NPhysics.currentStage.shapefill.arc(NPhysics.currentStage.getAxisPosition().getX(), NPhysics.currentStage.getAxisPosition().getY(), 100, 0, angle);
		}else angleLabel.setVisible(false);
		
		NPhysics.currentStage.shapefill.setColor(isLastSelected() ? shapeSelected : currentColor);
		
	}
	
	public void rotateVertices(Vector2 pivot,float angleRad){
		
		for (Point p : points) {
			
			Vector2 pos = p.initial;
			Vector2 npos = Util.rotPivot(pivot, pos, angleRad);
			p.setPosition(npos.x, npos.y,false);
			p.updatePosition();
		}
		
		updatePosition();
	}
	
	@Override
	public void unselect() {
		
		currentColor = shape;
		UIStage.contextMenu.hide();

		
	}
	@Override
	public void select(int pointer) {
		
		currentColor = mightSelected;
		UIStage.contextMenu.show();

	}
	
	@Override
	public BaseOptionWindow getForm() {
		return form;
	}


	@Override
	public void updateValuesToForm() {
		
		setValue("polygon_phys_mass", getValue("polygon_phys_density") * getPhysicalArea());
	}
	

	abstract float getArea();
	public float getPhysicalArea() { return getArea() / (Util.METERS_UNIT()*Util.METERS_UNIT());}
	public float calculateMass() {
		
		physMass = definition.density * getPhysicalArea();
		setValue("polygon_phys_mass", physMass);
		return physMass;
	}
	private float calculateDensity() {
		
		definition.density = physMass / getPhysicalArea();
		setValue("polygon_phys_density", definition.density);
		return definition.density;
	}
	
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
		
		definition.isBullet = getValue("polygon_isbullet") == 1;
		
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
	public void addComponent(ObjectChildren child) {
		
		components.add(child);
	}
	
	public void removeComponent(ObjectChildren child) {
		
		components.remove(child);
	}
	
	public ArrayList<ObjectChildren> getComponents(){
		
		return components;
	}
	
	@Override
	public ArrayList<Point> getChildList() {
		return isEnded() ? points : null;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	
	@Override
	public boolean remove() {
		
		NPhysics.sandbox.polygonlist.remove(this);
		for (Point p : points) {
			
			ArrayList<PolygonActor> list = p.getObjectParents(PolygonActor.class);
			
			boolean canRemove = true;
			
			for (PolygonActor polygonActor : list) {
				if(polygonActor != this) {
					canRemove = false;
					break;
				}
			}
			if(!canRemove) continue;
			p.remove();
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Related> rls = (ArrayList<Related>) relateds.clone();
		
		for (Related related : rls) {
			
			related.destroy();
		}
		return super.remove();
	}
	
	private void createArrow() {
		
		if(gravityArrow == null) {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow = new SimpleArrow(start, new Vector2(start).add(0,-Math.abs(getPhysicalArea() * Util.GRAVITY_UNIT())));
			addActor(gravityArrow);
			gravityArrow.setColor(Color.BLUE);
		}else {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow.setStart(start);
			gravityArrow.setEnd(new Vector2(start).add(0,-Math.abs(getPhysicalArea() * Util.GRAVITY_UNIT())));
			gravityArrow.updateVertexArray();
		}
	}
	
	public boolean isStatic() {
		
		return definition.type == BodyType.StaticBody;
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(p != null) angle = 0 ;
		createArrow();
		polygonMassCenter.set(definition.getCenter(false));
		
		calculateMass();
		updateValuesToForm();
	}
}
