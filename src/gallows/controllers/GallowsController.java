package gallows.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import gallows.model.Game;


import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GallowsController implements Initializable {


    public AnchorPane pane;
    public Pane keyBoardPane;
    public ImageView gallowsPicture;
    public Pane gameLetters;
    public PieChart pieStat;
    private GallowsLetter[] letters;
    private Image[] gallows;
    private Game game;

    {
        gallows = new Image[7];
        for (int i = 0; i < gallows.length; i++) {
            gallows[i] = new Image("gallows/images/" + i + ".png");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieChartInit();

        restartGame();
    }

    private void pieChartInit() {
        pieStat.setData(FXCollections.observableArrayList(
                new PieChart.Data("Победа", 0),
                new PieChart.Data("Поражение", 0)
        ));
        pieStat.getData().forEach(data -> data.nameProperty().bind(
                Bindings.concat( data.getName(), " ", data.pieValueProperty())
        ));
    }

    private void clearField() {
        keyBoardPane.getChildren().clear();
        gameLetters.getChildren().clear();
    }

    private void prepareKeyboard() {
        String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toUpperCase();

        final int widthButtonsTable = 10;

        final int BUTTON_WIDTH = 50;
        final int BUTTON_HEIGHT = 30;
        final double MARGIN_X = 1.2;
        final double MARGIN_Y = 1.2;
        final double START_X = (keyBoardPane.getPrefWidth() - (BUTTON_WIDTH * widthButtonsTable * MARGIN_X)) / 2.0;
        final double START_Y = 10;

        for (int i = 0; i < widthButtonsTable; i++) {
            int widthLine = widthButtonsTable <= alphabet.length() ? widthButtonsTable : alphabet.length();
            for (int j = 0; j < widthLine; j++) {
                Button key = new Button(String.valueOf(alphabet.charAt(j)));
                key.setPrefWidth(BUTTON_WIDTH);
                key.setPrefHeight(BUTTON_HEIGHT);
                key.setLayoutX(START_X + j * MARGIN_X * BUTTON_WIDTH);
                key.setLayoutY(START_Y + i * BUTTON_HEIGHT * MARGIN_Y);
                key.setFocusTraversable(false);
                keyBoardPane.getChildren().add(key);
                key.setOnMouseClicked(event -> guessLetter(((Button) event.getSource())));
            }
            alphabet = alphabet.substring(widthLine, alphabet.length());
        }
    }

    private void prepareLetters() {
        letters = new GallowsLetter[game.getWordLength()];
        final double LETTERS_SIZE = 50;
        final double LETTERS_MARGIN = 1.2;
        final double LETTERS_OFFSET = (gameLetters.getPrefWidth() - LETTERS_SIZE * LETTERS_MARGIN * letters.length) / 2.0;
        for (int i = 0; i < letters.length; i++) {
            letters[i] = new GallowsLetter(LETTERS_SIZE, LETTERS_SIZE);
            letters[i].setLayoutX(LETTERS_OFFSET + LETTERS_SIZE * LETTERS_MARGIN * i);
            gameLetters.getChildren().add(letters[i]);
        }
    }

    private void restartGame() {
        game = new Game();

        clearField();

        prepareKeyboard();

        prepareLetters();

        gallowsPicture.setImage(gallows[0]);
    }

    private void guessLetter(Button source) {
        try {
            ArrayList<Integer> guessed = game.makeChoice(source.getText());
            String gameWord = game.getGameWord().toUpperCase();
            for (Integer index :
                    guessed) {
                letters[index].setLetter(gameWord.charAt(index));
            }
            source.setVisible(false);
            gallowsPicture.setImage(gallows[game.getLoseCount()]);

            if (!game.isAlive()) {
                endGame(gameWord);
            }
        } catch (Game.LetterWasUsedException e) {
            e.printStackTrace();
        }
    }

    private void endGame(String gameWord) {
        updateStatistics();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Виселица");
        if (game.isWin()) {
            alert.setHeaderText("Вы выиграли!");
        } else {
            alert.setHeaderText("Вы проиграли, загаданное слово - " + gameWord);
        }

        alert.setContentText("Хотите еще раз?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void updateStatistics() {
        pieStat.getData().get(0).setPieValue(game.getWins());
        pieStat.getData().get(1).setPieValue(game.getLoses());
    }

}

