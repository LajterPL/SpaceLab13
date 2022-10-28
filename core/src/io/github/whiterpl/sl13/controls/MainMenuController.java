package io.github.whiterpl.sl13.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.github.whiterpl.sl13.gui.MainMenuStage;
import io.github.whiterpl.sl13.gui.StageSwapper;

public class MainMenuController implements InputProcessor {

    MainMenuStage mainMenuStage;
    StageSwapper stageSwapper;

    int selectionIndex = 0;

    public MainMenuController(MainMenuStage mainMenuStage, StageSwapper stageSwapper) {
        this.mainMenuStage = mainMenuStage;
        this.stageSwapper = stageSwapper;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode){
            case Input.Keys.RIGHT:
                selectionIndex++;
                if (selectionIndex > 1) selectionIndex = 0;
                mainMenuStage.selectButton(selectionIndex);
                return true;
            case Input.Keys.LEFT:
                selectionIndex--;
                if (selectionIndex < 0) selectionIndex = 1;
                mainMenuStage.selectButton(selectionIndex);
                return true;
            case Input.Keys.ENTER:
                if (selectionIndex == 0) {
                    stageSwapper.changeStage(StageSwapper.State.CHARACTER_CREATION);
                    return true;
                }
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
            default:
                return false;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(isPressed(mainMenuStage.getNewGameButton(), screenX, screenY)) {
            selectionIndex = 0;
            mainMenuStage.selectButton(selectionIndex);
            return true;
        } else if (isPressed(mainMenuStage.getCreditsButton(), screenX, screenY)) {
            selectionIndex = 1;
            mainMenuStage.selectButton(selectionIndex);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private boolean isPressed(TextButton button, int x, int y) {
        x = (int) (1920 - x - button.getX());
        y = (int) (1080 - y - button.getY());
        return x >= 0 && y >= 0 && x <= button.getWidth() && y <= button.getHeight();
    }
}
