package io.github.whiterpl.sl13.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import io.github.whiterpl.sl13.gui.CharacterCreationStage;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

import java.util.Locale;

public class CharacterCreationController implements InputProcessor {

    int selectionIndex;

    PlayerController playerController;
    CharacterCreationStage stage;
    StageSwapper stageSwapper;

    public CharacterCreationController(PlayerController playerController, CharacterCreationStage stage, StageSwapper stageSwapper) {
        this.playerController = playerController;
        this.stage = stage;
        this.stageSwapper = stageSwapper;
        selectionIndex = 0;

        stage.resetLabels();
    }

    @Override
    public boolean keyDown(int keycode) {

        int oldIndex;

        switch (keycode) {
            case Input.Keys.LEFT:
                oldIndex = selectionIndex;
                selectionIndex--;
                selectionIndex = selectionIndex < 0 ? 9 : selectionIndex;
                stage.selectLabel(selectionIndex, oldIndex);
                return true;
            case Input.Keys.RIGHT:
                oldIndex = selectionIndex;
                selectionIndex++;
                selectionIndex = selectionIndex > 9 ? 0 : selectionIndex;
                stage.selectLabel(selectionIndex, oldIndex);
                return true;
            case Input.Keys.UP:
                if (selectionIndex != 0) {
                    stage.addPoint(selectionIndex);
                    return true;
                }
                break;
            case Input.Keys.DOWN:
                if (selectionIndex != 0) {
                    stage.removePoint(selectionIndex);
                    return true;
                }
                break;
            case Input.Keys.BACKSPACE:
                if (selectionIndex == 0) {
                    stage.removeCharFromName();
                    return true;
                }
                break;
            case Input.Keys.ENTER:
                if (stage.getPointsToSpend() == 0) {
                    stageSwapper.setPlayerController(stage.getPlayerController());
                    stageSwapper.changeStage(StageSwapper.State.GAME);
                    return true;
                }
                break;
            case Input.Keys.R:
                if (selectionIndex != 0) {
                    stage.setRandomPoints();
                }
                break;
            case Input.Keys.ESCAPE:
                stageSwapper.changeStage(StageSwapper.State.MAIN_MENU);
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

        if (selectionIndex == 0 && "abcdefghijklmnopqrtuwxyz".contains(String.valueOf(character).toLowerCase(Locale.ROOT))) stage.addCharToName(character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
}
