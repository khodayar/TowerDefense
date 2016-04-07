package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.GameController;
import model.Critter;
import model.Game;
import model.GameGrid;
import model.GameScore;
import model.GridLocation;
import model.strategy.AttackStrategyFactory;
import model.tower.Tower;

/**
 * This class is the main user interface view used to play the game. It implements the Observer interface to get
 * informed of changes in the Game class objects.
 *
 * @author Team 6
 *
 */
public class GameView implements Observer {

    /**
     * Amount of time, in milliseconds, to show the attacking effects on the critters.
     */
    public static int ATTACK_EFFECTS_DELAY = 15;

    public ArrayList<JLabel> towerLabels;
    public JButton playButton;
    public JButton saveButton;
    public JButton sellTowerButton;
    public JButton upgradeTowerButton;
    public JComboBox<String> strategyComboBox;
    public Tower selectedTower;
    public Critter selectedCritter;

    private JButton[][] tiles;
    private JFrame gameFrame;
    private JLabel cashLabel;
    private JLabel lifeLabel;
    private JFrame towerInspectionFrame;
    private JFrame critterInspectionFrame;
    private JLabel crittersKilledLabel;
    private JLabel waveLabel;

    private GameController gameController;


    /**
     * Constructs the GameView object.
     *
     * @param game Game object the GameView observes.
     * @param controller The controller receiving the user inputs.
     *
     */
    public GameView(Game game, GameController controller) {

        int row = game.grid.getCases().length;
        int col = game.grid.getCases()[0].length;

        this.gameFrame = new JFrame("Tower defense game");
        this.gameController = controller;
        this.towerInspectionFrame = new JFrame("Tower Inspection");
        this.towerInspectionFrame.setBounds(700 + 530 * col / 10, 400, 500, 500);

        this.critterInspectionFrame = new JFrame("Critter Inspection");
        this.critterInspectionFrame.setBounds(450 + 530 * col / 10, 160, 230, 100);

        // mainPane to add all other panels
        JPanel mainPane = new JPanel();
        mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPane.setLayout(new BorderLayout(0, 0));
        this.gameFrame.setContentPane(mainPane);

        JPanel map = new JPanel(new GridLayout(row, col, 0, 0));
        this.tiles = new JButton[row][col];

        for (int i = 0; i < row * col; i++) {

            this.tiles[i / col][i % col] = new JButton();
            this.tiles[i / col][i % col].setContentAreaFilled(false);
            this.tiles[i / col][i % col].setFocusPainted(false);
            this.tiles[i / col][i % col].setOpaque(false);
            this.tiles[i / col][i % col].setBorderPainted(false);

            int caseTypeOrdinal = game.grid.getCases()[i / col][i % col].ordinal();
            String iconPath = GameGrid.CASE_TYPES_ICON_PATHS[caseTypeOrdinal];
            this.tiles[i / col][i % col].setIcon(new ImageIcon(iconPath));

            this.tiles[i / col][i % col].addMouseListener(controller);

            map.add(this.tiles[i / col][i % col]);
        }

        mainPane.add(map);

        this.gameFrame.setSize(530 * col / 10, 680 * row / 10);

        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Area where towers are displayed
        JPanel towerSelectionArea = new JPanel();
        towerSelectionArea.setBackground(Color.DARK_GRAY);
        mainPane.add(towerSelectionArea, BorderLayout.NORTH);
        JLabel towerSelectionText = new JLabel("Towers");
        towerSelectionText.setForeground(Color.white);
        towerSelectionArea.add(towerSelectionText);

        this.towerLabels = new ArrayList<JLabel>();

        // Adding towers and their click listeners
        for (int i = 0; i < Game.AVAILABLE_TOWERS.length; i++) {

            Tower tower = Game.AVAILABLE_TOWERS[i];
            ImageIcon towerIcon = new ImageIcon(tower.getIconPath());
            JLabel towerLabel = new JLabel(towerIcon);

            this.towerLabels.add(towerLabel);
            towerSelectionArea.add(towerLabel);
            towerLabel.addMouseListener(controller);

        }

        // Critters killed, Health points and money panel.
        JPanel healthBankPanel = new JPanel();
        healthBankPanel.setBackground(Color.DARK_GRAY);
        mainPane.add(healthBankPanel, BorderLayout.SOUTH);

        this.waveLabel = new JLabel("Wave: " + game.getWave());
        this.waveLabel.setForeground(Color.green);
        healthBankPanel.add(this.waveLabel);

        // Critters image
        JLabel crittersImgLabel = new JLabel(new ImageIcon(Critter.ICON_PATH));
        healthBankPanel.add(crittersImgLabel);
        this.crittersKilledLabel = new JLabel("" + game.getKilledCritters());
        this.crittersKilledLabel.setForeground(Color.green);
        healthBankPanel.add(this.crittersKilledLabel);

        // Bank image
        JLabel bankImgLabel = new JLabel(new ImageIcon("icons/bank_icon.png"));
        healthBankPanel.add(bankImgLabel);
        this.cashLabel = new JLabel("$" + game.getMoney());
        this.cashLabel.setForeground(Color.green);
        healthBankPanel.add(this.cashLabel);

        // Health image
        JLabel lifeImgLabel = new JLabel(new ImageIcon("icons/life_icon.png"));
        healthBankPanel.add(lifeImgLabel);
        this.lifeLabel = new JLabel("" + game.getLives());
        this.lifeLabel.setForeground(Color.green);
        healthBankPanel.add(this.lifeLabel);

        this.playButton = new JButton("play");
        playButton.addMouseListener(controller);
        healthBankPanel.add(playButton);

        this.saveButton = new JButton("save");
        saveButton.addMouseListener(controller);
        healthBankPanel.add(saveButton);

        this.gameFrame.setResizable(false);

        JOptionPane.showMessageDialog(null, GameScore.displayHighScores(game.grid.getGameScores()), "High scores.",
                        JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Displays the GameView object.
     */
    public void show() {
        this.gameFrame.setVisible(true);
    }

    /**
     * After it is instantiated, the view should only be updated using this method. The view should know what to look
     * for in the Game object in order to update it's representation.
     */
    @Override
    public void update(Observable observable, Object object) {
        Game game = (Game) observable;

        for (GridLocation attackLocation : game.attackedCritters) {
            this.tiles[attackLocation.x][attackLocation.y].setIcon(new ImageIcon("icons/fire.png"));
            try {
                Thread.sleep(GameView.ATTACK_EFFECTS_DELAY);
            } catch (InterruptedException e) {
                System.out.println("This can happen if we interrupt the thread in the game.");
            }
        }

        // For now, we only update the locations of the towers.
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                if (game.hasTower(i, j)) {
                    this.placeTower(i, j, game.getTower(i, j));
                }

                if (game.hasCritter(new GridLocation(i, j))) {
                    this.placeCritter(i, j);
                }

                if (game.noCritter(new GridLocation(i, j))) {
                    this.removeCritter(i, j);
                }

            }
        }

        this.cashLabel.setText("$" + game.getMoney());
        this.lifeLabel.setText("" + game.getLives());
        this.crittersKilledLabel.setText("" + game.getKilledCritters());
        this.waveLabel.setText("Wave: " + game.getWave());

        if (this.selectedCritter != null) {
            this.showCritterDetails(this.selectedCritter);
        }

        if (this.selectedTower != null && this.critterInspectionFrame.isVisible()) {
            this.showTowerDetails(this.selectedTower);
        }

        if (game.isOver()) {
            JOptionPane.showMessageDialog(null, "Sorry, you lost. Please try again.", "Game Over.",
                            JOptionPane.INFORMATION_MESSAGE);
            this.gameFrame.setVisible(false);
        } else if (game.isWon()) {
            JOptionPane.showMessageDialog(null, "You won the game!", "Congratulations!",
                            JOptionPane.INFORMATION_MESSAGE);
            this.gameFrame.setVisible(false);
        }
    }

