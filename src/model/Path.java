package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class implements components for calculating shortest path, finding neighbors with minimum distance, getting next
 * location on the path and calculating if a location is on the path.
 * 
 * @author Team 6
 *
 */
public class Path {

    /**
     * Game grid in which to find the shortest path.
     */
    public GameGrid gamegrid;

    /**
     * Cached version of the shortest path in the grid.
     */
    private ArrayList<GridLocation> shortestPath;

    /**
     * Creates the shortest path associated with the specified GameGrid object.
     *
     * @param gamegrid GameGrid in which to find the shortest path.
     */
    public Path(GameGrid gamegrid) {
        this.gamegrid = gamegrid;
    }

    /**
     * Gets the location coming after the current location in the shortest path.
     *
     * @param currentLocation current location in the path.
     * @return The next location in the path after the specified GridLocation.
     */
    public GridLocation getNextLocation(GridLocation currentLocation) {

        // Lazy initializes the shortest path.
        if (this.shortestPath == null) {
            this.shortestPath = this.calculateShortestPath();
        }

        int index = -1;
        Iterator<GridLocation> itr = shortestPath.iterator();
        while (itr.hasNext()) {
            if (itr.next().equals(currentLocation)) {
                if (!itr.hasNext()) {
                    return null;
                }
                index = shortestPath.indexOf(itr.next());
                break;
            }

        }

        return shortestPath.get(index);

    }

    /**
     * Gets the shortest path associated with the grid.
     * 
     * @return An array list of GridLocation representing the shortest path from the entry point to the exit point in
     *         the grid.
     */
    public ArrayList<GridLocation> getShortestPath() {
        // Lazy initializes the shortest path.
        if (this.shortestPath == null) {
            this.shortestPath = this.calculateShortestPath();
        }
        return this.shortestPath;
    }

    /**
     * Returns the shortest path as an array list starts with the entry point
     */
    private ArrayList<GridLocation> calculateShortestPath() {
        ArrayList<GridLocation> pathlist = new ArrayList<GridLocation>();
        GridLocation grid = gamegrid.exitPoint();
        grid = minNeighbor(grid, this.gamegrid.connectivities());
        while (!(grid.x == gamegrid.entryPoint().x && grid.y == gamegrid.entryPoint().y)) {
            pathlist.add(0, grid);
            grid = minNeighbor(grid, this.gamegrid.connectivities());

        }
        return pathlist;
    }

    /**
     * Finds the neighbor with the minimum distance from the entry (nearest).
     * 
     * @param gridLocation The current grid location.
     * @param connectivites Grid connectivities.
     *
     * @return neighbor nearest the entry point.
     */
    private GridLocation minNeighbor(GridLocation gridLocation, int[][][] connectivites) {
        int line = gridLocation.x;
        int column = gridLocation.y;
        GridLocation minNeighbor = gridLocation;

        // check the left
        if (isPath(line, column - 1, connectivites)) {
            if (connectivites[line][column - 1][1] < connectivites[minNeighbor.x][minNeighbor.y][1]) {
                minNeighbor = new GridLocation(line, column - 1);
            }
        }

        // check above
        if (isPath(line - 1, column, connectivites)) {
            if (connectivites[line - 1][column][1] < connectivites[minNeighbor.x][minNeighbor.y][1]) {
                minNeighbor = new GridLocation(line - 1, column);
            }
        }

        // check below
        if (isPath(line + 1, column, connectivites)) {
            if (connectivites[line + 1][column][1] < connectivites[minNeighbor.x][minNeighbor.y][1]) {
                minNeighbor = new GridLocation(line + 1, column);
            }
        }


        // check the right
        if (isPath(line, column + 1, connectivites)) {
            if (connectivites[line][column + 1][1] < connectivites[minNeighbor.x][minNeighbor.y][1]) {
                minNeighbor = new GridLocation(line, column + 1);
            }
        }

        return minNeighbor;
    }

    /**
     * Determines if the location specified belongs to the path
     *
     * @param line Line of the coordinate to validate.
     * @param column Column of the coordinate to validate.
     * @param connectivities Matrix used for the connectivity check.
     * @return True if the location is a valid path location
     */
    private boolean isPath(int line, int column, int[][][] connectivities) {

        if (line < 0) {
            return false;
        }
        if (line > connectivities.length - 1) {
            return false;
        }
        if (column < 0) {
            return false;
        }
        if (column > connectivities[0].length - 1) {
            return false;
        }

        if (connectivities[line][column][0] == 0) {
            return false;
        }

        return true;
    }


    /**
     * Returns the String consists of GridLocations in the shortest path
     */
    public String toString() {
        String pathstr = "";
        this.shortestPath = this.calculateShortestPath();
        pathstr += "[" + shortestPath.get(0).x + "," + shortestPath.get(0).y + "]";
        Iterator<GridLocation> itr = shortestPath.iterator();
        while (itr.hasNext()) {
            pathstr += "[" + itr.next().x + "," + itr.next().y + "]";
        }

        return pathstr;
    }


}


