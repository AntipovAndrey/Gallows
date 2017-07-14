package gallows.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by andrey on 12.07.17.
 */
public class Game {

    private String gameWord;
    private HashSet<String> usedLetters;
    private int loseCounter;
    private int lettersToGuess;
    private final int MAX_LOSE = 6;
    private GameStage stage = GameStage.IN_GAME;
    private static int totalWins;
    private static int totalLoses;

    public int getWins() {
        return totalWins;
    }

    public int getLoses() {
        return totalLoses;
    }


    public static class GallowsException extends Exception {
    }

    public static class LetterWasUsedException extends GallowsException {
    }

    enum GameStage {
        IN_GAME, LOSE, WIN
    }

    public Game() {
        gameWord = Dictionary.getWord().toLowerCase();
        lettersToGuess = gameWord.length();
        usedLetters = new HashSet<>();
    }

    public ArrayList<Integer> makeChoice(String letter) throws LetterWasUsedException {
        if (usedLetters.contains(letter)) throw new LetterWasUsedException();
        else usedLetters.add(letter);

        char toSearch = letter.toLowerCase().charAt(0);
        ArrayList<Integer> guessedIndices = new ArrayList<>();
        char[] wordCharArray = gameWord.toCharArray();
        for (int i = 0; i < gameWord.length(); i++) {
            if (wordCharArray[i] == toSearch) {
                guessedIndices.add(i);
                lettersToGuess--;
            }
        }
        if (guessedIndices.size() == 0) loseCounter++;
        changeStage();
        return guessedIndices;
    }

    private void changeStage() {
        if (loseCounter == MAX_LOSE) {
            stage = GameStage.LOSE;
            totalLoses++;
        } else if (lettersToGuess == 0) {
            stage = GameStage.WIN;
            totalWins++;
        }
    }

    public boolean isAlive() {
        return stage == GameStage.IN_GAME;
    }

    public boolean isWin() {
        return stage == GameStage.WIN;
    }

    public int getWordLength() {
        return gameWord.length();
    }

    public String getGameWord() {
        return gameWord;
    }

    public int getLoseCount() {
        return loseCounter;
    }
}


