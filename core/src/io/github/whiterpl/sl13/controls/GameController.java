package io.github.whiterpl.sl13.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.Direction;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.item.Slot;
import io.github.whiterpl.sl13.atoms.mob.MobGenerator;
import io.github.whiterpl.sl13.atoms.mob.Skill;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;
import io.github.whiterpl.sl13.gui.GameStage;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

public class GameController implements InputProcessor {

    private final PlayerController playerController;
    private final GameStage gameStage;
    private final StageSwapper stageSwapper;

    private int lookX;
    private int lookY;

    private Prompt currentPrompt;
    enum Prompt {
        NONE,
        WEAR,
        DROP,
        PICK,
        LOOK,
        ATTACK
    }

    public GameController(PlayerController playerController, GameStage gameStage, StageSwapper stageSwapper) {
        this.playerController = playerController;
        this.gameStage = gameStage;
        this.stageSwapper = stageSwapper;
        this.currentPrompt = Prompt.NONE;

        gameStage.getInfoPane().appendMessage("Press ? for list of commands");
    }

    @Override
    public boolean keyDown(int keycode) {

        if (gameStage.getInfoPane().isTextHidden()) gameStage.getInfoPane().showMessages();

        Direction direction = null;

        switch (keycode) {
            case Input.Keys.ESCAPE:
                if (currentPrompt == Prompt.LOOK || currentPrompt == Prompt.ATTACK) {
                    currentPrompt = Prompt.NONE;
                    gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
                    gameStage.getInfoPane().updateStats(playerController);
                    return true;
                }
                break;
            case Input.Keys.NUMPAD_9:
                direction = Direction.NORTH_EAST;
                break;
            case Input.Keys.NUMPAD_8:
                direction = Direction.NORTH;
                break;
            case Input.Keys.NUMPAD_7:
                direction = Direction.NORTH_WEST;
                break;
            case Input.Keys.NUMPAD_6:
                direction = Direction.EAST;
                break;
            case Input.Keys.NUMPAD_5:
                direction = Direction.NONE;
                break;
            case Input.Keys.NUMPAD_4:
                direction = Direction.WEST;
                break;
            case Input.Keys.NUMPAD_3:
                direction = Direction.SOUTH_EAST;
                break;
            case Input.Keys.NUMPAD_2:
                direction = Direction.SOUTH;
                break;
            case Input.Keys.NUMPAD_1:
                direction = Direction.SOUTH_WEST;
                break;
            default:
                return false;
        }

        if (direction != null) {
            switch (currentPrompt) {
                case LOOK:
                    look(direction);
                    return true;
                case ATTACK:
                    attack(direction);
                    return true;
                case NONE:
                    return movePlayer(direction);
                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        if (currentPrompt != Prompt.NONE) {
            switch (currentPrompt) {
                case WEAR:
                    wearItem(character);
                    return true;
                case DROP:
                    dropItem(character);
                    return true;
                case PICK:
                    pickItem(character);
                    return true;
                default:
                    return false;
            }
        }

        switch (character) {
            case 'a':
                attack();
                return true;
            case 'i':
                printInventory();
                return true;
            case 'w':
                wearItem();
                return true;
            case 'd':
                dropItem();
                return true;
            case 'l':
                look();
                return true;
            case '.':
                pickItem();
                return true;
            case '?':
                showHelp();
                return true;
            default:
                return false;
        }
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

    // GETTERS & SETTERS

    public PlayerController getPlayerController() {
        return playerController;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public StageSwapper getStageSwapper() {
        return stageSwapper;
    }


    // METHODS

    private void showHelp() {
        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage("[#f2ee02]Commands:[]");
        gameStage.getInfoPane().appendMessage("i - show inventory");
        gameStage.getInfoPane().appendMessage("w - wear/wield item");
        gameStage.getInfoPane().appendMessage("d - drop item");
        gameStage.getInfoPane().appendMessage("f - fire ranged weapon");
        gameStage.getInfoPane().appendMessage("u - use item");
        gameStage.getInfoPane().appendMessage(". - pick up item");
        gameStage.getInfoPane().appendMessage("");
    }

    private boolean movePlayer(Direction direction) {

        boolean result = true;

        Tile walkedOnTile = playerController
                .getActiveRegion()
                .getTile(playerController.player.getX() + direction.getXMod(), playerController.getPlayer().getY() + direction.getYMod());

        if (walkedOnTile.getMob() != null) {
            if (!walkedOnTile.getMob().hasStatus(Status.FRIENDLY) && walkedOnTile.getMob() != playerController.getPlayer()) {
                attack(direction);
                return true;
            }
        }

        if (!playerController.getPlayer().move(playerController.getActiveRegion(), direction)) {
            gameStage.getInfoPane().appendMessage("[#FF0000]Something blocks your path![]");
            result = false;
        }

        if (result) {

            if (!walkedOnTile.getItems().isEmpty()) {
                StringBuilder sb  = new StringBuilder();
                sb.append("[#f2ee02]You see ");

                for (int i = 0; i < 3 && i < walkedOnTile.getItems().size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(walkedOnTile.getItems().get(i).getName());
                }

                if (walkedOnTile.getItems().size() > 3) sb.append(" and more");
                sb.append(" here.[]");

                gameStage.getInfoPane().appendMessage(sb.toString());
            }

            playerController.getActiveRegion().advanceQueue(playerController);
        }

        gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
        gameStage.getInfoPane().updateStats(playerController);

        return result;
    }

    private void printInventory() {
        StringBuilder sb = new StringBuilder();

        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage("[#f2ee02]Inventory:[]");
        char i = 'a';

        int weightSum = 0;

        for (Item item : playerController.getPlayer().getEquipment().getAllItems()) {

            sb.append(i);
            sb.append(" - ");
            sb.append(String.format("%-25s", item.getName()));

            if (playerController.getPlayer().getEquipment().getWornItems().containsValue(item)) {
                sb.append(" (worn)");
            } else sb.append("       ");

            sb.append("   #");
            sb.append(String.format("%-3d", item.getWeight()));

            weightSum += item.getWeight();
            i++;

            gameStage.getInfoPane().appendMessage(sb.toString());
            sb = new StringBuilder();
        }

        gameStage.getInfoPane().appendMessage(String.format("%3d/%-3d kg", weightSum, MobGenerator.calculateMaxWeightLimit(playerController.getPlayer().getSkills()[Skill.STRENGTH.getIndex()])));
        gameStage.getInfoPane().appendMessage("");
    }

    private void wearItem() {

        if (playerController.getPlayer().getEquipment().getAllItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("You have nothing to wear.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[#f2ee02]What to wear?[] ");

        char i = 'a';

        for (Item item : playerController.getPlayer().getEquipment().getAllItems()) {
            if (item.getUsageSlots().stream()
                    .filter(slot -> slot != Slot.BACKPACK)
                    .anyMatch(slot -> !playerController.player.getEquipment().getWornItems().containsValue(item))) {
                sb.append(i);
            }

            i++;
        }

        if (sb.toString().equals("[#f2ee02]What to wear?[] ")) {
            gameStage.getInfoPane().appendMessage("You have nothing to wear.");
            return;
        }

        currentPrompt = Prompt.WEAR;
        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage(sb.toString());
        gameStage.getInfoPane().appendMessage("");
    }

    private void wearItem(char c) {
        char i = 'a';

        for (Item item : playerController.getPlayer().getEquipment().getAllItems()) {

            if (i == c) {
                playerController.getPlayer().getEquipment().wearItem(playerController.getPlayer(), item);
                gameStage.getInfoPane().appendMessage(String.format("You put on the %s", item.getName()));
                currentPrompt = Prompt.NONE;

                playerController.getActiveRegion().advanceQueue(playerController);
                gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
                gameStage.getInfoPane().updateStats(playerController);
                return;
            }

            i++;
        }

        gameStage.getInfoPane().appendMessage("[#FF0000]This item does not exist![]");
        currentPrompt = Prompt.NONE;
    }

    private void dropItem() {
        gameStage.getInfoPane().hideMessages();

        if (playerController.getPlayer().getEquipment().getAllItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("You have nothing to drop.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[#f2ee02]What to drop?[] ");

        char i = 'a';

        for (Item item : playerController.getPlayer().getEquipment().getAllItems()) {
            if (item.getLegalSlots().stream().anyMatch(slot -> slot != Slot.BACKPACK)) {
                sb.append(i);
            }

            i++;
        }


        currentPrompt = Prompt.DROP;

        gameStage.getInfoPane().appendMessage(sb.toString());
        gameStage.getInfoPane().appendMessage("");
    }

    private void dropItem(char c) {
        char i = 'a';

        for (Item item : playerController.getPlayer().getEquipment().getAllItems()) {

            if (i == c) {
                playerController.getPlayer()
                        .getEquipment()
                        .dropItem(
                                Game.getPlayerController()
                                        .getActiveRegion()
                                        .getTile(Game.getPlayerController().getPlayer().getPosition()),
                                item);
                gameStage.getInfoPane().appendMessage(String.format("You drop the %s", item.getName()));
                currentPrompt = Prompt.NONE;
                return;
            }

            i++;
        }

        gameStage.getInfoPane().appendMessage("[#FF0000]This item does not exist![]");
        currentPrompt = Prompt.NONE;
    }

    private void pickItem() {
        Tile pickTile = playerController.getActiveRegion().getTile(playerController.getPlayer().getPosition());

        if (pickTile.getItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("There is nothing to pick up.");
            return;
        }

        if (pickTile.getItems().size() == 1) {
            if (playerController.getPlayer().getEquipment().takeItem(playerController.player, pickTile.getItems().get(0))) {
                gameStage.getInfoPane().appendMessage(String.format("You picked up the %s", pickTile.getItems().get(0).getName()));
                pickTile.getItems().remove(0);

                playerController.getActiveRegion().advanceQueue(playerController);
                gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
                gameStage.getInfoPane().updateStats(playerController);
                return;
            }

            gameStage.getInfoPane().appendMessage("[#FF0000]You can't pick this up![]");
            return;
        }

        gameStage.getInfoPane().hideMessages();



        gameStage.getInfoPane().appendMessage("[#f2ee02]What to pick up? []");
        char i = 'a';

        for (Item item : pickTile.getItems()) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(" - ");
            sb.append(String.format("%-25s", item.getName()));
            sb.append("   #");
            sb.append(String.format("%-3d", item.getWeight()));

            gameStage.getInfoPane().appendMessage(sb.toString());

            i++;
        }

        currentPrompt = Prompt.PICK;
    }

    private void pickItem(char c) {
        Tile pickTile = playerController.getActiveRegion().getTile(playerController.getPlayer().getPosition());

        char i = 'a';

        for (Item item : pickTile.getItems()) {

            if (i == c) {
                if (playerController.getPlayer().getEquipment().takeItem(playerController.player, item)) {
                    currentPrompt = Prompt.NONE;
                    gameStage.getInfoPane().showMessages();
                    gameStage.getInfoPane().appendMessage(String.format("You picked up the %s", item.getName()));
                    pickTile.removeItem(item);

                    playerController.getActiveRegion().advanceQueue(playerController);
                    gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
                    gameStage.getInfoPane().updateStats(playerController);
                    return;
                }

                currentPrompt = Prompt.NONE;
                gameStage.getInfoPane().showMessages();
                gameStage.getInfoPane().appendMessage("[#FF0000]You can't pick this up![]");
                return;
            }

            i++;
        }
        currentPrompt = Prompt.NONE;
        gameStage.getInfoPane().showMessages();
        gameStage.getInfoPane().appendMessage("[#FF0000]This item does not exist![]");
    }

    private void look() {
        lookX = playerController.getPlayer().getX();
        lookY = playerController.getPlayer().getY();

        currentPrompt = Prompt.LOOK;
    }

    private void look(Direction direction) {
        lookX += direction.getXMod();
        lookY += direction.getYMod();

        lookX = Math.max(lookX, 0);
        lookX = Math.min(lookX, Region.WIDTH);

        lookY = Math.max(lookY, 0);
        lookY = Math.min(lookY, Region.HEIGHT);

        gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), lookX, lookY);
        gameStage.getInfoPane().updateStats(playerController);
    }

    private void attack() {
        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage("Choose attack direction.");

        currentPrompt = Prompt.ATTACK;
    }

    private void attack(Direction direction) {
        playerController.getPlayer().attackMelee(playerController.getActiveRegion(), direction);

        playerController.getActiveRegion().advanceQueue(playerController);

        gameStage.getGamePane().updateGameScreen(playerController.getActiveRegion(), playerController.getPlayer().getX(), playerController.getPlayer().getY());
        gameStage.getInfoPane().updateStats(playerController);

        currentPrompt = Prompt.NONE;
    }

}
