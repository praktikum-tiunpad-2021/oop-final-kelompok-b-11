package fifteenpuzzle;

import java.awt.*;
import java.util.Random;

public class Puzzle{
    private int size;
    private int dimension;
    private int margin;

    private int sizeTile;
    private int sizeAllTile;
    private int[] value;
    private int numberOfTile;

    public Puzzle(int size){
        this.size = size;
        this.dimension = 460;
        this.margin = 30;
        this.numberOfTile = (this.size * this.size) - 1;
        this.value = new int[this.numberOfTile + 1];
        this.sizeAllTile = (this.dimension - (2 * this.margin));
        this.sizeTile = this.sizeAllTile / this.size;
        newGame(); //Method buat start new game
    }

    public void drawGrid(){
        for (int i=0; i < this.value.length; i++){
            this.value[i] = i;
        }

        while (!isSolvable(this.value)){
            shuffle(this.value); //Method selama arraynya belum solvable, arraynya dishuffle terus
        }
    }
}