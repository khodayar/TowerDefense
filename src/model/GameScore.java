package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a game score. The score is the number of critters the player was able to kill.
 *
 * @author Team 6
 *
 */
public class GameScore implements Comparable<GameScore> {

    /**
     * The number of scores that are displayed in the high score window.
     */
    public static int HIGH_SCORES_COUNT = 5;

    public java.util.Date datePlayed = new Date(System.currentTimeMillis());
    public int killedCritters = 0;
    public boolean won = false;

    public GameScore() {

    }

    public GameScore(int crittersKilled, boolean won) {
        this.killedCritters = crittersKilled;
        this.won = won;
    }

    @Override
    public int compareTo(GameScore gameScore) {
        return gameScore.killedCritters - this.killedCritters;
    }

    @Override
    public String toString() {
        return this.datePlayed.toGMTString() + ',' + this.killedCritters + ',' + (this.won != false ? "won" : "lost");
    }

    @SuppressWarnings("deprecation")
    public void fromString(String gameScoreString) {
        String[] infos = gameScoreString.split(",", 1000);
        this.datePlayed = new Date(Date.parse(infos[0]));
        this.killedCritters = Integer.parseInt(infos[1]);
        this.won = (infos[2].length() == 3);
    }

    public static ArrayList<GameScore> getHighScores(ArrayList<GameScore> gameScores) {
        ArrayList<GameScore> response = new ArrayList<GameScore>();
        Object[] gameScoresArray = gameScores.toArray();
        // System.out.println(gameScoresArray);
        Arrays.sort(gameScoresArray);
        int highScoreCounts = Math.min(GameScore.HIGH_SCORES_COUNT, gameScores.size());
        for (int i = 0; i < highScoreCounts; i++) {
            response.add((GameScore) gameScoresArray[i]);
        }
        return response;
    }

    public static String displayHighScores(ArrayList<GameScore> gameScores) {
        if (gameScores.size() == 0) {
            return "No high scores for this map!";
        }
        ArrayList<GameScore> highScores = GameScore.getHighScores(gameScores);
        String response = "Date                                    Critters killed  Result\n";
        for (GameScore gameScore : highScores) {
            response += gameScore.datePlayed.toGMTString() + "  ";
            response += gameScore.killedCritters + "                       ";
            if (gameScore.killedCritters < 10) {
                response += "  ";
            }
            response += (gameScore.won ? "won" : "lost") + '\n';
        }
        return response;
    }

}
