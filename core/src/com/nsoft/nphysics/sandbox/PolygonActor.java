/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;

import earcut4j.Earcut;

/**
 * Classe que defineix un cos poligonal, hereda de {@link PhysicalActor} i defineix com a de ser la construcci�
 * d'un cos on l'usuari defineix els vertexs
 * @author Usuari
 *
 */
public class PolygonActor extends PhysicalActor<PolygonDefinition>{

	private Point initial;
	private ArrayList<Segment> segments = new ArrayList<>();
	private List<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private float X,Y,width,height; //BOUNDS
	
	private Polygon hitboxPolygon; 
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		
	}

	public void initDefinition() {
		
		definition = new PolygonDefinition();
	}
	/**
	 * Crea una copia del cos a una certa posici� relativa
	 * @param offset Vector que indica la separaci� entre l'actual cos i la copia
	 * @return la copia
	 */
	
	/*
	 * Un pla per el futur seria generalitzar aquesta funci� incloent-la en la classe superior,
	 * aix� es podria crea copies de cossos especials com CircleActor
	 */
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
		/*forceVariableCount = 0;
		for (ObjectChildren f : getComponents()) {
			
			if(f instanceof ForceComponent) {
				
				forceVariableCount += ((ForceComponent) f).isVariable() ? 1 : 0;
			}
		}*/
	}
	@Override
	public boolean isInside(float x, float y) {
		
		if (x < X || x > width || y < Y|| y > height) return false;
		if(hitboxPolygon.contains(x, y)) return true;
		return false;
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		if(!isEnded()) return;
		
		/*S'utilitza renderitzaci� immediata per renderitzar el poligon en si, realment es podria
		utilitzar un VBO per� l'impacte en rendiment del sistema actual es sostenible*/
		
		Util.renderPolygon(NPhysics.currentStage.shapefill, points, indexes);
		
		
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
	
	/**
	 * Crea un buffer on emmagatzarem les posicions dels vertex en una array unidimensional
	 * aquesta funci� s'ha d'executar cada cop que es modifica el cos.
	 */
	private void createBuffer() {
		
		buffer = new double[points.size()*2];
		
		for (int i = 0; i < points.size()*2; i+=2) {
				
			buffer[i] = points.get(i/2).getX();
			buffer[i + 1] = points.get(i/2).getY();
		}
	}
	
	/**
	 * Triangulitza el poligon, necessari per poder crea les fixtures d'aquest cos a la simulaci�
	 * ja que Box2D no permet l'utilitzaci� de poligons concaus com a fixtura
	 */
	private void triangulate() {
		
		createBuffer();
		
		indexes = Earcut.earcut(buffer);
	}
	
	private void calculateBounds() {
		
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
		calculateBounds();
		hitboxPolygon = new Polygon(definition.getRawVertices());
	}
	private void calculateHitBox() {
		
		calculateBounds();
		hitboxPolygon.setVertices(definition.getRawVertices());
		
		calculateMass();
		
	}
	
	private void createDefinition() {
		
		definition.vertices.clear();
		definition.indexes.clear();
		
		definition.indexes = indexes;
		for (Point p : points) {
			
			definition.vertices.add(new PositionVector(p.getX(), p.getY()));
		}
		
		definition.init();
		
	}
	
	@Override
	float getArea() {
		return Math.abs(hitboxPolygon.area());
	}
	@Override
	public void end() {
		

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
		
		super.end();
	}

	
	
	@Override
	public void act(float delta) {

		super.act(delta);
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		super.updatePosition(x, y, p);
		if(!isEnded())return;
		triangulate();

		createDefinition();
		calculateHitBox();
	}
	
	@Override
	public boolean remove() {
		
		for (Segment s : segments) {
			s.remove();
		}
		return super.remove();
	}
}
