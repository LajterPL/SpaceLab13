package io.github.whiterpl.sl13;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.whiterpl.sl13.gui.GameStage;

public class MyGdxGame extends ApplicationAdapter {

	GameStage gameStage;
	BitmapFont guiFont;
	BitmapFont gameFont;
	NinePatchDrawable terminalBorder;

	@Override
	public void create () {
		guiFont = new BitmapFont(Gdx.files.internal("font/digitaldisco-32.fnt"));
		gameFont = new BitmapFont(Gdx.files.internal("font/digitaldisco-32-fixed-width.fnt"));
		gameFont.setFixedWidthGlyphs("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"abcdefghijklmnopqrstuvwxyz" +
				"1234567890 " +
				"\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
		terminalBorder = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("sprites/terminalborder.png")), 6, 6, 6, 6));
		gameStage = new GameStage(guiFont, gameFont, terminalBorder);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		gameStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		gameStage.dispose();
		guiFont.dispose();
	}
}
