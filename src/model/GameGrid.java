package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a game map, composed of a 2-dimension matrix of tiles. It implements methods for locating
 * different cells on the map, checking the validity of a map and saving/restoring maps from text files.
 *
 * @author Team 6
 */
public class GameGrid {

    /**
     * Different types of cases a GameGrid object can hold.
     */
    public static enum CASE_TYPES {
        GRASS, BUSH, ROAD, START, END, NONE
    };

    /**
     * Images used to represent the different types of case types
     */
    public static String[] CASE_TYPES_ICON_PATHS =
                    {"icons/grass.jpg", "icons/grass2.jpg", "icons/road.jpg", "icons/start.png", "icons/end.png"};

    public int pathindex = 1;
    public String filePath = "";

    CASE_TYPES[][] cases;
    private Random randomGenerator = new Random();

    private ArrayList<GameScore> scores;

    private Date dateCreated;

    private Date dateModified;

    /**
     * Constructs an empty GameGrid.
     */
    public GameGrid() {
        long currentTime = System.currentTimeMillis();
        this.dateCreated = new Date(currentTime);
        this.dateModified = new Date(currentTime);
        this.scores = new ArrayList<GameScore>();

    }

    /**
     * Constructs a new GameGrid with the specified dimensions. All the tiles are initialized as grass.
     *
     * @param lineCount user's choice for width
     * @param columnCount user's choice for length
     */
    public GameGrid(int lineCount, int columnCount) {
        long currentTime = System.currentTimeMillis();
        this.dateCreated = new Date(currentTime);
        this.dateModified = new Date(currentTime);
        this.scores = new ArrayList<GameScore>();
        this.cases = new CASE_TYPES[lineCount][columnCount];
        for (int i = 0; i < lineCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                this.cases[i][j] = CASE_TYPES.GRASS;
            }
        }
    }

    /**
     * Saves the game grid to a file using the file path from where we loaded the game grid.
     */
    public void writeToFile() {
        this.writeToFile(this.filePath);
    }

    /**
     * Writes a serialized version of the game grid to a file.
     *
     * @param filename name of the file to store the grid to.
     */
    @SuppressWarnings("deprecation")
    public void writeToFile(String filename) {

        // Writing the map to a file, so we need to update the
        // modification date.
        this.dateModified = new Date(System.currentTimeMillis());

        PrintWriter pr;
        try {
            pr = new PrintWriter(filename);
            pr.print(this.cases.length + " " + this.cases[0].length);

            for (int i = 0; i < this.cases.length; i++) {
                pr.println();
                for (int j = 0; j < this.cases[0].length; j++) {
                    pr.print(this.cases[i][j].ordinal() + " ");
                }
            }

            pr.println();
            pr.println(this.dateCreated.toGMTString());
            pr.println(this.dateModified.toGMTString());

            for (GameScore gameScore : this.scores) {
                pr.println(gameScore.toString());
            }

            pr.close();

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Gets the cases used by the grid.
     */
    public CASE_TYPES[][] getCases() {
        return this.cases;
    }

    /**
     * Sets the cases used by the grid.
     *
     * @param cases the new matrix of cases used by the grid.
     */
    public void setCases(CASE_TYPES[][] cases) {
        this.cases = cases;
    }

    /**
     * This method reads a serialized GameGrid object from a file specified by the user.
     *
     * @param filename Name of the file where the object is stored.
     * @param addRandomBushes Determines if random bushes should be generated randomly on the loaded grid.
     *
     * @throws exception If the file could not be read or has the wrong format.
     */
    public void readFromFile(String filename, Boolean addRandomBushes) {

        filePath = filename;
        int linenumber = 0; // line number starts from the second line
        int rows = 0;
        int columns = 0; // n customer, k teams

        // Using an ArrayList instead of standard arrays.
        this.cases = new CASE_TYPES[1][1]; //
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line = null;

            // read the 1st line, dimensions of the map
            line = br.readLine();
            String[] tokens = line.split("\\s+");
            rows = Integer.parseInt(tokens[0]);
            columns = Integer.parseInt(tokens[1]);

            this.cases = new CASE_TYPES[rows][columns];

            // read other lines
            for (int j = 0; j < rows; j++) {

                line = br.readLine();
                // \\s+ means any number of white spaces between tokens
                tokens = line.split("\\s+");

                for (int i = 0; i < columns; i++) {
                    int caseValue = Integer.parseInt(tokens[i]);
                    this.cases[linenumber][i] = CASE_TYPES.values()[caseValue];
                    if (addRandomBushes && this.cases[linenumber][i] == CASE_TYPES.GRASS
                                    && randomGenerator.nextInt(100) > 92) {
                        this.cases[linenumber][i] = CASE_TYPES.BUSH;
                    }
                }

                linenumber = linenumber + 1; // next line (lane) information

            }

            this.dateCreated = new Date(Date.parse(br.readLine()));
            this.dateModified = new Date(Date.parse(br.readLine()));

            while ((line = br.readLine()) != null) {
                GameScore gameScore = new GameScore();
                gameScore.fromString(line);
                this.scores.add(gameScore);
            }
            // while line

            br.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Validates that a map is valid. Many checks are made, including if one exit and one entry point exist, and if
     * there is a connecting path between them.
     *
     * @throws GameGridException
     */
    public void validateMap() throws GameGridException {

        ArrayList<GridLocation> entryPoints = this.getCasesByType(CASE_TYPES.START);
        if (entryPoints.size() > 1) {
            throw new GameGridException("Invalid grid : too many entry points.");
        }

        if (entryPoints.size() == 0) {
            throw new GameGridException("Invalid grid : no entry point.");
        }

        ArrayList<GridLocation> exitPoints = this.getCasesByType(CASE_TYPES.END);
        if (exitPoints.size() > 1) {
            throw new GameGridException("Invalid grid : too many exit points.");
        }

        if (exitPoints.size() == 0) {
            throw new GameGridException("Invalid grid : no exit point.");
        }

        GridLocation entryPoint = this.entryPoint();
        GridLocation exitPoint = this.exitPoint();

        if (!this.isValidEntryPoint(entryPoint)) {
            throw new GameGridException("Invalid entry point : must be on the left or top edge!");
        }

        if (!this.isValidExitPoint(exitPoint)) {
            throw new GameGridException("Invalid exit point : must be on the right or bottom edge!");
        }

        if (!this.isConnected()) {
            throw new GameGridException("Invalid grid : no connecting path between exit point and entry point.");
        }

    }

    /**
     * Verifies if a map is connected from the entry point to the exit point. We must be sure that there is only one
     * exit point and one entry point before calling this function!
     */
    public boolean isConnected() {
        GridLocation exitPoint = this.exitPoint();
        int[][][] cnctvt = this.connectivities();
        return cnctvt[exitPoint.x][exitPoint.y][0] == 1;
    }

    /**
     * Returns an array with represents the connections between entry and exit points
     *
     * @returns Connectivity Array
     */

    public int[][][] connectivities() {
        GridLocation entryPoint = this.entryPoint();
        int[][][] connectivities = new int[this.cases.length][this.cases[0].length][3];
        connectivities[entryPoint.x][entryPoint.y][0] = 1;
        connectivities[entryPoint.x][entryPoint.y][1] = pathindex++;
        this.connect(connectivities, entryPoint.x, entryPoint.y);

        return connectivities;
    }

    /**
     * Gets the cases of the grid corresponding to a certain type.
     *
     * @param caseType Type of the tiles to return.
     *
     * @return an ArrayList of GridLocation of the locations found.
     */
    public ArrayList<GridLocation> getCasesByType(CASE_TYPES caseType) {

        ArrayList<GridLocation> response = new ArrayList<GridLocation>();

        for (int i = 0; i < this.cases.length; i++) {
            for (int j = 0; j < this.cases[0].length; j++) {
                if (this.cases[i][j] == caseType) {
                    response.add(new GridLocation(i, j));
                }

            }
        }

        return response;

    }

    /**
     * Returns the entry point of the grid. The entry point is assumed to be at the left edge of the map.
     *
     * @returns the height of the entry point, or -1 if no valid entry point.
     */
    public GridLocation entryPoint() {
        return this.getCasesByType(CASE_TYPES.START).get(0);
    }

    /**
     * Returns the path grid.
     *
     * @returns an arraylist of GridLocation
     */
    public ArrayList<GridLocation> road() {
        return this.getCasesByType(CASE_TYPES.ROAD);
    }

    /**
     * Returns the exit point of the grid. The exit point is assumed to be at the right edge of the map.
     *
     * @returns the height of the exit point, or -1 if no valid exit point.
     */
    public GridLocation exitPoint() {
        return this.getCasesByType(CASE_TYPES.END).get(0);
    }

    /**
     * Determines if the location specified is a valid road location.
     *
     * @param line Line of the coordinate to validate.
     * @param column Column of the coordinate to validate.
     * @param connectivities Matrix used for the connectivity check.
     *
     * @return True if the location is a valid road location, false otherwise.
     */
    public boolean isRoad(int line, int column, int[][][] connectivities) {

        if (line < 0) {
            return false;
        }
        if (line > this.cases.length - 1) {
            return false;
        }
        if (column < 0) {
            return false;
        }
        if (column > this.cases[0].length - 1) {
            return false;
        }
        if (this.cases[line][column] == CASE_TYPES.GRASS || this.cases[line][column] == CASE_TYPES.BUSH) {
            return false;
        }
        if (connectivities[line][column][0] == 1) {
            return false;
        }
        return true;
    }

    /**
     * Side method for isConnected method, it connects the neighbor of the tile(i,j) together if they are path tiles,
     * from the entrance to the exit point
     */
    public void connect(int[][][] connectivites, int line, int column) {

        // check the right neighbor
        if (this.isRoad(line, column + 1, connectivites)) {
            connectivites[line][column + 1][0] = 1;
            connectivites[line][column + 1][1] = pathindex++;
            this.connect(connectivites, line, column + 1);
        }

        // check the below neighbor
        if (this.isRoad(line + 1, column, connectivites)) {
            connectivites[line + 1][column][0] = 1;
            connectivites[line + 1][column][1] = pathindex++;
            this.connect(connectivites, line + 1, column);
        }

        // check the above neighbor
        if (this.isRoad(line - 1, column, connectivites)) {
            connectivites[line - 1][column][0] = 1;
            connectivites[line - 1][column][1] = pathindex++;
            this.connect(connectivites, line - 1, column);
        }

        // check the left neighbor
        if (this.isRoad(line, column - 1, connectivites)) {
            connectivites[line][column - 1][0] = 1;
            connectivites[line][column - 1][1] = pathindex++;
            this.connect(connectivites, line, column - 1);
        }

    }

    /**
     * Determines if a location is valid as an entry point of the grid. Must be on the left edge or top edge to be a
     * valid entry point.
     *
     * @param gridLocation The grid location entry point candidate
     *
     * @return True if the location is a valid entry point, false otherwise.
     */
    public boolean isValidEntryPoint(GridLocation gridLocation) {
        if (gridLocation.x == 0 || gridLocation.y == 0) {
            return true;
        }
        return false;
    }

    /**
     * Determines if a location is valid as an exit point of the grid. Must be on the right edge or bottom edge to be a
     * valid entry point.
     *
     * @param gridLocation The grid location exit point candidate
     *
     * @return True if the location is a valid exit point, false otherwise.
     */
    public boolean isValidExitPoint(GridLocation gridLocation) {
        if (gridLocation.x == this.cases.length - 1 || gridLocation.y == this.cases[0].length - 1) {
            return true;
        }
        return false;
    }

    public void addGameScore(GameScore gameScore) {
        this.scores.add(gameScore);
    }

    public ArrayList<GameScore> getGameScores() {
        return this.scores;
    }

}
