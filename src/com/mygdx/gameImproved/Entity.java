package com.mygdx.gameImproved;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameImproved.MyGdxGame.Direction;

import sun.org.mozilla.javascript.internal.ast.Block;
//import com.sun.corba.se.impl.ior.ByteBuffer;
/*This is the improved version of entity
 * To reduce checks I have used a line check method that checks the vertical center of the 
 * overlapping area between two entities
 * I have also used a Rectangle to represent the image, as a bounding shape to dertemine if actual textures collide
 * Because when small images are used the bitmap just stores zeros for the 
 * We would have to reset the entity width each time we change characters. This makes the code less brute force
 * */
public class Entity {
	public MyGdxGame game;
	public float x;
	public float y;
	public float dx;
	public float dy;
	public int width;
	public int height;  
	public float speed;
	public Texture texture;
	Pixmap entitymap;
	ByteBuffer data ;
	int[][] bitmap;
	public Rectangle rect ;
	//public Rectangle bound;//rather
	public Vector2 position;
	public Entity(MyGdxGame game, float x, float y, int width, int height, float speed, Texture texture) {
		this.game = game;
		this.x = x;
		this.y = y;
		position = new Vector2(x,y);
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.texture = texture;
		rect = new Rectangle();//instead of having a bitmap too large. we trim
		rect.setPosition(x, y);//it down to rectangular size
		rect.setSize(texture.getWidth(), texture.getHeight());
		//we suse this rect to determine wether the entities collide where the image is
		if(!this.texture.getTextureData().isPrepared()){
			texture.getTextureData().prepare();
		}
	 entitymap = texture.getTextureData().consumePixmap();
	 entitymap.setBlending(Blending.None);//this removes the stress of stretching images
	//	bound = new Rectangle();
	//	bound.set(x, y, width, height);
bitmap = this.entityBitMap();
	}
	public int[][] entityBitMap(){//create the 
	Color col = 	new Color();
	bitmap = new int[height][width];
		for(int y = 0;y<height;y++){
			for(int x=0;x<width;x++){
					int value = entitymap.getPixel(x,y);
					Color.rgba8888ToColor(col, value);
					if(col.a>=0.5)
						bitmap[x][y]=(int)Math.ceil(col.a);
					else
					bitmap[x][y]=(int)Math.floor(col.a);
				//System.out.print(bitmap[x][y]+ " ");
			}
			//System.out.println();
		}
		//System.out.println("\sn");
		//System.out.println("\nThe end of this image");
		return bitmap;
	}
public Pixmap getBound(){
	return entitymap;
}
	public void update(float delta) {

	}
	
	public void move(float newX, float newY) {
		x = newX;
		y = newY;
	//	position = new Vector2(newX,newY);//move to this postion
		// bound.setPosition(newX, newY);//seeting the size of the
		 //box, am not so sure about this
		rect.setPosition(newX, newY);
	}
	
	public void render() {
		
	}

	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		System.out.println("tile collision at: " + tileX + " " + tileY);
		//check to see if there is a collision
		//check this entity against a tile
		
		if(direction == Direction.U) {
	//		bound.y = tileY * game.tileSize + game.tileSize;
			y = tileY * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.D) {
	//		bound.y = tileY * game.tileSize - height;//move down
			y = tileY * game.tileSize - height;
		}
		else if(direction == Direction.L) {
	//		bound.x = tileX * game.tileSize + game.tileSize;
			x = tileX * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.R) {//this was D, dunno why
		//	bound.x = tileX * game.tileSize - width;
			x = tileX * game.tileSize - width;
		}		
	}

	public void entityCollision(Entity e2, float newX, float newY, Direction direction) {
	//	System.out.println("entity collision around: " + newX + " " + newY);
		//Entity e2 is the enemy
		move(newX, newY);//move the player
		this.pixelCollision(e2, newX, newY, direction);
		}
	public boolean pixelCollision(Entity e2, float newX, float newY, Direction direction){
		boolean collide = false;
		//create a new block that is the overlap of block one and two
		//check if the two rectangles collide
		//if(direction==Direction.R){//this is on the left
		//if(this.rect.x+rect.width> e2.rect.x+e2.rect.width)
	//	}
		int xmin = (int)Math.max(this.x, e2.x);
		int ymin = (int)Math.max(this.y,e2.y);
		int ymax = (int)Math.min(this.y+height,(e2.y+e2.height));
		int xmax = (int)Math.min(this.x+width,(e2.x+e2.width));
		//possible collision box
		//1 improvement
		//check if there is a overlap
		if(xmax<xmin||ymax<ymin){
			//System.out.println("No collision at this point");
			return false ;//the textures are not overlapping.
		}//draw a line in the middle of the overlapping area and check it first for collisions
		for(int midy = ymin;midy<ymax;midy++){
			int midx = (xmin+xmax)/2;
			int midx2 = (xmin+xmax)/2;
			//System.out.println();
			if(midx-(int)this.x==50)midx--;
			if(midx+1-(int)this.x==-1)midx++;
			if(midx2-(int)e2.x==50)midx2--;
			if(midx2+1-(int)e2.x==-1)midx2++;
			if(this.bitmap[midx-(int)this.x]
					[midy-(int)this.y]==1
					& e2.bitmap[midx2-(int)(int)Math.floor(e2.x)]
							[midy-(int)Math.floor(e2.y)]==1){
				System.out.println("Effecient collision detected: "+midx+" "+midy);
				collide = true;
				break;//no need to to further check if there are more collisions
			}
			
		}
		if(collide==true) return true;
		for(int y = ymin;y<ymax;y++){
			
			for(int x=xmin;x<xmax;x++){
				int y1 = y-(int)(this.y);
				int x1 = x-(int)(this.x);
				int y2 = (y -(int)Math.floor(e2.y));
				int x2 = (x -(int)Math.floor(e2.x));
				//System.out.println(x2+" "+ x1);
				if(this.bitmap[x1][y1]==1& e2.bitmap[x2][y2]==1){
				System.out.println("Normal Collision detected at point "+x+" "+y);
		//		e2.move(e2.x+100, e2.y-100);
					collide = true;//collision found
					break;
				}
			}
			if(collide==true)break;//no need to make further checks after first detection
		}
		return collide;
	}
	//	el
		// could also resolve entity collisions in the same we do tile collision resolution
	
}


