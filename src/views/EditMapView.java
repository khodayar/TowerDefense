package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.GameGrid;

/**
 * This class is the main user interface view used to edit a game map. It implements the Observer interface to get
 * informed of changes in the edited game grid.
 *
 * @author Team 6
 *
 */
public class EditMapView<T extends ActionListener & MouseListener> {

    public JFrame frame;
    public JButton saveButton;
    public JButton startPointButton;
    public JButton endPointButton;
    public JButton[][] tiles;

    /**
     * Constructs the EditMapView object.
     *
     * @param gameGrid GameGrid object the EditMapView observes.
     * @param controller The controller receiving the user inputs.
     *
     */
    public EditMapView(GameGrid gameGrid, T controller) {

        final int row = gameGrid.getCases().length;
        final int col = gameGrid.getCases()[0].length;

        this.frame = new JFrame("Create or Edit map");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setBounds(100, 100, 400 * col / 10, 400 * row / 10 + 100);

        // mainPane contains all the other panels
        JPanel mainPane = new JPanel();
        mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPane.setLayout(new BorderLayout(0, 0));
        this.frame.setContentPane(mainPane);

        JPanel keys = new JPanel();
        keys.setBackground(Color.DARK_GRAY);

        mainPane.add(keys, BorderLayout.SOUTH);

        this.saveButton = new JButton("save");
        this.saveButton.addActionListener(controller);
        keys.add(this.saveButton);

        this.startPointButton = new JButton("Start Point");
        this.startPointButton.setBackground(Color.WHITE);
        this.startPointButton.addMouseListener(controller);
        keys.add(this.startPointButton);

        this.endPointButton = new JButton("Finish Point");
        this.endPointButton.setBackground(Color.WHITE);
        this.endPointButton.addMouseListener(controller);
        keys.add(this.endPointButton);

        JPanel map = new JPanel(new GridLayout(row, col, 2, 2));
        mainPane.add(map, BorderLayout.CENTER);

        this.tiles = new JButton[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                String iconPath = GameGrid.CASE_TYPES_ICON_PATHS[gameGrid.getCases()[i][j].ordinal()];
                ImageIcon tileIcon = new ImageIcon(iconPath);

                this.tiles[i][j] = new JButton();
                this.tiles[i][j].setIcon(tileIcon);
                this.tiles[i][j].setBorderPainted(false);
                this.tiles[i][j].setContentAreaFilled(false);
                this.tiles[i][j].setFocusPainted(false);

                // action listener for tiles for changing them to path
                this.tiles[i][j].addActionListener(controller);

                map.add(this.tiles[i][j]);

            }
        }

        frame.setVisible(true);

    }

    /**
     * Displays the EditMapView object.
     */
    public void show() {
        this.frame.setVisible(true);
    }

    /**
     * Shows a message to the user using the EditMapView.
     *
     * @param message Message to show to the user.
     * @param title Title of the message to show.
     *
     */
    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

}
