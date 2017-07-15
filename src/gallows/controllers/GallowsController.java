package gallows.controllers;

import gallows.model.Game;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GallowsController implements Initializable {


    public AnchorPane pane;
    public Pane keyBoardPane;
    public ImageView gallowsPicture;
    public AnchorPane gameLetters;
    public PieChart pieStat;
    public HBox boxWithLetters;
    public GridPane keyboardTable;
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
                Bindings.concat(data.getName(), " ", data.pieValueProperty())
        ));
    }

    private void clearField() {
        keyboardTable.getChildren().clear();
        keyboardTable.getColumnConstraints().clear();
        keyboardTable.getRowConstraints().clear();
        boxWithLetters.getChildren().clear();
    }

    private void prepareKeyboard() {
        String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toUpperCase();

        final int widthButtonsTable = 10;
        final int heightButtonsTable = (int) Math.ceil(1.0 * alphabet.length() / widthButtonsTable);

        for (int i = 0; i < widthButtonsTable; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / widthButtonsTable);
            keyboardTable.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < heightButtonsTable; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / heightButtonsTable);
            keyboardTable.getRowConstraints().add(rowConst);
        }

        final int BUTTON_WIDTH = 55;
        final int BUTTON_HEIGHT = 35;

        for (int i = 0; i < widthButtonsTable; i++) {
            int widthLine = widthButtonsTable <= alphabet.length() ? widthButtonsTable : alphabet.length();
            for (int j = 0; j < widthLine; j++) {
                Button key = new Button(String.valueOf(alphabet.charAt(j)));
                key.setPrefWidth(BUTTON_WIDTH);
                key.setPrefHeight(BUTTON_HEIGHT);
                key.setFocusTraversable(false);
                key.setOnMouseClicked(event -> guessLetter(((Button) event.getSource())));
                GridPane.setValignment(key, VPos.CENTER);
                GridPane.setHalignment(key, HPos.CENTER);
                GridPane.setFillWidth(key, true);
                keyboardTable.add(key, j, i);
            }
            alphabet = alphabet.substring(widthLine, alphabet.length());
        }
    }

    private void prepareLetters() {
        final double LETTERS_MARGIN = 5;
        letters = new GallowsLetter[game.getWordLength()];
        final double LETTERS_SIZE = 50;
        boxWithLetters.setSpacing(LETTERS_MARGIN);
        for (int i = 0; i < letters.length; i++) {
            letters[i] = new GallowsLetter(LETTERS_SIZE, LETTERS_SIZE);
            boxWithLetters.getChildren().add(letters[i]);
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

