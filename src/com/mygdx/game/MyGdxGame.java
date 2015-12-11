package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	int screenWidth;
	int screenHeight;
    
	// 1 = block
	// 0 = empty
	// the x and y coordinate system is not what it seems
	// visually x goes down and y across
	// this will make more sense when you compare it to what is drawn
	int[][] map = {//first problem- randomly place objects
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,1,1,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,1,1,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,1,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, 
	};
	int mapWidth = 15;
	int mapHeight = 15;
	int tileSize = 20;
	Texture tileTexture;
	
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	enum Axis { X, Y };
	enum Direction { U, D, L, R };

  @Override
  public void create () {
	  batch = new SpriteBatch();
	  tileTexture = new Texture("block.png");  
	  screenWidth = Gdx.graphics.getWidth();
	  screenHeight = Gdx.graphics.getHeight();
	 	  
	  // add some entities including a splayer
	  entities.add(new Player(this, 100, 50, 50, 50, 120.0f, new Texture("h.png")));
	  entities.add(new Entity(this, 180, 50, 50, 50, 120.0f, new Texture("r.png")));
	  entities.add(new Entity(this, 50, 150, 50, 50, 120.0f, new Texture("r.png")));
	  entities.add(new Entity(this, 200, 200, 50, 50, 120.0f, new Texture("r.png")));
	 
  }
  
  public void moveEntity(Entity e, float newX, float newY) {
	  // just check x collisions keep y the same
	  moveEntityInAxis(e, Axis.X, newX, e.y);
	  // just check y collisions keep x the same
	  moveEntityInAxis(e, Axis.Y, e.x, newY);
  }
  
  public void moveEntityInAxis(Entity e, Axis axis, float newX, float newY) {
	  Direction direction;
	  
	  // determine axis direction
	  if(axis == Axis.Y) {
		  if(newY - e.y < 0) direction = Direction.U;
		  else direction = Direction.D;
	  }
	  else {
		  if(newX - e.x < 0) direction = Direction.L;
		  else direction = Direction.R;
	  }

	  if(!tileCollision(e, direction, newX, newY) && !entityCollision(e, direction, newX, newY)) {
		  // full move with no collision
		  e.move(newX, newY);
	  }
	  // else collision with wither tile or entity occurred 
  }
  
  public boolean tileCollision(Entity e, Direction direction, float newX, float newY) {
	  boolean collision = false;
	//  
	
	  // determine affected tiles
	  int x1 = (int) Math.floor(Math.min(e.x, newX) / tileSize);
	  int y1 = (int) Math.floor(Math.min(e.y, newY) / tileSize);
	  int x2 = (int) Math.floor((Math.max(e.x, newX) + e.width - 0.1f) / tileSize);
	  int y2 = (int) Math.floor((Math.max(e.y, newY) + e.height - 0.1f) / tileSize);
/*	  
	  // todo: add boundary checks...DONE
	//  for(int i = 0;i<play)
for( int el=x1;el<x2;el++){//check the horizontal boarders
	if( map[el][y1]==1){//check for a collition at the top and bottom
		  collision = true;	        
		  e.tileCollision(map[el][y1], el, y1, newX, newY, direction);
		 // return collision;
	}
		  else if( map[el][y2]==1){
			  collision = true;	        
			  e.tileCollision(map[el][y2], el, y2, newX, newY, direction);  
			//  return collision;
		  }
	}
if (collision == true) return collision;
for(int al = y1;al<=y2;al++){
	if(map[x1][al]==1){
		collision = true;
		
		e.tileCollision(map[x1][al], x1, al, newX, newY, direction);
		return collision;
	}
	if(map[x2][al]==1){
		collision = true;
		e.tileCollision(map[x2][al], x2, al, newX, newY, direction);
		return collision;//the game is over if we detect a colission somewhere
	}
}
if (collision == true) return collision;//a collision has already been detected do not move on
*/	
// tile checks
	  for(int x = x1; x < x2; x++) {
		  for(int y = y1; y <y2; y++) {
			  if(map[x][y] == 1) {
				  	
				  Entity b= new Entity(this, x, y, tileSize, tileSize, 1.0f, new Texture("block.png"));
			collision=	 e.pixelCollision(b, newX, newY, direction);
			System.out.println("You hit the wall");
			return true;
				 // e.tileCollision(map[x][y], x, y, newX, newY, direction);
			  }
		  }
	  }
	  
	  return collision;
  }
  
  public boolean entityCollision(Entity e1, Direction direction, float newX, float newY) {
	  boolean collision = false;
	  
	  for(int i = 0; i < entities.size(); i++) {
		  Entity e2 = entities.get(i);
		  
		  // we don't want to check for collisions between the same entity
		  if(e1 != e2) {
			  // axis aligned rectangle rectangle collision detection
			  if(newX < e2.x + e2.width && e2.x < newX + e1.width &&
				  newY < e2.y + e2.height && e2.y < newY + e1.height) {
				  collision = true;
				  
				  e1.entityCollision(e2, newX, newY, direction);
			  }
		  }
	  }
	  
	  return collision;
  }

  @Override
  public void render () {
	  
	  // update
	  // ---
	  
	  
	  float delta = Gdx.graphics.getDeltaTime();
	  
	  // update all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  // update entity based on input/ai/physics etc
		  // this is where we determine the change in position
		  e.update(delta);
		  // now we try move the entity on the map and check for collisions
		  moveEntity(e, e.x + e.dx, e.y + e.dy);
	  }	  
	  
	  
	  // draw
	  // ---

	  
	  // to offset where your map and entities are drawn change the viewport
	  // see libgdx documentation
	  
	  Gdx.gl.glClearColor(0, 0, 0, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  batch.begin();
    
	  // draw tile map
	  // go over each row bottom to top
	  for(int y = 0; y < mapHeight; y++) {
		  // go over each column left to right		
		  for(int x = 0; x < mapWidth; x++) {
			  // tile
			  if(map[x][y] == 1) {
				  batch.draw(tileTexture, x * tileSize, y * tileSize);
			  }
			  // draw other types here...
		  }
	  }
    
	  // draw all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  batch.draw(e.texture, e.x, e.y);
	  }
	  
	  batch.end();
  }
}
