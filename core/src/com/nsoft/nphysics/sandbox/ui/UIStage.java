package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.layout.DragPane;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NDictionary.Languages;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Options;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.ThreadManager.Task;
import com.nsoft.nphysics.UILoader;
import com.nsoft.nphysics.sandbox.GState;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionSlider;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;
import com.nsoft.nphysics.simulation.dynamic.SolveJob;
/**
 * Classe encarregada de renderitzar i manejar tots els elements visuals
 * que formen part de l'intefície d'usuari. A part també es el punt central
 * del paquet destinat a la interfície d'usuari i node principal del que
 * tots els elements visuals no controlats per la camara estan afegits.
 * 
 * Gran part del codi d'aquesta classe es basa en construi tota la interfície
 * @author David
 */
public class UIStage extends Stage{

	static ShaderProgram backSP;
	static Texture nullTexture;
	static SpriteBatch backBatch;
	
	static OptionPane options;
	static VisLabel operation;
	static Table back;
	public static OptionPane contextMenu;
	public static OptionPane doubleContextMenu;
	public static ViewSelection view;
	static ShapeRenderer shapefill;
	static ShapeRenderer shapeline;
	static Table container;
	static MenuItem grid,clean;
	
	static Actor backgroundAnimation;
	
	public static StaticMenu menu;
	
	public static VisLabel fps;
	public static Scale s;
	
	public static UIStage stage;
	static Group labelGroup;
	
	public UIStage() {
		
		super(new ScreenViewport());
		labelGroup = new Group();
		addActor(labelGroup);
		stage = this;
		initBackGroundShader();
		shapefill = new ShapeRenderer();
		shapeline = new ShapeRenderer();
		FontManager.init();
		setStateMenu();
		loadViewMenu();
		
		back = new Table();
		back.setPosition(0, 0);
		back.setSize(Gdx.graphics.getWidth(), 40);
		back.pad(10);
		back.setColor(new Color(1, 1, 1, 0.7f));
		back.setBackground(OptionPane.generateBackground(back));
		back.pack();
		
		operation = new VisLabel();
		operation.setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		operation.setColor(Color.BLACK);
		
	//	back.add(operation).fill().expand();
		
		addActor(back);
		

		loadSubMenu();
		
		backgroundAnimation = new Actor();
		addActor(backgroundAnimation);
		backgroundAnimation.setColor(0,0,0,0);
		
		initstaticMenu();
		
		//Contador de fotogrames per segon per motius de debugueig
		fps = new VisLabel() {
			
			@Override
			public void act(float delta) {
				if(fps.isVisible()) {
					fps.setText(Gdx.graphics.getFramesPerSecond() + "FPS");
					super.act(delta);
				}
			}
		};
		fps.setStyle(new LabelStyle(new BitmapFont(), Color.RED));
		addActor(fps);
		fps.setPosition(100, Gdx.graphics.getHeight() - 50);
		fps.setVisible(false);

		s = new Scale();
		s.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 60);
		addActor(s);
		labelGroup.setZIndex(10);

