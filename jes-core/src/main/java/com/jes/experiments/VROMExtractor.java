package com.jes.experiments;

import com.jes.experiments.Tile;
import com.jes.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-17.
 */
public class VROMExtractor {
    private byte[] bank;

    public VROMExtractor() {
        bank = null;
    }

    public void setBank(byte[] bank) {
        this.bank = bank;
    }

    public List<Tile> extract() {
        int[] chA, chB;
        List<Tile> result = new ArrayList<Tile>();

        for(int i=0; i<bank.length; i+=16) {
            Tile tile = new Tile();
            for(int j=0; j<8; j++) {
                chA = CommonUtils.getByteArray(bank[i + j], 8);
                chB = CommonUtils.getByteArray(bank[i + j + 8], 8);

                Integer[] cmp = compose(chA, chB);
                tile.getTile().add(cmp);
            }
            result.add(tile);
        }

        return result;
    }

    private  Integer[] compose(int[] chA, int[] chB) {
        Integer[] result = new Integer[chA.length];

        for(int i=0; i<chA.length; i++) {
            if(chB[i] == 1) {
                chB[i] = 2;
            }

            result[i] = chA[i] + chB[i];
        }

        return result;
    }
}
