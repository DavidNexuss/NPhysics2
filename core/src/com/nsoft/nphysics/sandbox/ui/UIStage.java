package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.layout.DragPane;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.UILoader;
import com.nsoft.nphysics.sandbox.GState;
import com.nsoft.nphysics.sandbox.Sandbox;

public class UIStage extends Stage{

	static OptionPane options;
	public static OptionPane contextMenu;
	static ViewSelection view;
	static ShapeRenderer shapefill;
	static Table container;
	
	public UIStage() {
		
		super(new ScreenViewport());
		shapefill = new ShapeRenderer();
		
		setStateMenu();
		loadViewMenu();
	}
	
	private void loadViewMenu() {
		
		container = new Table(UILoader.skin);
		
		container.setPosition(40, Gdx.graphics.getHeight()- 30);
		container.setWidth(Gdx.graphics.getWidth() - 40);
		container.setHeight(30);

		view = new ViewSelection();
		view.add(new ViewTab("Studio"));
		view.add(new ViewTab("Prefab Studio"));
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
		loadSubMenu();
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
		contextMenu.color = new Color(0.1f, 0.1f, 0.1f, 1f);
		
		setContextMenuItems();
		contextMenu.pack();
		addActor(contextMenu);
		contextMenu.hide();
	}
	
	private void setContextMenuItems() {
		
		contextMenu.add(MenuItem.loadNewItem("axis.png", GState.CREATE_AXIS));
		contextMenu.add(MenuItem.loadNewItem("support.png", GState.CREATE_SUPPORT));
	}
	private void setOptionMenuItems() {
		
		options.add(MenuItem.loadNewItem("start.png", GState.START));
		options.add(MenuItem.loadNewItem("point.png", GState.CREATE_POINT));
		options.add(MenuItem.loadNewItem("segment.png", GState.CREATE_SEGMENT));
		options.add(MenuItem.loadNewItem("shape.png", GState.CREATE_POLYGON));
		options.add(MenuItem.loadNewItem("shape.png", GState.CREATE_POLYGON));
	}
	
	private void loadSubMenu() {
		
		MenuItem grid = MenuItem.loadNewItem("grid.png", ()->{Sandbox.snapping = !Sandbox.snapping;});
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		addActor(grid);
	}
	
	@Override
	public void draw() {
		
		shapefill.begin(ShapeType.Filled);
		shapefill.setColor(0.1f, 0.1f, 0.1f, 1f);
		shapefill.rect(container.getX(), container.getY(), container.getWidth(), container.getHeight());
		shapefill.end();
		super.draw();
	}
	
	
	
}
