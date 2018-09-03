package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.layout.DragPane;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.ThreadManager.Task;
import com.nsoft.nphysics.UILoader;
import com.nsoft.nphysics.sandbox.GState;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionColorPicker;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;

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
	static Table container;
	static MenuItem grid;
	
	static Actor backgroundAnimation;
	
	public static StaticMenu menu;
	
	public static UIStage stage;
	public UIStage() {
		
		super(new ScreenViewport());
		
		stage = this;
		initBackGroundShader();
		shapefill = new ShapeRenderer();
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
		
		back.add(operation).fill().expand();
		
		addActor(back);
		

		loadSubMenu();
		
		backgroundAnimation = new Actor();
		addActor(backgroundAnimation);
		backgroundAnimation.setColor(0,0,0,0);
		
		initstaticMenu();
		
	}
	
	private void initstaticMenu() {
		
		menu = new StaticMenu();
		
		FixedWindow test = new FixedWindow("TEST", null);
		test.setPosition(20, 40);
		test.addOption(new Option("Test", new UIOptionColorPicker()));
		
		menu.addWindow(test);
		addActor(test);
	}
	public static void initBackGroundShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader").readString();
	     String fragmentShader = Gdx.files.internal("shaders/backShader").readString();
	     
	     backSP = new ShaderProgram(vertexShader, fragmentShader);
	     backSP.pedantic = false;
	     System.out.println("Shader compiler log: " + backSP.getLog());
	     
	     Pixmap p = new  Pixmap(1, 1, Format.RGB565);
	     nullTexture = new Texture(p);
	     backBatch = new SpriteBatch();
	     backBatch.setShader(backSP);
	}

	public static void setOperationText(String op) {
		
		operation.setText(op);
	}
	private void loadViewMenu() {
		
		container = new Table(UILoader.skin);
		
		container.setPosition(40, Gdx.graphics.getHeight()- 30);
		container.setWidth(Gdx.graphics.getWidth() - 40);
		container.setHeight(30);

		view = new ViewSelection();
		view.add(new ViewTab("Studio"));
		view.add(new ViewTab("Simulation",()->{
			
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
	
	public void setStateMenu() {
		
		loadOptionMenu();
		loadContextMenu();
		loadDoubleContextMenu();
	}
	
	private void loadOptionMenu() {
		
		options = new OptionPane();

		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);

		setOptionMenuItems();
		options.pack();
		addActor(options);	
	}
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
	private void setContextMenuItems() {
		
		contextMenu.add(MenuItem.loadNewItem("axis.png", GState.CREATE_AXIS));
		contextMenu.add(MenuItem.loadNewItem("rollaxis.png", GState.CREATE_PRISMATIC));
		contextMenu.add(MenuItem.loadNewItem("support.png", GState.CREATE_SUPPORT));
		contextMenu.add(MenuItem.loadNewItem("force.png", GState.CREATE_FORCE));
	}
	private void setOptionMenuItems() {
		
		options.add(MenuItem.loadNewItem("start.png", GState.START));
		options.add(MenuItem.loadNewItem("point.png", GState.CREATE_POINT));
		options.add(MenuItem.loadNewItem("segment.png", GState.CREATE_SEGMENT));
		options.add(MenuItem.loadNewItem("shape.png", GState.CREATE_POLYGON));
		options.add(MenuItem.loadNewItem("shape.png", GState.CREATE_FAST_POLYGON));
	}
	
	private void setDoubleContextMenuItems() {
		
		doubleContextMenu.add(MenuItem.loadNewItem("axis.png", GState.CREATE_DOUBLE_AXIS));
		doubleContextMenu.add(MenuItem.loadNewItem("segment.png", GState.CREATE_ROPE));
	}
	private void loadSubMenu() {
		
		grid = MenuItem.loadNewItem("grid.png", ()->{NPhysics.currentStage.switchSnapping();});
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		addActor(grid);
	}
	
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
		super.draw();
	}
	
	
	private void updateMatrix() {
		
		shapefill.setProjectionMatrix(getCamera().combined);
		getBatch().setProjectionMatrix(getCamera().combined);
	}
	
	public void updateUILayout() {
		
		getViewport().setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getViewport().setWorldSize(0,  0);
		getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getCamera().position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		getCamera().update();
		updateMatrix();
		container.setPosition(40, Gdx.graphics.getHeight()- 30);
		container.setWidth(Gdx.graphics.getWidth() - 40);
		container.setHeight(30);
		
		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);
		
		contextMenu.setPosition(40, 0);
		contextMenu.setHeight(Gdx.graphics.getHeight() - 30);
		contextMenu.setWidth(40);
		contextMenu.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
		
		back.setPosition(0, 0);
		back.setSize(Gdx.graphics.getWidth(), 40);
		
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		
	}
}
