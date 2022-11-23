package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;

public class MainMenuStage extends Stage {

    private Table rootTable;
    private Image logo;
    private TextButton newGameButton;
    private TextButton creditsButton;

    public MainMenuStage(BitmapFont font, NinePatchDrawable border) {
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        this.addActor(rootTable);

        logo = new Image();
        logo.setDrawable(new SpriteDrawable(new Sprite(new Texture("sprites/sl13.png"))));
        logo.setScaling(Scaling.fill);
        logo.setHeight(Gdx.graphics.getHeight()/3f);
        rootTable.add(logo).height(logo.getHeight()).fillX().pad(20);

        rootTable.row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.checked = border;
        buttonStyle.disabled = border.tint(new Color(1, 1, 1, 0.5f));
        buttonStyle.disabledFontColor = new Color(1, 1, 1, 0.5f);
        buttonStyle.focused = border;

        newGameButton = new TextButton(" New Game ", buttonStyle);
        newGameButton.setTouchable(Touchable.enabled);
        newGameButton.setProgrammaticChangeEvents(true);
        newGameButton.toggle();

        rootTable.add(newGameButton).pad(10);

        creditsButton = new TextButton(" Credits ", buttonStyle);
        creditsButton.setTouchable(Touchable.enabled);
        creditsButton.setDisabled(true);
        creditsButton.toggle();

        rootTable.row();
        rootTable.add(creditsButton).width(newGameButton.getWidth()).pad(10);

    }

    public TextButton getNewGameButton() {
        return newGameButton;
    }

    public TextButton getCreditsButton() {
        return creditsButton;
    }

    public Image getLogo() {
        return logo;
    }

    public void selectButton(int selectionIndex) {
        if (selectionIndex == 0) {
            newGameButton.setDisabled(false);
            creditsButton.setDisabled(true);
        } else {
            newGameButton.setDisabled(true);
            creditsButton.setDisabled(false);
        }
    }

}
