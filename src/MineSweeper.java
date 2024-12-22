import java.util.Random;
import java.util.Scanner;

public class MineSweeper extends Game {
    private int totalMines;
    private int totalSafeCells;
    private int moves;
    private long endTime;
    private long startTime;


    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m"; //
    private static final String YELLOW = "\u001B[33m";

    public void setupGame() {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Welcome to MineSweeper!");
        System.out.print("Enter number of rows: ");
        rows = scanner.nextInt();
        System.out.print("Enter number of columns: ");
        cols = scanner.nextInt();


        totalMines = (rows * cols) / 3;
        totalSafeCells = (rows * cols) - totalMines;


        initializeBoard(rows, cols);


        initializeMines();
    }

    private void initializeMines() {
        Random random = new Random();
        int placedMines = 0;


        while (placedMines < totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (board[row][col] != -1) {
                board[row][col] = -1;
                placedMines++;
                updateNeighborCounts(row, col);
            }
        }
    }

    private void updateNeighborCounts(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isValidCell(newRow, newCol) && board[newRow][newCol] != -1) {
                    board[newRow][newCol]++;
                }
            }
        }
    }

    @Override
    public void displayBoard() {
        System.out.println("  " + getHeader());
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < cols; j++) {
                if (revealed[i][j]) {
                    if (board[i][j] == -1) {
                        System.out.print(RED + "* " + RESET);
                    } else {
                        System.out.print(GREEN + board[i][j] + " " + RESET);
                    }
                } else if (flagged[i][j]) {
                    System.out.print(YELLOW + "F " + RESET);
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    private String getHeader() {
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            header.append(i).append(" ");
        }
        return header.toString();
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean isGameOver = false;

        while (!isGameOver) {
            displayBoard();
            System.out.println("Enter your move (row column action): ");
            System.out.println("Action: 'R' to reveal, 'F' to flag/unflag");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            char action = scanner.next().charAt(0);

            if (!isValidCell(row, col)) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            if (action == 'R') {
                moves++;
                if (board[row][col] == -1) {
                    System.out.println("Boom! You hit a mine. Game over!");
                    revealall();
                    isGameOver = true;
                } else {
                    revealCell(row, col);
                    if (checkWin()) {
                        System.out.println("Congratulations! You cleared the board!");
                        isGameOver = true;
                    }
                }
            } else if (action == 'F') {
                moves++;
                flagged[row][col] = !flagged[row][col];
            }
        }
        endTime = System.currentTimeMillis();
        displayScore();
        displayBoard();
        scanner.close();
    }

    private void displayScore() {
        long takentime = (endTime - startTime) / 1000;
        double moveRatio = (double) moves / totalSafeCells;

        int score;
        if (moveRatio <= 1.2){
            score = 100;
        } else if (moveRatio <= 1.5) {
            score = 80;
        } else if (moveRatio <= 2.0) {
            score = 50;

        }
        else {
            score = 0;
        }
        System.out.println("Time taken: " + takentime + "seconds");
        System.out.println("number of moves:" + moves);
        System.out.println("your scorw: " + score);
    }

    private void revealCell(int row, int col) {
        if (!isValidCell(row, col) || revealed[row][col] || flagged[row][col]) {
            return;
        }

        revealed[row][col] = true;

        if (board[row][col] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revealCell(row + i, col + j);
                }
            }
        }
    }
    private void revealall() {
        for (int i=0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                revealed[i][j] = true;

            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!revealed[i][j] && board[i][j] != -1) {
                    return false;
                }
            }
        }
        return true;
    }
}