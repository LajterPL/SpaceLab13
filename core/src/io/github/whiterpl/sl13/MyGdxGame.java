package io.github.whiterpl.sl13;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.whiterpl.sl13.gui.StageSwapper;

public class MyGdxGame extends ApplicationAdapter {

	BitmapFont guiFont;
	BitmapFont gameFont;
	NinePatchDrawable terminalBorder;
	StageSwapper stageSwapper;

	@Override
	public void create () {
		guiFont = new BitmapFont(Gdx.files.internal("font/digitaldisco-32.fnt"));
		gameFont = new BitmapFont(Gdx.files.internal("font/digitaldisco-32-fixed-width.fnt"));
		gameFont.getData().markupEnabled = true;
		guiFont.getData().markupEnabled = true;
		gameFont.setFixedWidthGlyphs("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"abcdefghijklmnopqrstuvwxyz" +
				"1234567890 " +
				"\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
		terminalBorder = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("sprites/terminalborder.jpg")), 6, 6, 6, 6));

		stageSwapper = new StageSwapper(guiFont, gameFont, terminalBorder);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0.031F, 0, 0.106F, 1);
		stageSwapper.draw();
	}

	@Override
	public void resize(int width, int height) {
		stageSwapper.resize(width, height);
	}

	@Override
	public void dispose () {
		stageSwapper.dispose();
		guiFont.dispose();
	}
}
