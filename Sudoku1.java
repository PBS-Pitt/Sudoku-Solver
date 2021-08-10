package sudoku1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Sudoku1 {

    public static void main(String[] args) throws FileNotFoundException {
        new Sudoku1();
    }

    private int[][] gameBoard = new int[9][9];
    private Scanner keyboard = new Scanner(System.in);

    public Sudoku1() throws FileNotFoundException {
        initializeGame();
        printGameBoard();
        System.out.println();

        while (!hasGameBeenSolved()) {
            int[] cellLocation = getNextCell();
            if (cellLocation != null) {
//                System.out.print("Enter number in (" + cellLocation[0] + "," + cellLocation[1] + "): ");
//                gameBoard[cellLocation[0]][cellLocation[1]] = keyboard.nextInt();
                gameBoard[cellLocation[0]][cellLocation[1]] = cellLocation[2];
                printGameBoard();
                System.out.println();
            } else {
                System.out.println("Difficult Level Sudoku Game. \n" + "You need to implement more rules to solve it...");
                break;
            }
        }

    }

    private void initializeGame() throws FileNotFoundException {
        File sudokuFile = new File("/Users/Pau/NetBeansProjects/sudoku1/src/sudoku1/sudokutable");
        Scanner sudokuScanner = new Scanner(sudokuFile);

        int counter = 0;

        while (sudokuScanner.hasNextLine()) {

            String[] lineNum = sudokuScanner.nextLine().trim().split(" ");

            for (int i = 0; i < 9; i++) {
                gameBoard[counter][i] = Integer.parseInt(lineNum[i]);

            }
            counter++;
        }
    }

    private void printGameBoard() {

        for (int i = 0; i < 9; i++) {
            for (int z = 0; z < 9; z++) {
                System.out.print(gameBoard[i][z]);
            }
            System.out.println();
        }

    }

    private boolean hasGameBeenSolved() {

        int counter = 0;

        for (int i = 0; i < 9; i++) {
            for (int z = 0; z < 9; z++) {
                if (gameBoard[i][z] == 0) {
                    return false;
                } else {
                    counter++;
                }
            }
        }

        if (counter == 81) {
            return true;
        } else {
            return false;
        }
    }

    private int[] getNextCell() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gameBoard[row][col] == 0) {
                    Set<Integer> firstRuleElimination = getFirstRuleElimination(row);
                    Set<Integer> secondRuleElimination = getSecondRuleElimination(col);
                    Set<Integer> thirdRuleElimination = getThirdRuleElimination(row, col);
                    Set<Integer> survivors = getSurvivors(firstRuleElimination, secondRuleElimination, thirdRuleElimination, row, col);
                    if (survivors.size() == 1) {
                        List<Integer> list = new ArrayList(survivors);
                        int uniqueValue = list.get(0);
                        return new int[]{row, col, uniqueValue};
                    }
                }

            }
        }

        return null;

    }

    private Set<Integer> getFirstRuleElimination(int row) {

        Set<Integer> numbersInRow = new HashSet();

        for (int i = 0; i < 9; i++) {
            if (gameBoard[row][i] != 0) {
                numbersInRow.add(gameBoard[row][i]);
            }
        }
        return numbersInRow;

    }

    private Set<Integer> getSecondRuleElimination(int column) {

        Set<Integer> numbersInCol = new HashSet();

        for (int i = 0; i < 9; i++) {
            if (gameBoard[i][column] != 0) {
                numbersInCol.add(gameBoard[i][column]);
            }
        }
        return numbersInCol;

    }

    private Set<Integer> getThirdRuleElimination(int row, int column) {
        Set<Integer> numbersInCell = new HashSet();

        if ((getRowRangeForGroup(row) != null) && (getColRangeForGroup(column) != null)) {
            int[] rowGroup = getRowRangeForGroup(row);
            int[] colGroup = getColRangeForGroup(column);

            for (int i = rowGroup[0]; i <= rowGroup[1]; i++) {
                for (int z = colGroup[0]; z <= colGroup[1]; z++) {
                    if (gameBoard[row][i] != 0) {
                        numbersInCell.add(gameBoard[row][i]);
                    }
                }
            }
        }
        return numbersInCell;
    }

    private int[] getRowRangeForGroup(int row) {

        if ((row <= 2) && (row >= 0)) {
            return new int[]{0, 2};
        }
        if ((row <= 3) && (row >= 5)) {
            return new int[]{3, 5};
        }
        if ((row <= 6) && (row >= 8)) {
            return new int[]{6, 8};
        }
        return null;
    }

    private int[] getColRangeForGroup(int column) {

        if ((column <= 2) && (column >= 0)) {
            return new int[]{0, 2};

        }
        if ((column <= 3) && (column >= 5)) {
            return new int[]{3, 5};

        }
        if ((column <= 6) && (column >= 8)) {
            return new int[]{6, 8};
        }
        return null;
    }

    private Set<Integer> getSurvivors(Set<Integer> firstRuleElimination, Set<Integer> secondRuleElimination, Set<Integer> thirdRuleElimination, int row, int column) {

        Set<Integer> invalidNumbers = new HashSet();
        Set<Integer> survivors = new HashSet();

        invalidNumbers.addAll(getFirstRuleElimination(row));
        invalidNumbers.addAll(getSecondRuleElimination(column));
        invalidNumbers.addAll(getThirdRuleElimination(row, column));

        for (int i = 1; i < 10; i++) {
            if (!(invalidNumbers.contains(i))) {
                survivors.add(i);
            }
        }
        return survivors;
    }

}
