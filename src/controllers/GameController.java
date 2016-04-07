package controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import model.Critter;
import model.Game;
import model.GameGrid;
import model.GameLog;
import model.GridLocation;
import model.tower.Tower;
import views.GameView;

/**
 * Main controller for listening to user inputs from the GameView.
 *
 * @author Team 6
 *
 */
public class GameController implements MouseListener, ActionListener {

    // must be private
    public Game game;
    private GameView gameView;
    private GameLog gameLog;

    /**
     * Constructs a new GameController object. Links the Game object to a GameView object using the Observer design
     * pattern.
     *
     * @param game Game object to use with the view object.
     *
     */
    public GameController(Game game) {
        this.game = game;
        this.gameView = new GameView(game, this);
        this.gameLog = new GameLog();
        this.game.addObserver(this.gameView);
        this.game.addObserver(this.gameLog);
        this.gameView.show();
        this.gameView.update(this.game, this.game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent event) {

        if (event.getSource() instanceof JButton
                && this.gameView.getButtonLocation((JButton) event.getSource()) != null) {
            JButton buttonClicked = (JButton) event.getSource();
            GridLocation clickLocation = this.gameView.getButtonLocation(buttonClicked);
            // System.out.println("This is in the click event");
            // System.out.print(clickLocation);
            GameGrid.CASE_TYPES caseType = this.game.grid.getCases()[clickLocation.x][clickLocation.y];
            if (this.game.hasCritter(clickLocation)) {
                Critter critter = this.game.critters.get(clickLocation);
                this.gameView.selectedCritter = critter;
                this.gameView.showCritterDetails(critter);
            } else if (caseType == GameGrid.CASE_TYPES.GRASS) {
                if (this.gameView.selectedTower == null && this.game.hasTower(clickLocation.x, clickLocation.y)) {
                    Tower tower = this.game.getTower(clickLocation.x, clickLocation.y);
                    this.gameView.selectedTower = tower;
                    this.gameView.showTowerDetails(tower);
                } else if (this.gameView.selectedTower != null) {
                    Point towerLocation = this.gameView.getButtonLocation(buttonClicked);
                    this.game.buyTower(this.gameView.selectedTower, towerLocation.x, towerLocation.y);
                    if (this.game.getTower(towerLocation.x, towerLocation.y) != null) {
                        this.gameView.selectedTower = this.game.getTower(towerLocation.x, towerLocation.y);
                        this.gameView.showTowerDetails(this.gameView.selectedTower);
                    }
                }
            }
            return;
        }

        // We refuse to listen to the user actions during a wave.
        if (event.getSource() != this.gameView.saveButton && this.game.isMakingTurn()) {
            return;
        }

        // The user clicked on one of the tower images.
        if (this.gameView.towerLabels.indexOf(event.getSource()) != -1) {
            int selectedTowerIndex = this.gameView.towerLabels.indexOf(event.getSource());
            this.gameView.selectedTower = Game.AVAILABLE_TOWERS[selectedTowerIndex];
            this.gameView.showTowerDetails(this.gameView.selectedTower);

        }

        // sends a wave of critters
        else if (event.getSource() == this.gameView.playButton) {
            this.game.sendWave();
            // System.out.print("play");
        }

        // calls the method "save"
        else if (event.getSource() == this.gameView.saveButton) {
            this.game.saveGame("newsavedgame");
            // System.out.print("save");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.gameView.strategyComboBox) {
            JComboBox<String> strategyCombo = (JComboBox<String>) e.getSource();
            String strategy=(String) strategyCombo.getSelectedItem();
            this.game.changeStrategyTower(strategy,this.gameView.selectedTower.getLocation().x,
                    this.gameView.selectedTower.getLocation().y);
        } else if (e.getSource() == this.gameView.sellTowerButton) {
            this.game.sellTower(this.gameView.selectedTower.getLocation().x,
                    this.gameView.selectedTower.getLocation().y);
            this.gameView.removeTower(this.gameView.selectedTower.getLocation().x,
                    this.gameView.selectedTower.getLocation().y);
            this.gameView.closeTowerDetails();
        } else if (e.getSource() == this.gameView.upgradeTowerButton) {
            game.upgradeTower(this.gameView.selectedTower.getLocation().x, this.gameView.selectedTower.getLocation().y);
            this.gameView.showTowerDetails(this.gameView.selectedTower);
        }

    }


}
