package com.gtx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;


public class RogueGame extends ApplicationAdapter {
	SpriteBatch batch;
	
	OrthographicCamera camera;
	OrthographicCamera textCamera;

	float cameraZoom = .03f;
	
	GameMap gameMap;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();		
				
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = cameraZoom;
		camera.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
		camera.update();
		textCamera = new OrthographicCamera();
		textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		textCamera.translate(-Gdx.graphics.getWidth()/5f, -Gdx.graphics.getHeight()/1.25f);
		textCamera.zoom = 0.6f;
		textCamera.update();

		
		gameMap = new GameMap( new Vector2(25,25) );
		
		Gdx.input.setInputProcessor( gameMap );
		
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/OpenSans-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		font.setColor(Color.BROWN);
		//font.getData().setScale(1f);
	}

	@Override
	public void render() {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		gameMap.update(deltaTime);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched()) {
			camera.translate(-Gdx.input.getDeltaX() * cameraZoom, Gdx.input.getDeltaY() * cameraZoom);
			camera.update();
		}
		
		batch.setProjectionMatrix( camera.combined );
		batch.begin();
		
		gameMap.render(batch);
		batch.end();
		

		batch.setProjectionMatrix(textCamera.combined);
		batch.begin();
		
		
		// 
		//Gdx.graphics.getHeight();
		font.draw(batch, "" + Gdx.graphics.getHeight() + ", " + textCamera.viewportHeight, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
