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

    public void shuffle(int[] value){
        Random random = new Random();
        int index1, index2;
        int temp;

        for (int i=0; i < this.numberOfTile * 10; i++){
            index1 = random.nextInt(this.numberOfTile);
            index2 = random.nextInt(this.numberOfTile);

            temp = value[index1];
            value[index1] = value[index2];
            value[index2] = temp;
        }
    }

    public boolean isSolvable(int[] value){
        int inversions = 0;
        int blank = 0;

        for (int i=0; i < this.numberOfTile; i++){
            if (value[i] == 0){
                blank = i;
            }
            else{
                for (int j=i+1; j < this.numberOfTile; j++){
                    if (value[i] > value[j]){
                        inversions++;
                    }
                }
            }
        }

        if (this.size % 2 == 0){
            if ((blank % this.size) % 2 == 0){
                if (inversions % 2 == 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                if (inversions % 2 == 1){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        else{
            if (inversions % 2 == 0){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void newGame(){
        this.value = new int[this.numberOfTile + 1];
        this.drawGrid();
    }

    
}