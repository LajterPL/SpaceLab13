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

public class GameController implements InputProcessor {
    
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

    public GameController(GameStage gameStage, StageSwapper stageSwapper) {
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
                    gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
                    gameStage.getInfoPane().updateStats(Game.getPlayerController());
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
            case '>':
                goDown();
                return true;
            case '<':
                goUp();
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
        gameStage.getInfoPane().appendMessage(". - pick up item");
        gameStage.getInfoPane().appendMessage("< - go down");
        gameStage.getInfoPane().appendMessage("> - go up");
        gameStage.getInfoPane().appendMessage("");
    }

    private boolean movePlayer(Direction direction) {

        boolean result = true;

        Tile walkedOnTile = Game.getPlayerController()
                .getActiveRegion()
                .getTile(Game.getPlayerController().player.getX() + direction.getXMod(), Game.getPlayerController().getPlayer().getY() + direction.getYMod());

        if (walkedOnTile.getMob() != null) {
            if (!walkedOnTile.getMob().hasStatus(Status.FRIENDLY) && walkedOnTile.getMob() != Game.getPlayerController().getPlayer()) {
                attack(direction);
                return true;
            }
        }

        if(walkedOnTile.hasStatus(Status.BLOCK_PASSING)) {
            if(walkedOnTile.getStructure() != null && walkedOnTile.getStructure().hasInteraction()) {
                walkedOnTile.getStructure().interact(Game.getPlayerController().getPlayer());

                Game.getPlayerController().getActiveRegion().advanceQueue();
                gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
                gameStage.getInfoPane().updateStats(Game.getPlayerController());
                return true;
            }
        }

        if (!Game.getPlayerController().getPlayer().move(Game.getPlayerController().getActiveRegion(), direction)) {
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

            Game.getPlayerController().getActiveRegion().advanceQueue();
        }

        gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
        gameStage.getInfoPane().updateStats(Game.getPlayerController());

        return result;
    }

    private void printInventory() {
        StringBuilder sb = new StringBuilder();

        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage("[#f2ee02]Inventory:[]");
        char i = 'a';

        int weightSum = 0;

        for (Item item : Game.getPlayerController().getPlayer().getEquipment().getAllItems()) {

            sb.append(i);
            sb.append(" - ");
            sb.append(String.format("%-25s", item.getName()));

            if (Game.getPlayerController().getPlayer().getEquipment().getWornItems().containsValue(item)) {
                sb.append(" (worn)");
            } else sb.append("       ");

            sb.append("   #");
            sb.append(String.format("%-3d", item.getWeight()));

            weightSum += item.getWeight();
            i++;

            gameStage.getInfoPane().appendMessage(sb.toString());
            sb = new StringBuilder();
        }

        gameStage.getInfoPane().appendMessage(String.format("%3d/%-3d", weightSum, MobGenerator.calculateMaxWeightLimit(Game.getPlayerController().getPlayer().getSkills()[Skill.STRENGTH.getIndex()])));
        gameStage.getInfoPane().appendMessage("");
    }

