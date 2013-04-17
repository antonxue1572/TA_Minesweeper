package minesweeper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Minesweeper {

    
    // Constants
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    
    // Frame components
    private JFrame frame;
    private Container pane;
    private Insets insets;
    private JPanel gamePanel;
    private JLabel mineCounter;
    private JLabel mineHit;
    private JButton newGame;
    
    // Tile containers
    private MTiles [] tilesArr = new MTiles[100];
    private MTiles [] coverArr = new MTiles[100];
    
    // Variables
    private int totalMineCount = 0;
    
    // Constructor; Initialization
    public Minesweeper() {
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
        frame = new JFrame("Minesweeper");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setResizable(false);
        pane = frame.getContentPane();
        pane.setLayout(null);
        insets = pane.getInsets();
        
        gamePanel = new JPanel(null);
        gamePanel.setBounds(insets.left + 42, insets.top + 10, 210, 210);
        gamePanel.setBackground(Color.black);
        pane.add(gamePanel); 
        
        mineHit = new JLabel("You hit a mine!");
        mineHit.setBounds(insets.left + 100, insets.top + 230, mineHit.getPreferredSize().width, mineHit.getPreferredSize().height);
        mineHit.setVisible(false);
        pane.add(mineHit);
        
        newGame = new JButton("New Game");
        newGame.setBounds(insets.left + 200, insets.top + 230, newGame.getPreferredSize().width, newGame.getPreferredSize().height);
        newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                new Minesweeper();
                updateFrame();
            }
        
        });
        pane.add(newGame);
        
        initDefaultTiles();
        initMineTiles();
        initNumTiles();
        setTileBounds();
        initCoverTiles();
        addTiles();
        
        mineCounter = new JLabel("Total mines: " + getMineCount());
        mineCounter.setBounds(insets.left + 10, insets.top + 230, mineCounter.getPreferredSize().width, mineCounter.getPreferredSize().height);
        pane.add(mineCounter);
        
        frame.setVisible(true);
        updateFrame();
        System.out.println("Finished initialization");
    }
    
    private void updateFrame() {
        pane.repaint();
    }
    
    private void addTiles() {
        System.out.println("Adding tiles");
        for(int i = 0; i < 100; i++) {
            gamePanel.add(coverArr[i]);
        }
        
        for(int i = 0; i < 100; i++) {
            gamePanel.add(tilesArr[i]);
        }
    }
    
    private void initDefaultTiles() {
        System.out.println("Initializing default tiles");
        int mIndex = 0;
        for(int i = 0; i < 190; i += 21) {
            for(int j = 0; j < 190; j += 21, mIndex++) {
                try {
                    tilesArr[mIndex] = new MTiles(0, j, i);
                } catch (Exception e) {
                    System.out.println("Failed to initialize default tiles. \n" + e);
                    System.exit(0);
                }
            }
        }
    }
    
    private void initMineTiles() {
        System.out.println("Initializing mine tiles");
        for(int i = 0; i < 100; i++) {
            if(((int)(Math.random()*1000 % 10)) == 9) {
                try {
                    tilesArr[i] = new MTiles(9, tilesArr[i].getX(), tilesArr[i].getY());
                    tilesArr[i].addMouseListener(new MouseListener(){

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("You clicked a mine!");
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                        }
                    
                    });
                    totalMineCount++;
                } catch (IOException e) {
                    System.out.println("Failed to init mine tiles. \n" + e);
                    System.exit(0);
                }
            }
        }
    }
    
    private void initNumTiles() {
        System.out.println("Initializing numbered tiles");
        for(int i = 0; i < 100; i++) {
            if(tilesArr[i].getMineCount() != 9) {
                try {
                    tilesArr[i] = new MTiles(scanMines(i), tilesArr[i].getX(), tilesArr[i].getY());
                } catch(IOException e) {
                    System.out.println("Failed to init numbered tiles. \n" + e);
                    System.exit(0);
                }
            }
        }
    }
    
    private void initCoverTiles() {
        int mIndex = 0;
        System.out.println("Initializing covered tiles");
        for(int i = 0; i < 190; i += 21) {
            for(int j = 0; j < 190; j += 21, mIndex++) {
                try{
                    final MTiles cover = new MTiles(-1, j, i);
                    cover.setBounds(gamePanel.getInsets().left + j, gamePanel.getInsets().top + i, 20, 20);
                    if(tilesArr[mIndex].getMineCount() != 9) {
                        cover.addMouseListener(new MouseListener(){

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            gamePanel.remove(cover);
                            updateFrame();
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                        }
                    
                    });
                    } else{
                        cover.addMouseListener(new MouseListener(){

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                gamePanel.remove(cover);
                                mineHit.setVisible(true);
                                updateFrame();
                                System.out.println("You clicked on a mine!");
                            }

                            @Override
                            public void mousePressed(MouseEvent e) {
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                            }
                        
                        });
                    }
                    coverArr[mIndex] = cover;
                } catch(IOException e) {
                    System.out.println("Failed to init cover tiles. \n" + e);
                    System.exit(0);
                }
            }
        }
    }
    
    private void setTileBounds() {
        int mIndex = 0;
        System.out.println("Initializing tile bounds");
        for(int i = 0; i < 190; i += 21) {
            for(int j = 0; j < 190; j += 21, mIndex++) {
                tilesArr[mIndex].setBounds(gamePanel.getInsets().left + tilesArr[mIndex].getX(), gamePanel.getInsets().top + tilesArr[mIndex].getY(), 20, 20);
            }
        }
    }
    
    private int scanMines(int i) {
        // System.out.println("Scanning for mines");
        int mineCount = 0;
        if((i - 11) >= 0) {
            if(tilesArr[i-11].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i - 10) >= 0) {
            if(tilesArr[i-10].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i - 9) >= 0) {
            if(tilesArr[i-9].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i - 1) >= 0) {
            if(tilesArr[i-1].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i + 1) <= 99) {
            if(tilesArr[i+1].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i + 9) <= 99) {
            if(tilesArr[i+9].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i + 10) <= 99) {
            if(tilesArr[i+10].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        if((i + 11) <= 99) {
            if(tilesArr[i+11].getMineCount() == 9) {
                mineCount++;
            }
        }
        
        return mineCount;
    }
    
    private int getMineCount() {
        return totalMineCount;
    }
    
    public static void main(String[] args) {
        Minesweeper mine = new Minesweeper();
    }
}
