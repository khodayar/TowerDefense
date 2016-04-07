package model;

/**
 * Base class for the exceptions raised in the GameGrid class.
 *
 * @author Team 6
 *
 */
public class GameGridException extends Exception {

    /**
     * Constructor for the GameGridException class.
     *
     * @param string message associated with the raised exception.
     */
    public GameGridException(String string) {
        super(string);
    }

    private static final long serialVersionUID = 1L;
}
