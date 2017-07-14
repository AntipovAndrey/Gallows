package gallows.controllers;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Created by andrey on 12.07.17.
 */
public class GallowsLetter extends Group {
    private BorderPane mainPane;
    private Label textWithLetter;

    public GallowsLetter(double width, double height) {
        super();

        mainPane = new BorderPane();
        mainPane.setPrefWidth(width);
        mainPane.setPrefHeight(height);

        textWithLetter = new Label("");
        textWithLetter.setFont(Font.font(45));
        textWithLetter.setAlignment(Pos.CENTER);
        mainPane.setCenter(textWithLetter);

        Rectangle bottomRectangle = new Rectangle();
        bottomRectangle.setWidth(width);
        bottomRectangle.setHeight(0.1 * height);
        bottomRectangle.setArcHeight(10);
        bottomRectangle.setArcHeight(bottomRectangle.getArcHeight());
        bottomRectangle.setFill(Color.grayRgb(50));

        mainPane.setBottom(bottomRectangle);

        super.getChildren().add(mainPane);

    }

    public void setLetter(char letter) {
        textWithLetter.setText("" + letter);
    }

}
