package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame2 extends Application {

    // Set up the game board and snake dimensions
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int DOT_SIZE = 20;

    // Set up the snake, game state variables and the initial direction
    private List<Point2D> snake = new ArrayList<>();
    private Point2D fruit;
    private Point2D direction = new Point2D(1, 0);
    private boolean isDead = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");

//        Add key listener for snake movement
//        move the snake in each of the four directions (up, down, left, and right)
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                if (direction.getY() != 1) {
                    direction = new Point2D(0, -1);
                }
            } else if (event.getCode() == KeyCode.DOWN) {
                if (direction.getY() != -1) {
                    direction = new Point2D(0, 1);
                }
            } else if (event.getCode() == KeyCode.LEFT) {
                if (direction.getX() != 1) {
                    direction = new Point2D(-1, 0);
                }
            } else if (event.getCode() == KeyCode.RIGHT) {
                if (direction.getX() != -1) {
                    direction = new Point2D(1, 0);
                }
            }
        });

        initGame();
        // Set up the game loop
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {  // now represents the current timestamp in nanoseconds
                // Check if it's time for the next frame
                if (now - lastUpdate >= 100_000_000) {
                    lastUpdate = now;
                    update();
                    draw(canvas.getGraphicsContext2D());
                }
            }
        };
        timer.start();
        primaryStage.show();
    }
//draw fruit apple and snake

    //This method is called in the handle method of the AnimationTimer,
    // which ensures that it is called at a fixed rate
    private void draw(GraphicsContext gc) {
        //This method first clears the canvas using the clearRect method of the GraphicsContext object.
        //It then draws the fruit at its current location using the fillOval method with a red color.

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.RED);
        gc.fillOval(fruit.getX(), fruit.getY(), DOT_SIZE, DOT_SIZE);

        //Next, it iterates over each point in the snake list
        //and draws a green oval at that point using the fillOval method.
        gc.setFill(Color.GREEN);
        for (Point2D point : snake) {
            gc.fillOval(point.getX(), point.getY(), DOT_SIZE, DOT_SIZE);
        }
    }

//    Set up the initial snake and starting position for the snake and fruit
    private void initGame() {
        snake.add(new Point2D(WIDTH / 2, HEIGHT / 2));
        snake.add(new Point2D(WIDTH / 2 - DOT_SIZE, HEIGHT / 2));
        snake.add(new Point2D(WIDTH / 2 - 2 * DOT_SIZE, HEIGHT / 2));
        createFruit();
    }
    // Generate the initial apple
    private void createFruit() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / DOT_SIZE) * DOT_SIZE;
        int y = random.nextInt(HEIGHT / DOT_SIZE) * DOT_SIZE;
        fruit = new Point2D(x, y);
    }

    private void update() {
        if (isDead) {
            return;
        }
        //calculate the new position of the head of the snake based on the current direction.
        //(---  snake.get(0) gets the first element in the List which representing the snake's body,
        // which is the current position of the snake's head.
        // The add() method of Point2D is used to add a Vector2D object to the current position of the head to get the next position.

        //The Vector2D object is obtained by multiplying the current direction of the snake (represented as a Point2D object)
        // by the size of each dot on the game board. This is done to get the position of the next dot in the direction that the snake is moving.---)

        Point2D head = snake.get(0).add(direction.multiply(DOT_SIZE));
//        System.out.println(head);
//        if (head.getX() < 0 || head.getX() >= WIDTH || head.getY() < 0 || head.getY() >= HEIGHT) {
//            isDead = true;
//            return;
//        }

// checks if the head is outside the left, right, top, or bottom boundaries
// and adjusts its position to wrap around the board accordingly.
        if (head.getX() < 0) {
            head = new Point2D(WIDTH - DOT_SIZE, head.getY());
        } else if (head.getX() >= WIDTH) {
            head = new Point2D(0, head.getY());
        } else if (head.getY() < 0) {
            head = new Point2D(head.getX(), HEIGHT - DOT_SIZE);
        } else if (head.getY() >= HEIGHT) {
            head = new Point2D(head.getX(), 0);
        }

// This will add the head point to the beginning of the snake list,
// and remove the last element of the list, effectively moving the snake forward.
        if (head.equals(fruit)) {
            snake.add(0, fruit);
            createFruit();
        } else {
            snake.remove(snake.size() - 1);
            snake.add(0, head);
        }
// the snake die if it hits itself
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                isDead = true;
                break;
            }
        }

    }
}