package fifteenpuzzle;

import java.awt.*;
import java.util.Random;

public class Puzzle{
    private int size;
    private int dimension = 550;
    private int sizeTile;
    private int sizeAllTile;
    private int[] value;
    private int numberOfTile;
    private int numberOfMove;
    private boolean isSolved;

    public Puzzle(int size){
        this.size = size;
        this.dimension = 460;
        // this.margin = 30;
        this.numberOfTile = (this.size * this.size) - 1;
        this.value = new int[this.numberOfTile + 1];
        // this.sizeAllTile = (this.dimension - (2 * this.margin));
        // this.sizeTile = this.sizeAllTile / this.size;
        this.initGrid();
        newGame(); //Method buat start new game
    }

    public int getValue(int x, int y){
        return this.value[x*this.size + y];
    }

    public void initGrid(){
        for (int i=0; i < this.value.length; i++){
            this.value[i] = i;
        }

        do { 
            shuffle(this.value); //Method buat shuffle
        } while (!isSolvable(this.value)); //Method selama arraynya belum solvable, arraynya dishuffle terus
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
        this.value = new int[this.numberOfTile + 1]; //Mengisi array dengan 0
        // this.drawGrid(); 
    }

    public void isSolved(){ // solved ketika urutannya = 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0

        // temp = duplikasi dari array value
        int[] temp = new int[this.numberOfTile + 1];
        for (int i=0; i < temp.length; i++){
            temp[i] = this.value[i];
        }
        
        for (int i=0; i < temp.length-1; i++){ // ngecek  urutan value[i] = i+1 dari 1->15 (0 tidak dihitung)
            if (temp[i] != i+1){ 
                this.isSolved = false;
                break;
            }
            else{
                this.isSolved = true;
            }
        }

        
    }

    public void move(int direction){
        int blank = 0;
        int temp;

        for (int i=0; i < this.value.length; i++){
            if (this.value[i] == 0){
                blank = i;
            }
        }

        if (direction == 1){ //up
            if (blank >= this.size){
                temp = this.value[blank];
                this.value[blank] = this.value[blank - this.size];
                this.value[blank - this.size] = temp;
                this.numberOfMove++;
            }
        }
        else if (direction == 2){ //down
            if (blank < this.numberOfTile - this.size){
                temp = this.value[blank];
                this.value[blank] = this.value[blank + this.size];
                this.value[blank + this.size] = temp;
                this.numberOfMove++;
            }
        }
        else if (direction == 3){ //left
            if (blank % this.size != 0){
                temp = this.value[blank];
                this.value[blank] = this.value[blank - 1];
                this.value[blank - 1] = temp;
                this.numberOfMove++;
            }
        }
        else if (direction == 4){ //right
            if ((blank + 1) % this.size != 0){
                temp = this.value[blank];
                this.value[blank] = this.value[blank + 1];
                this.value[blank + 1] = temp;
                this.numberOfMove++;
            }
        }
    }
    
}