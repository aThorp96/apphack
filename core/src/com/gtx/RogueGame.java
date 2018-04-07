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
	
	public static OrthographicCamera camera;
	public static float cameraZoom = .07f;
	
	public static final int SPEED = 4;
	public static final int GOBLIN_VIEW_DISTANCE = 6;
	
	GameMap gameMap;
	
	public static int score = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();		
				
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = cameraZoom;
		camera.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
		camera.update();

		gameMap = new GameMap( new Vector2(40,40) );
		
		Gdx.input.setInputProcessor( gameMap );
	}

	@Override
	public void render() {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		gameMap.update(deltaTime);
		update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched()) {
			camera.translate(-Gdx.input.getDeltaX() * cameraZoom, Gdx.input.getDeltaY() * cameraZoom);
			camera.update();
		}
		
		batch.setProjectionMatrix( camera.combined );
		batch.begin();
		

		for (int i = 0; i < cameraZoom*Gdx.graphics.getHeight()+10; i++) {
			for (int j = 0; j < cameraZoom*Gdx.graphics.getWidth()+10; j++) {
				batch.draw(TileType.WALL.getTexture(), (float)Math.floor(camera.position.x - cameraZoom*Gdx.graphics.getWidth()/2) - .5f + j, (float)Math.floor(camera.position.y - cameraZoom*Gdx.graphics.getHeight()/2) - .5f + i, 1f, 1f);
			}
		}
		gameMap.render(batch);
		
		batch.end();
		
		//System.out.println("Score: " + score);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	private void update() {
		
	}
}
