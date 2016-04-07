package views;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * Main View for the program. Offers the user 3 choices : create a map, edit a map, or play the game.
 *
 * @author Team 6
 *
 */
public class MainView extends JFrame {

    public JButton buttonCreate;
    public JButton buttonEdit;
    public JButton buttonNewGame;
    public JButton buttonLoad;
    public JTextField textFieldLines;
    public JTextField textFieldColumns;

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    /**
     * Constructs the MainView object.
     *
     * @param mainController The controller receiving the user inputs.
     */
    public MainView(ActionListener mainController) {

        this.setTitle("Tower defense main menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 370, 230);
        this.setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(6, 6, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        this.buttonCreate = new JButton("Create map");
        this.buttonCreate.setBounds(10, 10, 140, 23);
        this.buttonCreate.addActionListener(mainController);
        contentPane.add(this.buttonCreate);

        this.textFieldLines = new JTextField();
        this.textFieldLines.setBounds(160, 12, 90, 20);
        this.textFieldLines.setText("lines");
        this.textFieldLines.setColumns(10);
        contentPane.add(this.textFieldLines);

        this.textFieldColumns = new JTextField();
        this.textFieldColumns.setBounds(260, 12, 90, 20);
        this.textFieldColumns.setText("columns");
        this.textFieldColumns.setColumns(10);
        contentPane.add(this.textFieldColumns);

        this.buttonEdit = new JButton("Edit map");
        this.buttonEdit.setBounds(10, 60, 140, 23);
        this.buttonEdit.addActionListener(mainController);
        contentPane.add(this.buttonEdit);

        this.buttonLoad = new JButton("Load game");
        this.buttonLoad.setBounds(10, 110, 140, 23);
        this.buttonLoad.addActionListener(mainController);
        contentPane.add(this.buttonLoad);

        this.buttonNewGame = new JButton("New game");
        this.buttonNewGame.setBounds(10, 160, 140, 23);
        this.buttonNewGame.addActionListener(mainController);
        contentPane.add(this.buttonNewGame);

    }

}
