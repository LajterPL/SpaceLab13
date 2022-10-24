package io.github.whiterpl.sl13.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import io.github.whiterpl.sl13.gui.CharacterCreationStage;

import java.util.Locale;

public class CharacterCreationController implements InputProcessor {

    int selectionIndex;
    CharacterCreationStage stage;

    public CharacterCreationController(CharacterCreationStage stage) {
        this.stage = stage;
        selectionIndex = 0;
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
