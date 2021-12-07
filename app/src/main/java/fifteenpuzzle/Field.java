package fifteenpuzzle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static fifteenpuzzle.Tile.TILE_SIZE;

class Field extends StackPane {
    private Tile[][] grid;
    private static final int TRANSITION_SPEED = 75;
    private static final boolean DEBUG = false;
    private int width;
    private int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        newGame();

        // Pengecekan apakah sudah selesai
        check();
    }
    
    public void newGame(){
        getChildren().clear();
        // Membuat grid dengan tile
        grid = new Tile[width][height];

        // Membuat posisi untuk tile kosong dalam grid
        Random random = new Random(53546346);
        int gap_x = DEBUG ? width - 2: random.nextInt(width);
        int gap_y = DEBUG ? height - 1: random.nextInt(height);

        // Mengisi ArrayList dengan angka
        ArrayList<String> nums = new ArrayList();
        for (int i = 1; i < width * height; i++) {
            nums.add(Integer.toString(i));
        }

        // Melakukan shuffle pada angka jika dibutuhkan
        if(!DEBUG) {
            do{
                Collections.shuffle(nums);
            }while(!this.isSolvable(nums));
        }

        int counter = 0;

        // Pada game field
        int size = width * height;
        for (int i = 0; i < size; i++) {
            // Membuat koordinat dua dimensi dari indeks satu dimensi
            int x = i % width;
            int y = i / height;

            // Mengosongkan tile di koordinat yang ditentukan
            if (x == gap_x && y == gap_y) {
                grid[x][y] = null;
                continue;
            }

            // Mengambil angka-angka dari ArrayList dan membuat tilenya
            String number = nums.get(counter);
            Tile tile = new Tile(number);
            counter++;

            // Memasang tile di layar dan di grid
            tile.setPosition(x, y);
            grid[x][y] = tile;

            // Menambahkan ke layar
            getChildren().add(tile);
        }
    }

    private boolean isSolvable(ArrayList<String> nums){
        int inversions = 0;
        int size = nums.size();
        for(int i = 0; i < size; i++){
            for(int j = i + 1; j < size; j++){
                if(nums.get(i).compareTo(nums.get(j)) > 0){
                    inversions++;
                }
            }
        }
        return inversions % 2 == 0;
    }

    /**
     * Menggerakkan tiles sesuai dangan yang di klik
     * @param tile - tile yang di kllik
     */

    public void move(Tile tile) {
        int gap_x = -1;
        int gap_y = -1;

        // Mencari kolom kosong di grid
        for (int i = 0; i < this.width ; i++) {
            for (int j = 0; j < this.height; j++) {
                if(grid[i][j] == null) {
                    gap_x = i;
                    gap_y = j;
                }
            }

        }

        // Jika kolom kosong tidak ditemukan
        if(gap_x == -1 || gap_y == -1) {
            System.out.println("gap not found");
            return;
        }

        // Mengecek apakah tile yang diklik dengan kolom kosong memiliki kesamaan
        if(gap_x != tile.grid_x && gap_y != tile.grid_y) {
            System.out.println("Sorry, no gaps here.");
            return;
        }

        // Menghitung berapa banyak tilenya digeser dan ke arah mana tilenya digeser
        int shift_x = gap_x - tile.grid_x;
        int shift_y = gap_y - tile.grid_y;

        System.out.println("shift_x=" + shift_x + " shift_y=" + shift_y);

        // Membuat ArrayList tiles yang ingin diselesaikan
        ArrayList<Tile> tiles = new ArrayList<>();

        // Buat arahnya
        int dirX = shift_x == 0 ? 0 : shift_x / Math.abs(shift_x);
        int dirY = shift_y == 0 ? 0 : shift_y / Math.abs(shift_y);

        // Buat nilai seberapa banyak tilenya digerakkan
        int how_many = dirY == 0 ? Math.abs(shift_x) : Math.abs(shift_y);

        // Hitung banyaknya iterasi
        for(int i = 0; i != how_many; i++) {
            // Dapatkan koordinat
            int x = i * dirX + tile.grid_x;
            int y = i * dirY + tile.grid_y;

            // Ambil tile dengan koordinat tadi
            Tile t = grid[x][y];

            // Tambahkan tile ke list pada posisi pertama
            tiles.add(0, t);

            // Hanya menampilkan tile apa yang ditambahkan ke list
            System.out.println("tile " + t + " added to the list");
        }

        // Gerakkan tile yang dikumpulkan ke arah yang diberikan
        MoveXY(tiles, dirX, dirY);
    }

    /**
     * Method untuk menggerakkan list tile ke semua arah
     * @param tiles - List tile yang perlu digerakkan
     * @param dirX - arah horizontal
     * @param dirY - arah vertikal
     */
    private void MoveXY(ArrayList<Tile> tiles, int dirX, int dirY) {
        // Ambil angka iterasi
        int count = tiles.size();
        int counter = 0;

        // Lakukan for each pada ArrayList tile
        for (Tile t:tiles) {

            // Ambil koordinat lama dan hitung koordinat baru
            int old_x = t.grid_x;
            int new_x = t.grid_x + dirX;

            // Ambil koordinat lama dan hitung koordinat baru
            int old_y = t.grid_y;
            int new_y = t.grid_y + dirY;

            // Animasi untuk transisi tile
            TranslateTransition anim = new TranslateTransition();
            anim.setNode(t);
            anim.setToX(new_x * TILE_SIZE);
            anim.setToY(new_y * TILE_SIZE);
            anim.setInterpolator(Interpolator.LINEAR);
            anim.setDuration(new Duration(TRANSITION_SPEED));
            anim.play();

            int finalCounter = ++counter;

            // Ketika animasinya selesai
            anim.setOnFinished(e -> {
                // Menetapkan posisi baru
                t.setPosition(new_x, new_y);
                // Mengisi null pada posisi sebelumnya
                grid[old_x][old_y] = null;
                // Menetapkan posisi baru di grid
                grid[new_x][new_y] = t;

                // Mengecek apakah game sudah selesai
                if(count == finalCounter) {
                    check();
                }
            });
        }
    }

    public void check() {
        boolean win = false;
        int counter = 1;

        System.out.println("checking...");

        int size = width * height;
        for (int q = 0; q < size; q++) {
            int x = q % width;
            int y = q / height;
            Tile t = grid[x][y];

            if(t != null) {
                int value = Integer.valueOf(t.getText());

                if(value == counter) {
                    t.setColor(Color.ROYALBLUE);

                    if (counter == (size-1))
                    {
                        win = true;
                        System.out.println("CONGRATULATIONS! YOU ARE WIN");
                        break;
                    } else {
                        counter++;
                    }
                } else {
                    t.setColor(Color.AQUA);
                    break;
                }

            } else {
                for (int i = q; i < size; i++) {
                    int xt = i % width;
                    int yt = i / height;
                    Tile tl = grid[xt][yt];
                    if(tl != null) {
                        tl.setColor(Color.AQUA);
                    }
                }
                break;
            }
        }

        System.out.println("win : " + win);
    }

}