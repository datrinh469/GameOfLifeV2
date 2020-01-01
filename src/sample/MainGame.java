/****************************************************************************************
 file: MainGame.java
 author: Dylan Trinh
 class: CS 1400: Introduction to Programming and Problem Solving

 Assignment: Program 5
 Date Last Modified: 12/03/2019

 Purpose: This program takes in a file with a .life extension and an integer
 of how many turns to simulates Conway's Game of Life. The output will be on screen.
 ***************************************************************************************/

package sample;

import java.util.*;
import java.io.*;

public class MainGame {

    private PrintWriter resultPrint;
    private int columns;
    private int rows;
    private char[][] gameBoard;

    public MainGame() {
        try {
            Scanner scnr = new Scanner(System.in);
            FileWriter resultFile = new FileWriter("generationsResult.txt", false);
            resultPrint = new PrintWriter(resultFile);

            // retrieves file and prepares to create 2-d array
            System.out.print("Enter file name: ");
            String fileName = scnr.nextLine();
            File mainFile = new File(fileName);
            Scanner gameFile = new Scanner(mainFile);
            resultPrint.println("Enter file name: " + fileName + "\n");

            // retrieves number of generations to create and print
            System.out.print("\nEnter how many generations to compute: ");
            int generation = scnr.nextInt();
            System.out.println("\n");
            resultPrint.println("Enter how many generations to compute: " + generation + "\n");

            // creates game board by grabbing number of rows and columns from file
            columns = gameFile.nextInt();
            rows = gameFile.nextInt();
            gameBoard = new char[rows][columns];
            gameFile.nextLine();

            // fills array with chars from file
            for (int i = 0; i < rows; i++) {
                for (int k = 0; k < columns; k++) {
                    gameBoard[i][k] = gameFile.next().charAt(0);
                }
                gameFile.nextLine();
            }

            //closes file and automatically starts computeNextGeneration and prints result into new file
            gameFile.close();
            computeNextGeneration(generation);
            resultPrint.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // method: getColumns
    // purpose: this method returns the number of columns from the game board from this instance of class MainGame
    public int getColumns() {
        return columns;
    }

    // method: getRows
    // purpose: this method returns the number of rows from the game board from this instance of class MainGame
    public int getRows() {
        return rows;
    }

    // method: getCell
    // purpose: this method returns an integer value of one if the specific cell on the game board is
    // "alive" and returns a zero if it is "dead". Returns a zero if the cell in question does not exist.
    public int getCell(int row, int column) {
        if (row < rows && row >= 0 && column < columns && column >= 0) {
            char letter = gameBoard[row][column];
            if (letter == 'X') {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // method: setCell
    // purpose: this method sets a cell within the game board as 'X' if the value given is greater than
    // 0 and as '0' if the value given is zero
    public void setCell(int row, int column, int value) {
        char letter;
        if (value > 0) {
            letter = 'X';
        } else {
            letter = '0';
        }
        gameBoard[row][column] = letter;
    }

    // method: computeNextGeneration
    // purpose: this recursive method prints out the generation number and modified game board onto the screen for the
    // user to see how their original board changed over time according to the rules in Conway's Game of Life
    // and prints out results into a .txt file
    public void computeNextGeneration(int generation) {
        if (generation == 1) {
            System.out.println("Generation 1\n");
            print();
            System.out.println();
            createFile(resultPrint, generation);
        } else {
            generation--;
            computeNextGeneration(generation);
            gameBoard = createNewGeneration();
            generation++;
            System.out.println("Generation " + generation + "\n");
            print();
            System.out.println();
            createFile(resultPrint, generation);
        }
    }

    // method: createNextGeneration
    // purpose: this private method assists computeNextGeneration by running the actual code that transforms the
    // original game board into the new game board for each generation and returns the new game board
    private char[][] createNewGeneration() {
        char[][] tempBoard = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < columns; k++) {
                if (i == 0 && k == 0) {
                    tempBoard[i][k] = checkTopLeft();
                } else if (i == 0 && k == columns - 1) {
                    tempBoard[i][k] = checkTopRight();
                } else if (i == rows - 1 && k == 0) {
                    tempBoard[i][k] = checkBottomLeft();
                } else if (i == rows - 1 && k == columns - 1) {
                    tempBoard[i][k] = checkBottomRight();
                } else if (i == 0) {
                    tempBoard[i][k] = checkTopRow(k);
                } else if (i == rows - 1) {
                    tempBoard[i][k] = checkBottomRow(k);
                } else if (k == 0) {
                    tempBoard[i][k] = checkLeftColumn(i);
                } else if (k == columns - 1) {
                    tempBoard[i][k] = checkRightColumn(i);
                } else {
                    tempBoard[i][k] = checkCenter(i, k);
                }
            }
        }
        return tempBoard;
    }

    // method: checkTopLeft
    // purpose: this private method determines whether the top left char of the game board is "dead" or "alive"
    // by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkTopLeft() {
        int count = checkRightCell(0, 0) + checkBottomCell(0, 0) +
                checkBottomRightCell(0, 0);
        return deadOrAlive(0, 0, count);
    }

    // method: checkTopRight
    // purpose: this private method determines whether the top right char of the game board is "dead" or "alive"
    // by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkTopRight() {
        int count = checkLeftCell(0, columns - 1) + checkBottomCell(0, columns - 1) +
                checkBottomLeftCell(0, columns - 1);
        return deadOrAlive(0, columns - 1, count);
    }

    // method: checkBottomLeft
    // purpose: this private method determines whether the bottom left char of the game board is "dead" or "alive"
    // by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkBottomLeft() {
        int count = checkTopCell(rows - 1, 0) + checkTopRightCell(rows - 1, 0) +
                checkRightCell(rows - 1, 0);
        return deadOrAlive(rows - 1, 0, count);
    }

