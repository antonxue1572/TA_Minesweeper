package minesweeper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MTiles extends JLabel {
    
    private BufferedImage tileImage;
    private JLabel tile;
    private int surroundingBombs;
    private int x;
    private int y;
    
    public MTiles(int newTileNumber, int newX, int newY) throws IOException {
        super(new ImageIcon(ImageIO.read(new File("src/Tiles/" + newTileNumber + ".png"))));
        this.surroundingBombs = newTileNumber;
        this.x = newX;
        this.y = newY;
        
    }
    
    

    public int getMineCount() {
        return this.surroundingBombs;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
}
