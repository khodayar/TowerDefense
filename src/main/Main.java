package main;

import java.awt.EventQueue;

import controllers.MainController;

/**
 * This class is the main class containing the entry point of the program.
 */
public class Main {

    /**
     * Entry point of the program.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(MainController.getInstance());
    }

}