    /**
     * Removes a critter form the specified location.
     *
     * @param line Line from where to remove the critter.
     * @param column Column from where to remove the critter.
     */
    public void removeCritter(int line, int column) {
        this.tiles[line][column].setIcon(new ImageIcon("icons/road.jpg"));

    }

    /**
     * Places a critter at the specified location.
     *
     * @param line Line where to place the critter.
     * @param column Column where to place the critter.
     */
    private void placeCritter(int line, int column) {
        this.tiles[line][column].setIcon(new ImageIcon(Critter.ICON_PATH));

    }

    /**
     * Places the selected tower on the game grid.
     *
     * @param line Line of selected tile.
     * @param column Column of the selected tile
     * @param tower Tower object to place on the grid.
     *
     */
    private void placeTower(int line, int column, Tower tower) {
        this.tiles[line][column].setBackground(new Color(45, 111, 1));
        this.tiles[line][column].setOpaque(true);
        this.tiles[line][column].setIcon(new ImageIcon(tower.getIconPath()));
    }

    /**
     * Removes a tower from the game grid.
     *
     * @param line Line of selected tile.
     * @param column Column of the selected tile
     */
    public void removeTower(int line, int column) {
        this.tiles[line][column].setIcon(new ImageIcon(GameGrid.CASE_TYPES_ICON_PATHS[0]));
    }

