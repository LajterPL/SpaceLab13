package io.github.whiterpl.sl13;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.whiterpl.sl13.atoms.item.ItemGenerator;
import io.github.whiterpl.sl13.atoms.mob.MobGenerator;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.RegionGenerator;
import io.github.whiterpl.sl13.atoms.structure.StructureGenerator;
import io.github.whiterpl.sl13.gui.InfoPane;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class Game extends ApplicationAdapter {

	private static Game gameInstance;

	//GUI
	private BitmapFont guiFont;
	private BitmapFont gameFont;
	private NinePatchDrawable terminalBorder;
	private StageSwapper stageSwapper;

	//GAME
	private List<Region> levels;


	public static Game getInstance() {
		if (gameInstance == null) gameInstance = new Game();
		return gameInstance;
	}

	public static PlayerController getPlayerController() {
		return gameInstance.stageSwapper.getPlayerController();
	}
	public static InfoPane getInfoPane() { return getInstance().stageSwapper.getGameStage().getInfoPane();}
	public static StageSwapper getStageSwapper() { return getInstance().stageSwapper;}
	public static List<Region> getLevels() { return getInstance().levels;}

	public void startNewGame() {
		levels.clear();

		MobGenerator.initialize();
		ItemGenerator.initialize();
		StructureGenerator.initialize();

		levels.add(RegionGenerator.getStartingRegion());

		getPlayerController().setActiveRegion(levels.get(0));
	}



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

		levels = new ArrayList<>();
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