    private void wearItem() {

        if (Game.getPlayerController().getPlayer().getEquipment().getAllItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("You have nothing to wear.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[#f2ee02]What to wear?[] ");

        char i = 'a';

        for (Item item : Game.getPlayerController().getPlayer().getEquipment().getAllItems()) {
            if (item.getUsageSlots().stream()
                    .filter(slot -> slot != Slot.BACKPACK)
                    .anyMatch(slot -> !Game.getPlayerController().player.getEquipment().getWornItems().containsValue(item))) {
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

        for (Item item : Game.getPlayerController().getPlayer().getEquipment().getAllItems()) {

            if (i == c) {
                Game.getPlayerController().getPlayer().getEquipment().wearItem(Game.getPlayerController().getPlayer(), item);
                gameStage.getInfoPane().appendMessage(String.format("You put on the %s", item.getName()));
                currentPrompt = Prompt.NONE;

                Game.getPlayerController().getActiveRegion().advanceQueue();
                gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
                gameStage.getInfoPane().updateStats(Game.getPlayerController());
                return;
            }

            i++;
        }

        gameStage.getInfoPane().appendMessage("[#FF0000]This item does not exist![]");
        currentPrompt = Prompt.NONE;
    }

    private void dropItem() {
        gameStage.getInfoPane().hideMessages();

        if (Game.getPlayerController().getPlayer().getEquipment().getAllItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("You have nothing to drop.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[#f2ee02]What to drop?[] ");

        char i = 'a';

        for (Item item : Game.getPlayerController().getPlayer().getEquipment().getAllItems()) {
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

        for (Item item : Game.getPlayerController().getPlayer().getEquipment().getAllItems()) {

            if (i == c) {
                Game.getPlayerController().getPlayer()
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
        Tile pickTile = Game.getPlayerController().getActiveRegion().getTile(Game.getPlayerController().getPlayer().getPosition());

        if (pickTile.getItems().isEmpty()) {
            gameStage.getInfoPane().appendMessage("There is nothing to pick up.");
            return;
        }

        if (pickTile.getItems().size() == 1) {
            if (Game.getPlayerController().getPlayer().getEquipment().takeItem(Game.getPlayerController().player, pickTile.getItems().get(0))) {
                gameStage.getInfoPane().appendMessage(String.format("You picked up the %s", pickTile.getItems().get(0).getName()));
                pickTile.getItems().remove(0);

                Game.getPlayerController().getActiveRegion().advanceQueue();
                gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
                gameStage.getInfoPane().updateStats(Game.getPlayerController());
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
        Tile pickTile = Game.getPlayerController().getActiveRegion().getTile(Game.getPlayerController().getPlayer().getPosition());

        char i = 'a';

        for (Item item : pickTile.getItems()) {

            if (i == c) {
                if (Game.getPlayerController().getPlayer().getEquipment().takeItem(Game.getPlayerController().player, item)) {
                    currentPrompt = Prompt.NONE;
                    gameStage.getInfoPane().showMessages();
                    gameStage.getInfoPane().appendMessage(String.format("You picked up the %s", item.getName()));
                    pickTile.removeItem(item);

                    Game.getPlayerController().getActiveRegion().advanceQueue();
                    gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
                    gameStage.getInfoPane().updateStats(Game.getPlayerController());
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
        lookX = Game.getPlayerController().getPlayer().getX();
        lookY = Game.getPlayerController().getPlayer().getY();

        currentPrompt = Prompt.LOOK;
    }

    private void look(Direction direction) {
        lookX += direction.getXMod();
        lookY += direction.getYMod();

        lookX = Math.max(lookX, 0);
        lookX = Math.min(lookX, Region.WIDTH);

        lookY = Math.max(lookY, 0);
        lookY = Math.min(lookY, Region.HEIGHT);

        gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), lookX, lookY);
        gameStage.getInfoPane().updateStats(Game.getPlayerController());
    }

    private void attack() {
        gameStage.getInfoPane().hideMessages();
        gameStage.getInfoPane().appendMessage("Choose attack direction.");

        currentPrompt = Prompt.ATTACK;
    }

    private void attack(Direction direction) {
        Game.getPlayerController().getPlayer().attackMelee(Game.getPlayerController().getActiveRegion(), direction);

        Game.getPlayerController().getActiveRegion().advanceQueue();

        gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
        gameStage.getInfoPane().updateStats(Game.getPlayerController());

        currentPrompt = Prompt.NONE;
    }

    private void goDown() {
        if(Game.getPlayerController().getPlayer().hasStatus(Status.PARALYZED)) return;

        Tile downTile = Game.getPlayerController().getActiveRegion().getTile(Game.getPlayerController().getPlayer().getPosition());

        if(downTile.getStructure() != null && downTile.getStructure().getSymbol() == '>') {
            Game.getPlayerController().goDown();
            Game.getInfoPane().appendMessage("[#f2ee02]You go down the stairs[]");
        } else {
            Game.getInfoPane().appendMessage("[#FF0000]You can't go down here![]");
        }

        gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
        gameStage.getInfoPane().updateStats(Game.getPlayerController());
    }

    private void goUp() {
        if(Game.getPlayerController().getPlayer().hasStatus(Status.PARALYZED)) return;

        Tile upTile = Game.getPlayerController().getActiveRegion().getTile(Game.getPlayerController().getPlayer().getPosition());

        if(upTile.getStructure() != null && upTile.getStructure().getSymbol() == '<') {
            Game.getPlayerController().goUp();
            Game.getInfoPane().appendMessage("[#f2ee02]You go up the stairs[]");
        } else {
            Game.getInfoPane().appendMessage("[#FF0000]You can't go up here![]");
        }

        gameStage.getGamePane().updateGameScreen(Game.getPlayerController().getActiveRegion(), Game.getPlayerController().getPlayer().getX(), Game.getPlayerController().getPlayer().getY());
        gameStage.getInfoPane().updateStats(Game.getPlayerController());
    }

}
