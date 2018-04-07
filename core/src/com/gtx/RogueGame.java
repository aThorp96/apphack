package com.gtx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RogueGame extends ApplicationAdapter {
	SpriteBatch batch;
	
	OrthographicCamera camera;
	float cameraZoom = .1f;
	
	GameMap gameMap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();		
		
		camera = new OrthographicCamera();
		camera.zoom = cameraZoom;
		camera.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
		camera.update();
		
		gameMap = new GameMap( new Vector2(0,0) );
	}

	@Override
	public void render() {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		gameMap.update(deltaTime);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix( camera.combined );
		batch.begin();
		
		gameMap.render(batch);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