		//	setDebugAll(true); //Funció per debugueig

	}
	
	public void showFPS(boolean v) {
		
		fps.setVisible(v);
	}
	/**
	 * Inicialitza el menu static o global
	 */
	
	FixedWindow sim;
	FixedWindow lang;
	private void initstaticMenu() {
		
		menu = new StaticMenu();
		WorldOptionManager handler = new WorldOptionManager();
		
		sim = new FixedWindow(NDictionary.get("simoptions"), handler);
		sim.setPosition(150, Gdx.graphics.getHeight());	
		sim.setPosition(sim.getX(), sim.getY() - sim.getPrefHeight() - 220);
		
		sim.addOption(new Option("gridmeterscale", new UIOptionNumber()).setValue(Util.METER_FACTOR));
		sim.addOption(new Option("gridscale", new UIOptionNumber()).setValue(Util.getUnit() + .0f));
		sim.addOption(new Option("gridnewtonscale", new UIOptionNumber()).setValue(Util.NEWTON_FACTOR));
		sim.addOption(new Option("gravity", new UIOptionNumber()).setValue(SimulationStage.gravity.y));
		sim.addText(NDictionary.get("solvejob"));
		sim.addOption(new Option("worldwait", new UIOptionNumber()).setValue(SolveJob.waitTime));
		sim.addOption(new Option("epsilon_exp", new UIOptionNumber()).setValue(SolveJob.exp));
		
		lang = new FixedWindow(NDictionary.get("langoptions"), handler);
		lang.setPosition(700, Gdx.graphics.getHeight());
		lang.setPosition(lang.getX(), lang.getY() - lang.getPrefHeight() - 100);
		
		UIOptionSlider sli = new UIOptionSlider(Options.names.esp,Options.names.cat,Options.names.eng);
		float v = 0;
		if(Options.options.currentLanguage == Languages.ESP) v = 0;
   else if(Options.options.currentLanguage == Languages.CAT) v = 1;
   else if(Options.options.currentLanguage == Languages.ENG) v = 2;
		
		lang.addOption(new Option("chooselang", sli));
		sli.setValue(v);
		menu.addWindow(sim);
		menu.addWindow(lang);
		addActor(sim);
		addActor(lang);
	}
	/**
	 * Carrega el shader per renderitzar el gradient del fons del menú
	 */
	public static void initBackGroundShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader").readString();
	     String fragmentShader = Gdx.files.internal("shaders/backShader").readString();
	     
	     backSP = new ShaderProgram(vertexShader, fragmentShader);
	     ShaderProgram.pedantic = false;
	    
	     System.out.println("Shader compiler log: " + backSP.getLog());
	     
	     Pixmap p = new  Pixmap(1, 1, Format.RGB565);
	     nullTexture = new Texture(p);
	     backBatch = new SpriteBatch();
	     backBatch.setShader(backSP);
	}

	public static void setOperationText(String op) {
		
		operation.setText(op);
	}
	/**
	 * Carrega el menu lliscant
	 */
	private void loadViewMenu() {
		
		container = new Table(UILoader.skin);
		
		container.setPosition(40, Gdx.graphics.getHeight()- 30);
		container.setWidth(Gdx.graphics.getWidth() - 40);
		container.setHeight(30);

		view = new ViewSelection();
		view.add(new ViewTab(NDictionary.get("studio"),()->{
			
			NPhysics.switchToSandbox();
		}));
		view.add(new ViewTab(NDictionary.get("simulation"),()->{
			
			NPhysics.switchToSimulation();
		}));
		
		view.switchTab(0);
		
		DragPane p = view.getTabsPane();
		p.pack();
		container.add(p).expand().fill();

		view.initInput();
		addActor(container);
		

		container.setDebug(false);
	}	
	
	/**
	 * Carrega els 3 menus, el de les opcions el de tria la fase
	 * i el de desactivar el snap
	 */
	public void setStateMenu() {
		
		loadOptionMenu();
		loadContextMenu();
		loadDoubleContextMenu();
	}
	/**
	 * Carrega el menú d'eines
	 */
	private void loadOptionMenu() {
		
		options = new OptionPane();

		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);

		setOptionMenuItems();
		options.pack();
		addActor(options);	
	}
	/**
	 * Carrega el submenu d'eines 
	 */
	private void loadContextMenu() {
		
		contextMenu = new OptionPane();

		contextMenu.setPosition(40, 0);
		contextMenu.setHeight(Gdx.graphics.getHeight() - 30);
		contextMenu.setWidth(40);
		contextMenu.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
		
		setContextMenuItems();
		contextMenu.pack();
		addActor(contextMenu);
		contextMenu.setVisible(false);
	}
	
	/**
	 * Carrega el segon submenu d'eines
	 */
	private void loadDoubleContextMenu() {
		
		doubleContextMenu = new OptionPane();

		doubleContextMenu.setPosition(80, 0);
		doubleContextMenu.setHeight(Gdx.graphics.getHeight() - 30);
		doubleContextMenu.setWidth(40);
		doubleContextMenu.setColor(Color.BLACK);
		
		setDoubleContextMenuItems();
		doubleContextMenu.pack();
		addActor(doubleContextMenu);
		doubleContextMenu.setVisible(false);
	}
	
	/**
	 * Afegeix els items del menú d'eines
	 */
	private void setOptionMenuItems() {
		
		options.add(MenuItem.loadNewItem("start.png", GState.START));
		options.add(MenuItem.loadNewItem("point.png", GState.CREATE_POINT));
		options.add(MenuItem.loadNewItem("segment.png", GState.CREATE_SEGMENT));
		options.add(MenuItem.loadNewItem("shape.png", GState.CREATE_FAST_POLYGON));
		options.add(MenuItem.loadNewItem("circle.png", GState.CREATE_CIRCLE));
		options.add(MenuItem.loadNewItem("water.png", GState.CREATE_WATER));
	}

	/**
	 * Afegeix els items del submenu
	 */
	private void setContextMenuItems() {
		
		contextMenu.add(MenuItem.loadNewItem("axis.png", GState.CREATE_AXIS));
		contextMenu.add(MenuItem.loadNewItem("rollaxis.png", GState.CREATE_PRISMATIC));
		contextMenu.add(MenuItem.loadNewItem("force.png", GState.CREATE_FORCE));
	}
	
	/**
	 * Afegeix els items del segon submenú d'eines
	 */
	private void setDoubleContextMenuItems() {
		
		doubleContextMenu.add(MenuItem.loadNewItem("axis.png", GState.CREATE_DOUBLE_AXIS));
		doubleContextMenu.add(MenuItem.loadNewItem("segment.png", GState.CREATE_ROPE));
		doubleContextMenu.add(MenuItem.loadNewItem("spring.png", GState.CREATE_SPRING));
		doubleContextMenu.add(MenuItem.loadNewItem("pulley.png", GState.CREATE_PULLEY));
	}
	private void loadSubMenu() {
		
		grid = MenuItem.loadNewItem("grid.png", ()->{NPhysics.currentStage.switchSnapping();});
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		addActor(grid);
		
		clean = MenuItem.loadNewItem("new.png", ()->{NPhysics.cleanWorld();});
		clean.setPosition(Gdx.graphics.getWidth() - 80, 8);
		addActor(clean);
	}
	
	
	/************************************************
	 * Funcions per mostrar i ocultar el menú global
	 **********************************************/
	private Task t = Task.createEmpty();
	
	public boolean canSwitch() {return t.isComplete();}
	public void hideStaticMenu() {
		
		if(t.isComplete()) {
			backgroundAnimation.addAction(Actions.fadeOut(0.8f, Interpolation.exp5));
			menu.hide();
			t = ThreadManager.createTimer(1f);
		}
	}
	public void showStaticMenu() {
		
		if(t.isComplete()) {
			
			backgroundAnimation.addAction(Actions.fadeIn(0.8f, Interpolation.exp5));
			menu.show();
			t = ThreadManager.createTimer(1f);
		}
	}
	
	@Override
	public void draw() {
		
		shapefill.begin(ShapeType.Filled);
		shapefill.setColor(0.1f, 0.1f, 0.1f, 1f);
		shapefill.rect(container.getX(), container.getY(), container.getWidth(), container.getHeight());
		shapefill.end();

		backBatch.begin();
		backSP.setUniformf("A",backgroundAnimation.getColor().a / 2f);
		backBatch.draw(nullTexture, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		backBatch.end();
		
		shapeline.begin(ShapeType.Line);
		super.draw();
		shapeline.end();
		
	}
	
	/**
	 * Actualitza la matriu dels diferents shaders utilitzats
	 */
	private void updateMatrix() {
		
		shapefill.setProjectionMatrix(getCamera().combined);
		shapeline.setProjectionMatrix(getCamera().combined);
		getBatch().setProjectionMatrix(getCamera().combined);
	}
	
	/**
	 * Actualitza tot l'esquema per una nova resolució de pantalla
	 */
	public void updateUILayout() {
		
		getViewport().setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getViewport().setWorldSize(0,  0);
		getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getCamera().position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		getCamera().update();
		updateMatrix();
		container.setPosition(40, Gdx.graphics.getHeight()- 30);
		view.getTabsPane().setWidth(Gdx.graphics.getWidth());
		container.setWidth(Gdx.graphics.getWidth());
		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);
		
		contextMenu.setPosition(40, 0);
		contextMenu.setHeight(Gdx.graphics.getHeight() - 30);
		contextMenu.setWidth(40);
		contextMenu.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
		
		doubleContextMenu.setPosition(80, 0);
		doubleContextMenu.setHeight(Gdx.graphics.getHeight() - 30);
		doubleContextMenu.setWidth(40);
		doubleContextMenu.setColor(Color.BLACK);
		
		back.setPosition(0, 0);
		back.setSize(Gdx.graphics.getWidth(), 40);
		
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		clean.setPosition(Gdx.graphics.getWidth() - 80, 8);
		
		fps.setPosition(100, Gdx.graphics.getHeight() - 50);
		

		s.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 60);
		
		sim.setPosition(150, Gdx.graphics.getHeight());	
		sim.setPosition(sim.getX(), sim.getY() - sim.getPrefHeight() - 100);
		
		lang.setPosition(700, Gdx.graphics.getHeight());
		lang.setPosition(lang.getX(), lang.getY() - lang.getPrefHeight() - 100);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(super.keyDown(keyCode)) return true;
		
		if(keyCode == Keys.F) {
			showFPS(!fps.isVisible()); //Controla el marcador FPS
			return true;
		}
		
		return false;
	}
}