    /**
     * Returns the (x,y) coordinates of the specified button.
     *
     * @param button Game grid tile on which user has clicked.
     *
     * @return The Coordinates of the button clicked.
     */
    public GridLocation getButtonLocation(JButton button) {
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                if (this.tiles[i][j] == button) {
                    return new GridLocation(i, j);
                }
            }
        }
        return null;
    }

    /**
     *
     * Shows the details of the critter in the critter inspection panel.
     *
     * @param critter critter to get the details from.
     */
    public void showCritterDetails(final Critter critter) {

        // Open new window for tower inspection.
        JPanel critterInspectionPanel = new JPanel();
        critterInspectionPanel.setBackground(Color.DARK_GRAY);
        this.critterInspectionFrame.setContentPane(critterInspectionPanel);

        JPanel critterImagePanel = new JPanel();
        critterInspectionPanel.add(critterImagePanel, BorderLayout.NORTH);
        JLabel towerImage = new JLabel(new ImageIcon(Critter.ICON_PATH));
        towerImage.setBackground(Color.DARK_GRAY);
        critterImagePanel.setBackground(Color.DARK_GRAY);
        critterImagePanel.add(towerImage);

        // Tower Details
        JPanel critterDetailsPanel = new JPanel();
        critterDetailsPanel.setBackground(Color.DARK_GRAY);
        critterDetailsPanel.setLayout(new GridLayout(0, 2));
        critterInspectionPanel.add(critterDetailsPanel, BorderLayout.SOUTH);

        // Critter Level
        JLabel critterLevelTxt = new JLabel("Level: ");
        critterLevelTxt.setForeground(Color.white);
        critterDetailsPanel.add(critterLevelTxt);
        JLabel critterLevel = new JLabel(Integer.toString(critter.getLevel()));
        critterLevel.setForeground(Color.white);
        critterDetailsPanel.add(critterLevel);

        // Critter health
        JLabel critterHealthTxt = new JLabel("Health: ");
        critterHealthTxt.setForeground(Color.white);
        critterDetailsPanel.add(critterHealthTxt);
        JLabel critterHealth = new JLabel(Integer.toString(critter.getHealthPoints()));
        critterHealth.setForeground(Color.white);
        critterDetailsPanel.add(critterHealth);

        // Critter health
        JLabel critterSpeedTxt = new JLabel("Speed: ");
        critterSpeedTxt.setForeground(Color.white);
        critterDetailsPanel.add(critterSpeedTxt);
        JLabel critterSpeed = new JLabel(Integer.toString(critter.getSpeed()));
        critterSpeed.setForeground(Color.white);
        critterDetailsPanel.add(critterSpeed);

        // Critter health
        JLabel critterFrozenTxt = new JLabel("Is frozen: ");
        critterFrozenTxt.setForeground(Color.white);
        critterDetailsPanel.add(critterFrozenTxt);
        JLabel critterFrozen = new JLabel(Boolean.toString(critter.isFrozen()));
        critterFrozen.setForeground(Color.white);
        critterDetailsPanel.add(critterFrozen);

        critterInspectionFrame.setVisible(true);
    }

    /**
     *
     * Shows the details of the tower in the tower inspection panel.
     *
     * @param tower Tower to get the details from.
     */
    public void showTowerDetails(final Tower tower) {

        boolean placedOnTile = (tower.getLocation() != null);

        // Open new window for tower inspection.
        JPanel towerInspectionPanel = new JPanel();
        towerInspectionPanel.setBackground(Color.DARK_GRAY);
        towerInspectionPanel.setSize(100, 250);
        this.towerInspectionFrame.setContentPane(towerInspectionPanel);

        // Tower Image Sell Tower Button and Upgrade Tower Button.
        JPanel towerImagePanel = new JPanel();
        towerInspectionPanel.add(towerImagePanel, BorderLayout.NORTH);
        JLabel towerImage = new JLabel(new ImageIcon(tower.getIconPath()));
        towerImage.setBackground(Color.DARK_GRAY);
        towerImagePanel.setBackground(Color.DARK_GRAY);
        towerImagePanel.add(towerImage);
        if (placedOnTile) {

            this.sellTowerButton = new JButton();
            this.sellTowerButton.setBackground(Color.white);
            this.sellTowerButton.setText("Sell Tower");
            this.sellTowerButton.addActionListener(this.gameController);
            towerImagePanel.add(this.sellTowerButton);

            this.upgradeTowerButton = new JButton();
            this.upgradeTowerButton.setBackground(Color.white);
            this.upgradeTowerButton.setText("Upgrade Tower");
            this.upgradeTowerButton.addActionListener(this.gameController);
            towerImagePanel.add(this.upgradeTowerButton);
        }

        // Tower Details
        JPanel towerDetailsPanel = new JPanel();
        towerDetailsPanel.setBackground(Color.DARK_GRAY);
        towerDetailsPanel.setLayout(new GridLayout(0, 2));
        towerInspectionPanel.add(towerDetailsPanel, BorderLayout.SOUTH);

        // Tower Name
        JLabel towerNameTxt = new JLabel("Name: ");
        towerNameTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerNameTxt);
        JLabel towerName = new JLabel(tower.getName());
        towerName.setForeground(Color.white);
        towerDetailsPanel.add(towerName);

        // Tower Level
        JLabel towerLevelTxt = new JLabel("Level: ");
        towerLevelTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerLevelTxt);
        JLabel towerLevel = new JLabel(Integer.toString(tower.getLevel()));
        towerLevel.setForeground(Color.white);
        towerDetailsPanel.add(towerLevel);

        // Tower Initial Cost
        JLabel towerInitCostTxt = new JLabel("Initial Cost: ");
        towerInitCostTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerInitCostTxt);
        JLabel towerInitCost = new JLabel(Integer.toString(tower.getInitialCost()));
        towerInitCost.setForeground(Color.white);
        towerDetailsPanel.add(towerInitCost);

        // Tower Increase Level Cost
        JLabel towerCostLevelTxt = new JLabel("Cost to increase level: ");
        towerCostLevelTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerCostLevelTxt);
        JLabel towerCostLevel = new JLabel(Integer.toString(tower.getLevelCost()));
        towerCostLevel.setForeground(Color.white);
        towerDetailsPanel.add(towerCostLevel);

        // Tower Range
        JLabel towerRangeTxt = new JLabel("Range: ");
        towerRangeTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerRangeTxt);
        JLabel towerRange = new JLabel(Integer.toString(tower.getRange()));
        towerRange.setForeground(Color.white);
        towerDetailsPanel.add(towerRange);

        // Tower Power
        JLabel towerPowerTxt = new JLabel("Power: ");
        towerPowerTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerPowerTxt);
        JLabel towerPower = new JLabel(Integer.toString(tower.getPower()));
        towerPower.setForeground(Color.white);
        towerDetailsPanel.add(towerPower);

        // Tower Rate of Fire
        JLabel towerRateFireTxt = new JLabel("Rate of fire: ");
        towerRateFireTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerRateFireTxt);
        JLabel towerRateFire = new JLabel(Integer.toString(tower.getRateOfFire()));
        towerRateFire.setForeground(Color.white);
        towerDetailsPanel.add(towerRateFire);

        // Tower Special Effects
        JLabel towerSpecialEffectsTxt = new JLabel("Special Effects: ");
        towerSpecialEffectsTxt.setForeground(Color.white);
        towerDetailsPanel.add(towerSpecialEffectsTxt);
        JLabel towerSpecialEffects = new JLabel(tower.getSpecialEffect());
        towerSpecialEffects.setForeground(Color.white);
        towerDetailsPanel.add(towerSpecialEffects);

        if (placedOnTile) {

            JLabel refundAmountTxt = new JLabel("Refund Amount: ");
            refundAmountTxt.setForeground(Color.white);
            towerDetailsPanel.add(refundAmountTxt);
            JLabel refundAmount = new JLabel(Integer.toString(tower.refundAmout()));
            refundAmount.setForeground(Color.white);
            towerDetailsPanel.add(refundAmount);

            JLabel strategyTxt = new JLabel("Attack Strategy: ");
            strategyTxt.setForeground(Color.white);
            towerDetailsPanel.add(strategyTxt);
            this.strategyComboBox = new JComboBox<String>(AttackStrategyFactory.getAvailableStrategies());
            this.strategyComboBox.setPreferredSize(new Dimension(10, 20));
            this.strategyComboBox.setSelectedItem(tower.getAttackStrategy().getName());;
            this.strategyComboBox.addActionListener(this.gameController);
            towerDetailsPanel.add(this.strategyComboBox);

            JLabel space1 = new JLabel("");
            JLabel space2 = new JLabel("");
            towerDetailsPanel.add(space1);
            towerDetailsPanel.add(space2);

            for (String log : tower.getLogs()) {
                // Tower Log
                JLabel towerLogtxt = new JLabel("Log: ");
                towerLogtxt.setForeground(Color.white);
                towerDetailsPanel.add(towerLogtxt);
                JLabel towrLog = new JLabel(log);
                towrLog.setForeground(Color.white);
                towerDetailsPanel.add(towrLog);
            }

        }

        towerInspectionFrame.setVisible(true);
    }

    /**
     * Closes the tower details window.
     */
    public void closeTowerDetails() {
        this.towerInspectionFrame.setVisible(false);
    }

}
