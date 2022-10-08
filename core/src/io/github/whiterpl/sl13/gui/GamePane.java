package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class GamePane extends Table {

    protected Label gameLabel;

    public GamePane(BitmapFont font, NinePatchDrawable border) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        gameLabel = new Label("34x48", labelStyle);
        gameLabel.setWrap(true);

        this.add(gameLabel).expand().center().left().pad(5);
        this.background(border);
    }
}