    // method: checkBottomRight
    // purpose: this private method determines whether the bottom right char of the game board is "dead" or "alive"
    // by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkBottomRight() {
        int count = checkLeftCell(rows - 1, columns - 1) + checkTopCell(rows - 1, columns - 1)
                + checkTopLeftCell(rows - 1, columns - 1);
        return deadOrAlive(rows - 1, columns - 1, count);
    }

    // method: checkTopRow
    // purpose: this private method determines whether the top row of chars of the game board are "dead" or "alive"
    // by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkTopRow(int column) {
        int count = checkLeftCell(0, column) + checkBottomLeftCell(0, column) +
                checkBottomCell(0, column) + checkBottomRightCell(0, column) + checkRightCell(0, column);
        return deadOrAlive(0, column, count);
    }

    // method: checkBottomRow
    // purpose: this private method determines whether the bottom row of chars of the game board are "dead" or
    // "alive" by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkBottomRow(int column) {
        int count = checkLeftCell(rows - 1, column) + checkTopLeftCell(rows - 1, column) +
                checkTopCell(rows - 1, column) + checkTopRightCell(rows - 1, column) +
                checkRightCell(rows - 1, column);
        return deadOrAlive(rows - 1, column, count);
    }

    // method: checkLeftColumn
    // purpose: this private method determines whether the left column of chars of the game board are "dead" or
    // "alive" by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkLeftColumn(int row) {
        int count = checkTopCell(row, 0) + checkTopRightCell(row, 0) + checkRightCell(row, 0) +
                checkBottomRightCell(row, 0) + checkBottomCell(row, 0);
        return deadOrAlive(row, 0, count);
    }

    // method: checkRightColumn
    // purpose: this private method determines whether the right column of chars of the game board are "dead"
    // or "alive" by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkRightColumn(int row) {
        int count = checkLeftCell(row, columns - 1) + checkTopCell(row, columns - 1) +
                checkTopLeftCell(row, columns - 1) + checkBottomLeftCell(row, columns - 1) +
                checkBottomCell(row, columns - 1);
        return deadOrAlive(row, columns - 1, count);
    }

    // method: checkCenter
    // purpose: this private method determines whether the chars not at the border of the game board are
    // "dead" or "alive" by counting the number or "alive" cells neighboring it and running the deadOrAlive method
    private char checkCenter(int row, int column) {
        int count = checkTopLeftCell(row, column) + checkTopCell(row, column) + checkTopRightCell(row, column) +
                checkRightCell(row, column) + checkBottomRightCell(row, column) + checkBottomCell(row, column) +
                checkBottomLeftCell(row, column) + checkLeftCell(row, column);
        return deadOrAlive(row, column, count);
    }

    // method: deadOrAlive (by Bon Jovi)
    // purpose: this private method determines whether or not the specific cell is "dead" or "alive" by testing it
    // under Conway's Game of Life framework
    private char deadOrAlive(int row, int column, int count) {
        if (gameBoard[row][column] == '0') {
            if (count == 3) {
                return 'X';
            }
            return '0';
        } else {
            if (count < 2 || count > 3) {
                return '0';
            }
            return 'X';
        }
    }

    // method: checkTopLeftCell
    // purpose: this method determines whether or not the top left cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkTopLeftCell(int row, int column) {
        return getCell(row - 1, column - 1);
    }

    // method: checkTopCell
    // purpose: this method determines whether or not the top cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkTopCell(int row, int column) {
        return getCell(row - 1, column);
    }

    // method: checkTopRightCell
    // purpose: this method determines whether or not the top right cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkTopRightCell(int row, int column) {
        return getCell(row - 1, column + 1);
    }

    // method: checkLeftCell
    // purpose: this method determines whether or not the left cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkLeftCell(int row, int column) {
        return getCell(row, column - 1);
    }

    // method: checkRightCell
    // purpose: this method determines whether or not the right cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkRightCell(int row, int column) {
        return getCell(row, column + 1);
    }

    // method: checkBottomLeftCell
    // purpose: this method determines whether or not the bottom left cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkBottomLeftCell(int row, int column) {
        return getCell(row + 1, column - 1);
    }

    // method: checkBottomCell
    // purpose: this method determines whether or not the bottom cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkBottomCell(int row, int column) {
        return getCell(row + 1, column);
    }

    // method: checkBottomRightCell
    // purpose: this method determines whether or not the bottom right cell relative to this cell is "dead" or "alive"
    // by running the getCell method
    private int checkBottomRightCell(int row, int column) {
        return getCell(row + 1, column + 1);
    }

    // method: print
    // purpose: this method prints out the game board onto the screen for the user to see that current generation
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < columns; k++) {
                System.out.print(gameBoard[i][k] + " ");
            }
            System.out.println();
        }
    }

    // method: createFile
    // purpose: creates a .txt file that captures all interactions with the user in the compiler
    private void createFile(PrintWriter result, int generation) {
        result.println("Generation " + generation + "\n");
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < columns; k++) {
                result.print(gameBoard[i][k] + " ");
            }
            result.println();
        }
        result.println();
    }


    public static void main(String[] args) {
        // creates instance of class to run when program is ran
        MainGame main = new MainGame();
    }
}