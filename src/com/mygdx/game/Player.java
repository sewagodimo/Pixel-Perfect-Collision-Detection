package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Entity;

public class Player extends Entity {
	int[][] bitmap;
	public Player(MyGdxGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		bitmap = super.entityBitMap();
	}

	@Override
	public void update(float delta) {
		
		dx = 0;
		dy = 0;
	//	bound.setPosition(dx, dy);
//set the rectangle
	//	this.getBound().
		// move
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			dy = speed * delta;//so the rect will also get updated
			//bound.y = speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			dy = -speed * delta;
		//	bound.y = -speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			dx = -speed * delta;
		//	bound.x = -speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			dx = speed * delta;//we make sure that the rect also moves
		//	bound.x = speed * delta;
		}
	}
	
}
