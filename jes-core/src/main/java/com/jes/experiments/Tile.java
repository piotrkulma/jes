package com.jes.experiments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-17.
 */
public class Tile {
    List<Integer[]> tile;

    public Tile() {
        tile = new ArrayList<Integer[]>();
    }

    public List<Integer[]> getTile() {
        return tile;
    }
}
