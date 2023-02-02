package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.Direction;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.item.ItemGenerator;
import io.github.whiterpl.sl13.atoms.item.Slot;
import io.github.whiterpl.sl13.atoms.mob.Action;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.atoms.mob.MobGenerator;
import io.github.whiterpl.sl13.atoms.structure.Structure;
import io.github.whiterpl.sl13.atoms.structure.StructureGenerator;

import java.util.Random;

public class RegionGenerator {

    private static final Random randomGen = new Random();

    public static Region getBlankRegion() {
        Region region = new Region();

        for (int x = 0; x < Region.WIDTH; x++) {
            region.tiles[x][0].setStructure(StructureGenerator.getWall());
            region.tiles[x][Region.HEIGHT - 1].setStructure(StructureGenerator.getWall());
        }

        for (int y = 0; y < Region.HEIGHT; y++) {
            region.tiles[0][y].setStructure(StructureGenerator.getWall());
            region.tiles[Region.WIDTH - 1][y].setStructure(StructureGenerator.getWall());
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

    public static Region getStartingRegion() {
        Region region = RegionGenerator.getBlankRegion();

        Position startingRoom = RegionGenerator.placeRoom(region, 4, 4, 4, 25);

        Game.getPlayerController().getPlayer().placeMob(region, startingRoom.x + 2, startingRoom.y + 2);

        Position downStair = RegionGenerator.placeRoom(region, 4, 15, 4, 25);

        while (downStair == null || Position.getDistance(startingRoom, downStair) < 30) {
            downStair = RegionGenerator.placeRoom(region, 4, 15, 4, 25);
        }

        region.getTile(downStair.x + 1, downStair.y + 1).setStructure(StructureGenerator.getStairDown());

        for (int i = 0; i < 40; i++) {
            placeRoom(region, 6, 15, 4, 25);
        }

        for (int i = 0; i < 50; i++) {
            placeWall(region, 4, 10, 25);
        }

        placeRandomMobs(region, 10, 1);

        placeRandomItems(region, 5);

        region.getTile(startingRoom).addItem(ItemGenerator.getRandomMelee());

        return region;
    }

    public static Region generateRegion(Position upStair, int level, int turn) {
        Region region = getBlankRegion();
        region.lastUpdateTurn = turn;

        region.getTile(upStair).setStructure(StructureGenerator.getStairUp());

        Position downStair = RegionGenerator.placeRoom(region, 4, 15, 4, 25);

        while (downStair == null || Position.getDistance(upStair, downStair) < 30) {
            downStair = RegionGenerator.placeRoom(region, 4, 15, 4, 25);
        }

        region.getTile(downStair.x + 1, downStair.y + 1).setStructure(StructureGenerator.getStairDown());


        for (int i = 0; i < 40; i++) {
            placeRoom(region, 6, 15, 4, 25);
        }

        for (int i = 0; i < 50; i++) {
            placeWall(region, 4, 10, 25);
        }

        placeRandomMobs(region, 10, level);

        placeRandomItems(region, 5);

        return region;
    }

    public static void placeRandomItems(Region region, int count) {
        for (int i = 0; i < count; i++) {
            while (true) {
                int randomX = randomGen.nextInt(Region.WIDTH);
                int randomY = randomGen.nextInt(Region.HEIGHT);

                Tile spawn = region.getTile(randomX, randomY);

                if (!spawn.hasStatus(Status.BLOCK_PASSING)) {
                    region.getTile(randomX, randomY).addItem(ItemGenerator.getRandomMelee());
                    break;
                }
            }
        }
    }

    public static void placeRandomMobs(Region region, int count, int level) {
        for (int i = 0; i < count; i++) {
            while (true) {
                int randomX = randomGen.nextInt(Region.WIDTH);
                int randomY = randomGen.nextInt(Region.HEIGHT);

                Tile spawn = region.getTile(randomX, randomY);

                if (!spawn.hasStatus(Status.BLOCK_PASSING) && spawn.getMob() == null) {
                    MobGenerator.getRandomAngryMob(level).placeMob(region, randomX, randomY);
                    break;
                }
            }
        }
    }

    public static void placeWall(Region region, int minSize, int maxSize, int maxTries) {

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
                        region.getTile(iX, randomY).setStructure(StructureGenerator.getWall());
                    }
                    break;
                case 2:
                case 3:
                    for (int iY = randomY; iY < randomY + width; iY++) {
                        region.getTile(randomX, iY).setStructure(StructureGenerator.getWall());
                    }
                    break;
                default:
                    for (int iX = randomX; iX < randomX + width; iX++) {
                        for (int iY = randomY; iY < randomY + width; iY++) {
                            if (iX == randomX + width/2 || iY == randomY + width/2) {
                                region.getTile(iX, iY).setStructure(StructureGenerator.getWall());
                            }
                        }
                    }
            }



            return;

        }
    }

    public static Position placeRoom(Region region, int minSize, int maxSize, int maxDoors, int maxTries) {

        for (int i = 0; i < maxTries; i++) {

            int width;
            int height;

            if (minSize == maxSize) {
                width = minSize;
                height = minSize;
            } else {
                width = randomGen.nextInt(maxSize - minSize) + minSize;
                height = randomGen.nextInt(maxSize - minSize) + minSize;
            }


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
                            region.getTile(iX, iY).setStructure(StructureGenerator.getDoor());
                            randomOffset = doorOffset;
                            doorNum--;
                        } else {
                            region.getTile(iX, iY).setStructure(StructureGenerator.getWall());
                            randomOffset--;
                        }
                    }
                }
            }

            return new Position(randomX, randomY);

        }

        return null;
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
