package com.gtx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RogueGame extends ApplicationAdapter {
	SpriteBatch batch;
	SpriteBatch uiBatch;
	OrthographicCamera ui;
	Texture overlay;

	private BitmapFont font;
    
    
	public static OrthographicCamera camera;
	public static float defaultZoom = .07f;
	
	public static float cameraZoom = defaultZoom;
	
	public static final int SPEED = 4;
	public static final int GOBLIN_VIEW_DISTANCE = 6;

	GameMap gameMap;
	
	public static int score = 0;
	int defaultWidth;
	int defaultHeight;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();	
		uiBatch = new SpriteBatch();
				
		camera = new OrthographicCamera();
		ui = new OrthographicCamera();
		defaultHeight = Gdx.graphics.getHeight();
		defaultWidth = Gdx.graphics.getWidth();
		updateCameras();
		
		overlay = new Texture("../core/assets/fog.png");


		gameMap = new GameMap( new Vector2(40,40) );
		
		Gdx.input.setInputProcessor( gameMap );
	
		font = new BitmapFont();
        font.setColor(Color.RED);
	
	}

	@Override
	public void render() {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		gameMap.update(deltaTime);
		update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.translate(gameMap.hero.position.x - camera.position.x , gameMap.hero.position.y - camera.position.y);
		camera.update();
		
		ui.translate(gameMap.hero.position.x - camera.position.x , gameMap.hero.position.y - camera.position.y);
		ui.update();


		if (Gdx.input.isTouched()) {
			//camera.translate(-Gdx.input.getDeltaX() * cameraZoom, Gdx.input.getDeltaY() * cameraZoom);
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

		uiBatch.setProjectionMatrix( ui.combined );
		
		uiBatch.begin();
		
		gameMap.render(uiBatch);

		uiBatch.draw( overlay, 
				ui.position.x - Gdx.graphics.getWidth()/2 , 
				ui.position.y - Gdx.graphics.getHeight()/2,
				Gdx.graphics.getWidth(), 
				Gdx.graphics.getHeight());
		//uiBatch.draw( overlay, camera.position.x , camera.position.y);
		

		font.draw(uiBatch, "Score: " + score, ui.position.x + Gdx.graphics.getWidth()/4, ui.position.y + Gdx.graphics.getHeight()/2 - 12);
		
		uiBatch.end();


	}
	
	private void updateCameras() {
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = cameraZoom;
		camera.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
		camera.update();
		
		ui.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ui.update();
	}
	
	@Override
	public void resize(int width, int height) {
		float widthRatio =  (float)   defaultWidth / (float) width;
		float heightRatio =  (float)     defaultHeight / (float) height;
		if ( widthRatio < heightRatio) {
			RogueGame.cameraZoom = RogueGame.defaultZoom * widthRatio;
		} else {
			RogueGame.cameraZoom = RogueGame.defaultZoom * heightRatio;
		}
		updateCameras();
		
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		uiBatch.dispose();
		
	}
	
	private void update() {
		
	}
}
