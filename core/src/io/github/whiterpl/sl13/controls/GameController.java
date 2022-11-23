package io.github.whiterpl.sl13.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import io.github.whiterpl.sl13.atoms.Direction;
import io.github.whiterpl.sl13.gui.GameStage;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

public class GameController implements InputProcessor {

    PlayerController playerController;
    GameStage gameStage;
    StageSwapper stageSwapper;

    public GameController(PlayerController playerController, GameStage gameStage, StageSwapper stageSwapper) {
        this.playerController = playerController;
        this.gameStage = gameStage;
        this.stageSwapper = stageSwapper;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
            case Input.Keys.NUMPAD_9:
                movePlayer(Direction.NORTH_EAST);
                break;
            case Input.Keys.NUMPAD_8:
                movePlayer(Direction.NORTH);
                break;
            case Input.Keys.NUMPAD_7:
                movePlayer(Direction.NORTH_WEST);
                break;
            case Input.Keys.NUMPAD_6:
                movePlayer(Direction.EAST);
                break;
            case Input.Keys.NUMPAD_5:
                movePlayer(Direction.NONE);
                break;
            case Input.Keys.NUMPAD_4:
                movePlayer(Direction.WEST);
                break;
            case Input.Keys.NUMPAD_3:
                movePlayer(Direction.SOUTH_EAST);
                break;
            case Input.Keys.NUMPAD_2:
                movePlayer(Direction.SOUTH);
                break;
            case Input.Keys.NUMPAD_1:
                movePlayer(Direction.SOUTH_WEST);
                break;
            default:

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

    private void movePlayer(Direction direction) {

        playerController.getActiveRegion().advanceQueue(playerController);

        if (!playerController.getPlayer().move(playerController.getActiveRegion(), direction)) {
            gameStage.appendMessage("[#FF0000]Something blocks your path![]");
        }

        gameStage.updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
        gameStage.updateStats(playerController);
    }

}
