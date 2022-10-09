package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.whiterpl.sl13.atoms.Region;

public class GameStage extends Stage {
    private GamePane gamePane;
    private InfoPane infoPane;

    public GameStage (BitmapFont guiFont, BitmapFont gamefont, NinePatchDrawable border) {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        this.addActor(rootTable);

        gamePane = new GamePane(gamefont, border);
        rootTable.add(gamePane).expandY().width(Gdx.graphics.getHeight()).fill().top().left();

        infoPane = new InfoPane(guiFont, border);
        rootTable.add(infoPane).expand().fill().top().left().padLeft(5);
    }

    public void updateGameScreen(Region acticeRegion, int x, int y) {
        gamePane.updateGameScreen(acticeRegion, x, y);
    }

}
