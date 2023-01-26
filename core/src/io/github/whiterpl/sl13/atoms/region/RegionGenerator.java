package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.Direction;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.item.Slot;
import io.github.whiterpl.sl13.atoms.mob.Action;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.atoms.structure.Structure;

import java.util.Random;

public class RegionGenerator {

    private static final Random randomGen = new Random();

    public static Region getBlankRegion() {
        Region region = new Region();
        Structure wall = new Structure("Wall", "Wall", '#', "9e9baaff");
        wall.addStatus(Status.BLOCK_PASSING);

        for (int x = 0; x < Region.WIDTH; x++) {
            region.tiles[x][0].setStructure(wall);
            region.tiles[x][Region.HEIGHT - 1].setStructure(wall);
        }

        for (int y = 0; y < Region.HEIGHT; y++) {
            region.tiles[0][y].setStructure(wall);
            region.tiles[Region.WIDTH - 1][y].setStructure(wall);
        }

        return region;
    }

    public static Region getTestingRegion() {
        Region region = getBlankRegion();

        Action moveNorth = new Action(1, (m) -> true, (m) -> m.move(Game.getPlayerController().getActiveRegion(), Direction.NORTH));

        Mob testMob = new Mob("Alien", "ayy lmao", 'A', "FF00FFFF");
        testMob.setActionDelay(7);
        testMob.addAction(moveNorth);
        testMob.placeMob(region, 10, 20);

        Mob testMob2 = new Mob("Salamander", "Some kind of a lizard", 'S', "efe51cFF");
        testMob2.setActionDelay(9);
        testMob2.addAction(moveNorth);
        testMob2.placeMob(region, 9, 20);

        Mob testMob3 = new Mob("Zombie", "He wants your brain", 'Z', "1cef6aFF");
        testMob3.setActionDelay(15);
        testMob3.addAction(moveNorth);
        testMob3.placeMob(region, 8, 20);

        Mob testMob4 = new Mob("Alien", "ayy lmao", 'A', "AA00FFFF");
        testMob4.setActionDelay(7);
        testMob4.addAction(1, Action.GenericAction.WALK_RANDOM);
        testMob4.addAction(2, Action.GenericAction.ATTACK, Status.HUMAN);
        testMob4.placeMob(region, 20, 20);

        Item helmet = new Item("Helmet", "Thing to protect your head", 'n', "8ffcefFF");
        helmet.addLegalSlot(Slot.BACKPACK);
        helmet.addLegalSlot(Slot.HEAD);
        helmet.addLegalSlot(Slot.RIGHT_HAND);
        helmet.addLegalSlot(Slot.LEFT_HAND);
        helmet.addUsageSlot(Slot.HEAD);
        helmet.setUsageAction(new Action(0, mob -> {return true;}, mob -> {mob.setArmorMod(5);}));
        region.getTile(3, 3).addItem(helmet);

        for (int i = 0; i < 40; i++) {
            placeRoom(region, 6, 15, 4, 25);
        }

        for (int i = 0; i < 50; i++) {
            placeWall(region, 4, 10, 25);
        }

        return region;
    }

