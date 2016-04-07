package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * This class implements the Observer interface to get informed of changes in the Game class objects.
 *
 * @author Team 6
 *
 */
public class GameLog implements Observer {

    /**
     * Long date format for the log file paths.
     */
    public static SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("dd MMM HH:mm:ss");

    /**
     * Short date format for the log file paths.
     */
    public static SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private String oldlog = "";
    private HashMap<String, List<String>> logMap = new HashMap<String, List<String>>();
    private List<String> towerColLog=new ArrayList<String>();

    /**
     * Method called when the observed object is modified.
     */
    @Override
    public void update(Observable observable, Object object) {
        Game game = (Game) observable;

        if (game.logfile == null) {
            Calendar cal = Calendar.getInstance();
            game.logfile = LONG_DATE_FORMAT.format(cal.getTime()).replaceAll(":", "-");;
            game.logfile = "GameLog-" + game.logfile;
            System.out.println(game.logfile);
        }

        PrintWriter pr;
        try {
            pr = new PrintWriter(new BufferedWriter(new FileWriter(game.logfile, true)));

            if (game.startlog) {
                pr.println(initialLog(game));
                System.out.println("format the code then remove it ! ");
                System.out.println(initialLog(game));
                game.startlog = false;
            }
            Calendar cal = Calendar.getInstance();

            if (!game.log.equals(oldlog) && game.log != "") {
                pr.println(SHORT_DATE_FORMAT.format(cal.getTime()) + "   -> ");
                System.out.println(SHORT_DATE_FORMAT.format(cal.getTime()) + "   -> ");
                pr.println(game.log);
                System.out.println(game.log);
            }

            String[] lines = game.log.split("\\\n");
            for (int i = 0; i < lines.length; i++) {
                lines[i] = "@" + SHORT_DATE_FORMAT.format(cal.getTime()) + " " + lines[i];

                if (lines[i].indexOf("[") == 18) {
                    String key = lines[i].substring(10, 21);
                    if (key.indexOf("tower")==0){
                        towerColLog.add(lines[i]+"\n");
                    }                  
                    if (logMap.containsKey(key)) {
                        logMap.get(key).add(lines[i] + "\n");
                    } else {
                        List<String> newList = new ArrayList<String>();
                        newList.add(lines[i] + "\n");
                        logMap.put(key, newList);
                    }
                }
            }

            // after the game is over
            if (game.isOver() || game.isWon()) {
                pr.println("-----------object logs----------------------------------");
                System.out.println("-----------object logs----------------------------------");
                Iterator it = logMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    List<String> sList = (ArrayList<String>) pair.getValue();

                    //sorting the list of collective logs
                    Set<String> sortIt = new LinkedHashSet<>();
                    sortIt.addAll(sList);
                    sList.clear();
                    sList.addAll(sortIt);

                    pr.println(pair.getKey() + "\n" + sList);
                    System.out.println(pair.getKey() + "\n" + sList);

                    pr.println();
                    System.out.println();
                    it.remove();
                }

                System.out.println(" \n -------------------Towers Collective Log----------------- \n");
                System.out.println(towerColLog);
                pr.println(" \n -------------------Towers Collective Log----------------- \n");
                pr.println(towerColLog);
            }
            oldlog = game.log;
            pr.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Returns a string which shows the game log.
     *
     * @param game an object of Game class
     * @return a string representing initial log
     */
    private String initialLog(Game game) {
        String init;
        Calendar cal = Calendar.getInstance();
        init = LONG_DATE_FORMAT.format(cal.getTime()) + "  Game Started \n";
        init += "Game Grid (" + game.grid.filePath + ") : " + game.grid.cases.length + " x " + game.grid.cases[0].length+ "\n";
        init += "Map Entry Point : " + game.grid.entryPoint() + " | Map Exit Point : " + game.grid.exitPoint() + " \n";
        init += "Starting health = " + game.getLives() + "\n";
        init += "Starting money = " + game.getMoney() + " units\n";

        return init;
    }

}
