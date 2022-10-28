package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.whiterpl.sl13.controls.CharacterCreationController;
import io.github.whiterpl.sl13.controls.GameController;
import io.github.whiterpl.sl13.controls.MainMenuController;
import io.github.whiterpl.sl13.player.PlayerController;

public class StageSwapper {
    MainMenuStage mainMenuStage;
    CharacterCreationStage characterCreationStage;
    GameStage gameStage;
    PlayerController playerController;

    public enum State {
        MAIN_MENU,
        CHARACTER_CREATION,
        GAME
    }

    State state;

    public StageSwapper(BitmapFont guiFont, BitmapFont gameFont, NinePatchDrawable border) {
        this.mainMenuStage = new MainMenuStage(guiFont, border);
        this.characterCreationStage = new CharacterCreationStage(guiFont, border);
        this.gameStage = new GameStage(guiFont, gameFont, border);
        changeStage(State.MAIN_MENU);
        playerController = new PlayerController();
    }

    public void changeStage(State state) {
        this.state = state;

        switch (state) {
            case MAIN_MENU:
                Gdx.input.setInputProcessor(new MainMenuController(mainMenuStage, this));
                break;
            case CHARACTER_CREATION:
                Gdx.input.setInputProcessor(new CharacterCreationController(playerController, characterCreationStage, this));
                break;
            case GAME:
                gameStage.updateStats(playerController);
                Gdx.input.setInputProcessor(new GameController(playerController, gameStage, this));
                break;
        }
    }

    public void draw() {
        switch (state) {
            case MAIN_MENU:
                mainMenuStage.draw();
                break;
            case CHARACTER_CREATION:
                characterCreationStage.draw();
                break;
            case GAME:
                gameStage.draw();
                break;
        }
    }

    public void dispose() {
        gameStage.dispose();
        characterCreationStage.dispose();
    }

    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);
        characterCreationStage.getViewport().update(width, height);
    }

    public CharacterCreationStage getCharacterCreationStage() {
        return characterCreationStage;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public State getState() {
        return state;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
}
