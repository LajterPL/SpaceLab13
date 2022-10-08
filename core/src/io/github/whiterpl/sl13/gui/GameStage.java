package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class GameStage extends Stage {

    public GameStage (BitmapFont guiFont, BitmapFont gamefont, NinePatchDrawable border) {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.setDebug(true);
        this.addActor(rootTable);

        GamePane gamePane = new GamePane(gamefont, border);
        rootTable.add(gamePane).expandY().width(Gdx.graphics.getHeight()).fill().top().left();

        InfoPane infoPane = new InfoPane(guiFont, border);
        rootTable.add(infoPane).expand().fill().top().left().padLeft(5);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