    public static void placeWall(Region region, int minSize, int maxSize, int maxTries) {
        Structure wall = new Structure("Wall", "Wall", '#', "9e9baaff");
        wall.addStatus(Status.BLOCK_PASSING);

        for (int i = 0; i < maxTries; i++) {

            int width = randomGen.nextInt(maxSize - minSize) + minSize;

            int randomX = randomGen.nextInt(Region.WIDTH - (width + 2)) + 2; // Ściany mogą się generować tylko tak, żeby nie dotykać krawędzi regionu
            int randomY = randomGen.nextInt(Region.HEIGHT - (width + 2)) + 2;

            int orientation = randomGen.nextInt(5);

            boolean validWall;

            switch (orientation) {
                case 0:
                case 1:
                    validWall = canPlaceStructure(region, randomX - 1, randomY - 1, randomX + width, randomY + 1);
                    break;
                case 2:
                case 3:
                    validWall = canPlaceStructure(region, randomX - 1, randomY - 1, randomX + 1, randomY + width);
                    break;
                default:

                    if ((width | 1) != width) { //Jeśli szerokość nie jest nieparzysta
                        if (width + 1 <= maxSize) width++;
                        else width--;
                    }

                    validWall = canPlaceStructure(region, randomX + width/2 - 1, randomY - 1, randomX + width/2 + 1, randomY + width);
                    validWall = validWall && canPlaceStructure(region, randomX - 1, randomY + width/2 - 1, randomX + width, randomY + width/2 + 1);
            }


            if (!validWall) continue;

            switch (orientation) {
                case 0:
                case 1:
                    for (int iX = randomX; iX < randomX + width; iX++) {
                        region.getTile(iX, randomY).setStructure(wall);
                    }
                    break;
                case 2:
                case 3:
                    for (int iY = randomY; iY < randomY + width; iY++) {
                        region.getTile(randomX, iY).setStructure(wall);
                    }
                    break;
                default:
                    for (int iX = randomX; iX < randomX + width; iX++) {
                        for (int iY = randomY; iY < randomY + width; iY++) {
                            if (iX == randomX + width/2 || iY == randomY + width/2) {
                                region.getTile(iX, iY).setStructure(wall);
                            }
                        }
                    }
            }



            return;

        }
    }

    public static void placeRoom(Region region, int minSize, int maxSize, int maxDoors, int maxTries) {

        Structure wall = new Structure("Wall", "Wall", '#', "9e9baaff");
        wall.addStatus(Status.BLOCK_PASSING);

        Structure door = new Structure("Door", "Door", '+', "9e9baafe");

        for (int i = 0; i < maxTries; i++) {
            int width = randomGen.nextInt(maxSize - minSize) + minSize;
            int height = randomGen.nextInt(maxSize - minSize) + minSize;

            int randomX = randomGen.nextInt(Region.WIDTH - (width + 3)) + 3; // Pokoje mogą się generować tylko tak, żeby nie dotykać krawędzi regionu
            int randomY = randomGen.nextInt(Region.HEIGHT - (height + 3)) + 3;

            boolean validRoom = true;

            for (int iX = randomX - 2; iX < randomX + width + 4; iX++) { //Sprawdzanie, czy pokój nie dotyka istniejących ścian
                for (int iY = randomY - 2; iY < randomY + height + 4; iY++) {
                    if (region.getTile(iX, iY) == null || region.getTile(iX, iY).getStructure() != null) {
                        validRoom = false;
                        break;
                    }
                }
                if (!validRoom) break;;
            }

            if (!validRoom) continue;

            int doorNum = randomGen.nextInt(maxDoors) + 1;
            int doorOffset = ((2*width + 2*height) - doorNum)/doorNum + 1;
            int randomOffset = randomGen.nextInt(width/2) + 1;

            for (int iX = randomX - 1; iX < randomX + width + 2; iX++) {
                for (int iY = randomY - 1; iY < randomY + height + 2; iY++) {
                    if (iY == randomY - 1 || iY == randomY + height + 1 || iX == randomX - 1 || iX == randomX + width + 1) { //Sptawianie ścian i drzwi
                        if (doorNum > 0 && randomOffset <= 0 && !isCorner(iX, iY, randomX - 1, randomY - 1, randomX + width + 1, randomY + height + 1)) {
                            region.getTile(iX, iY).setStructure(door);
                            randomOffset = doorOffset;
                            doorNum--;
                        } else {
                            region.getTile(iX, iY).setStructure(wall);
                            randomOffset--;
                        }
                    }
                }
            }

            return;

        }
    }

    private static boolean canPlaceStructure(Region region, int lX, int tY, int rX, int bY) {

        for (int iX = lX; iX <= rX; iX++) {
            for (int iY = tY; iY <= bY; iY++) {
                if (region.getTile(iX, iY) == null || region.getTile(iX, iY).getStructure() != null) return false;
            }
        }

        return true;
    }

    private static boolean isCorner(int x, int y, int lX, int tY, int rX, int bY) {

        boolean isTopBottom = y == tY || y == bY;
        boolean isLeftRight = x == lX || x == rX;

        return isTopBottom && isLeftRight;
    }

}
