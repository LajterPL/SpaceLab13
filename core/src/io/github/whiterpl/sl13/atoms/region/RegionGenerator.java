package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.structure.Structure;

public class RegionGenerator {

    public static Region getTestingRegion() {
        Region region = new Region();
        Structure wall = new Structure("Wall", "Wall", '#', "9e9baaff");
        wall.addStatus(Status.BLOCK_PASSING);

        for (int x = 0; x < Region.WIDTH; x++) {
            region.tiles[x][0].setStructure(wall);
            region.tiles[x][Region.HEIGHT - 1].setStructure(wall);
        }

        for (int y = 0; y < Region.WIDTH; y++) {
            region.tiles[0][y].setStructure(wall);
            region.tiles[Region.WIDTH - 1][y].setStructure(wall);
        }

        return region;
    }

}
