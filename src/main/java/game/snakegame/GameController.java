package game.snakegame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {


    @FXML
    private AnchorPane gameScreen;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label gameOverLabel;

    private final ArrayList<Rectangle> snakeBody = new ArrayList<>();

    private double WIDTH;
    private double HEIGHT;
    private double REC_SIZE;

    private int score;

    private final int CHECK_NUM = 30;
    final int OFFSET = 2;

    private Rectangle food;
    private Rectangle snakeHead;

    Timer gameClock;

    int direction = 0;
//    0 LEFT
//    1 UP
//    2 RIGHT
//    3 DOWN

    public void initialize(){
        createCheckeredField();
        createSnake();
        createFood();


        gameClock = new Timer();
        gameClock.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Platform.runLater(() -> update());
            }
        }, 0, 250);
    }

    private void createFood() {
        food = new Rectangle(REC_SIZE, REC_SIZE);
        food.getStyleClass().add("food");
        gameScreen.getChildren().add(food);
        snakeHead = snakeBody.get(0);
        putFood();
    }

    private void putFood() {
        int randomX = new Random().nextInt(30);
        int randomY = new Random().nextInt(30);
        food.setLayoutX(randomX * REC_SIZE + OFFSET);
        food.setLayoutY(randomY * REC_SIZE + OFFSET);
        snakeHead.toFront();
        if (checkSnakeCollisionFood())
            putFood();
    }

    private boolean checkSnakeCollisionFood() {
        for (Rectangle bodyPiece: snakeBody){
            if (bodyPiece.getLayoutX() == food.getLayoutX() && bodyPiece.getLayoutY() == food.getLayoutY())
                return true;
        }
        return false;
    }

    private boolean checkSnakeCollisionHead() {
        for (Rectangle bodyPiece: snakeBody){
            if (bodyPiece.equals(snakeHead))
                continue;
            if (bodyPiece.getLayoutX() == snakeHead.getLayoutX() && bodyPiece.getLayoutY() == snakeHead.getLayoutY())
                return true;
        }
        return false;
    }

    private void createSnake() {
        System.out.println("GAME STARTED");
        Rectangle rectangle = new Rectangle(REC_SIZE, REC_SIZE);
        rectangle.setLayoutX(WIDTH / 2);
        rectangle.setLayoutY(HEIGHT / 2);
        snakeBody.add(rectangle);

        snakeBody.get(0).getStyleClass().add("snake-head");
        gameScreen.getChildren().addAll(snakeBody);
    }

    private void createCheckeredField() {
        WIDTH = gameScreen.getPrefWidth();
        HEIGHT = gameScreen.getPrefHeight();
        REC_SIZE = (int) (WIDTH/CHECK_NUM);
//        System.out.println("WIDTH: " + WIDTH);
//        System.out.println("HEIGHT: " + HEIGHT);

        for (int i = 0; i < CHECK_NUM; i++) {
            for (int j = 0; j < CHECK_NUM; j++) {
                Rectangle rectangle = new Rectangle(REC_SIZE, REC_SIZE);
                if ((i + j) % 2 == 0 )
                    rectangle.setFill(Color.web("#0b6b13"));
                else
                    rectangle.setFill(Color.web("#25b832"));

                rectangle.setLayoutX(j * REC_SIZE + OFFSET);
                rectangle.setLayoutY(i * REC_SIZE + OFFSET);

                gameScreen.getChildren().add(rectangle);
            }

        }
    }

    public void update(){
        scoreLabel.toFront();
        scoreLabel.setText("SCORE: " + score);

        if (snakeHead.getLayoutY() == food.getLayoutY() && snakeHead.getLayoutX() == food.getLayoutX()){
            System.out.println("EATEN FOOD\t LENGTH: " + snakeBody.size());
            eatFood();
            putFood();
        }

        for (int i = snakeBody.size()-1; i > 0; i--) {
            Rectangle current = snakeBody.get(i);
            Rectangle next = snakeBody.get(i-1);
            current.setLayoutY(next.getLayoutY());
            current.setLayoutX(next.getLayoutX());
        }

        switch (direction){
            case 0 -> snakeBody.get(0).setLayoutX(snakeBody.get(0).getLayoutX() + REC_SIZE);
            case 1 -> snakeBody.get(0).setLayoutY(snakeBody.get(0).getLayoutY() - REC_SIZE);
            case 2 -> snakeBody.get(0).setLayoutX(snakeBody.get(0).getLayoutX() - REC_SIZE);
            case 3 -> snakeBody.get(0).setLayoutY(snakeBody.get(0).getLayoutY() + REC_SIZE);
        }

        if (checkSnakeCollisionHead() || checkSnakeOutOfBounds()){
            gameover();
        }

    }

    private boolean checkSnakeOutOfBounds() {
        return snakeHead.getLayoutX() < 0 || snakeHead.getLayoutX() > WIDTH - REC_SIZE || snakeHead.getLayoutY() < 0 || snakeHead.getLayoutY() > HEIGHT - REC_SIZE ;
    }

    private void gameover() {
        System.out.println("GAME LOST!\tSCORE: " + score);
        gameScreen.getChildren().remove(snakeHead);
        gameClock.cancel();
    }

    private void eatFood() {
        Rectangle rectangle = new Rectangle(REC_SIZE, REC_SIZE);
        rectangle.setLayoutX(snakeBody.get(snakeBody.size()-1).getLayoutX());
        rectangle.setLayoutY(snakeBody.get(snakeBody.size()-1).getLayoutY());
        rectangle.getStyleClass().add("snake");
        snakeBody.add(rectangle);
        gameScreen.getChildren().add(rectangle);
        score = snakeBody.size() * 10;
    }

    public void keyUp(){
//        System.out.println("UP");
        if (direction == 3) return;
        direction = 1;
    }

    public void keyDown(){
//        System.out.println("DOWN");
        if (direction == 1) return;
        direction = 3;
    }

    public void keyLeft(){
//        System.out.println("LEFT");
        if (direction == 0) return;
        direction = 2;
    }

    public void keyRight(){
//        System.out.println("RIGHT");
        if (direction == 2) return;
        direction = 0;
    }
}