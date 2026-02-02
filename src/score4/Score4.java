package score4;

import java.util.Scanner;

public class Score4 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to Score 4");

        // --- 1. Pairnei kai kanei validate ta player names ---
        int numOfPlayers = 0;
        while (numOfPlayers < 2 || numOfPlayers > 4) {
            System.out.print("Choose the number of players (2, 3, or 4): ");
            if (input.hasNextInt()) {
                numOfPlayers = input.nextInt();
                if (numOfPlayers < 2 || numOfPlayers > 4) {
                    System.out.println("Error: Choose between 2 and 4 players.");
                }
            } else {
                System.out.println("Error: Invalid input, please enter a number.");
                input.next(); 
            }
        }

        // --- 2. Pairnei ta player names kai episis checkarei gia ton an exoun mpei swstoi xaraktires---
        String[] playerNames = new String[numOfPlayers];
        char[] playerTokens = {'X', 'O', '#', '$'};
        input.nextLine(); 

        for (int i = 0; i < numOfPlayers; i++) {
            boolean uniqueName = false;
            while (!uniqueName) {
                System.out.print("Enter name for Player " + (i + 1) + ": ");
                String nameInput = input.nextLine().trim();

                if (nameInput.isEmpty()) {
                    System.out.println("Name cannot be empty.");
                    continue;
                }

                // Checkarei gia dipla onomata
                boolean exists = false;
                for (int j = 0; j < i; j++) { 
                    if (playerNames[j].equalsIgnoreCase(nameInput)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println("Error: That name is already taken! Please choose a different one.");
                } else {
                    playerNames[i] = nameInput;
                    uniqueName = true;
                }
            }
        }

        // --- 3. Orizei to board size ---
        int rows = 0;
        int cols = 0;
        if (numOfPlayers == 2) {
            rows = 6; cols = 7;
        } else if (numOfPlayers == 3) {
            rows = 7; cols = 9;
        } else if (numOfPlayers == 4) {
            rows = 7; cols = 12;
        }

        // --- 4. Arxikopoiei to Board ---
        char[][] board = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = '.';
            }
        }

        boolean gameWon = false;
        boolean boardFull = false;
        int currentPlayerIndex = 0;
        
        // Metabliti metriti strofwn
        int totalTurns = 0; 

        // --- 5. Kurios brogxos paixnidiou ---
        while (!gameWon && !boardFull) {
            printBoard(board, rows, cols);

            System.out.println("Turn " + (totalTurns + 1)); // Deixnei ta total turns
            System.out.println(playerNames[currentPlayerIndex] + " (" + playerTokens[currentPlayerIndex] + "), it's your turn.");
            
            int chosenCol = -1;
            boolean validMove = false;

            while (!validMove) {
                System.out.print("Enter column (1-" + cols + "): ");
                
                if (input.hasNextInt()) {
                    chosenCol = input.nextInt() - 1; 

                    if (chosenCol < 0 || chosenCol >= cols) {
                        System.out.println("Invalid! Column must be between 1 and " + cols);
                    } else if (board[0][chosenCol] != '.') {
                        System.out.println("That column is full! Try another.");
                    } else {
                        // Gravity Logic
                        for (int r = rows - 1; r >= 0; r--) {
                            if (board[r][chosenCol] == '.') {
                                board[r][chosenCol] = playerTokens[currentPlayerIndex];
                                validMove = true;
                                totalTurns++; // Kanei increase ta total turns edw
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("Error: Please enter a whole number.");
                    input.next(); 
                }
            }

            // --- 6. Checkarei gia niki ---
            if (checkWin(board, rows, cols, playerTokens[currentPlayerIndex])) {
                gameWon = true;
            }

            // --- 7. Checkarei gia isopalia ---
            if (!gameWon) {
                boolean foundEmpty = false;
                for (int c = 0; c < cols; c++) {
                    if (board[0][c] == '.') foundEmpty = true;
                }
                if (!foundEmpty) boardFull = true;
            }

            // --- 8. Xeirismos apotelesmatwn ---
            if (gameWon) {
                printBoard(board, rows, cols);
                System.out.println("\nVICTORY! " + playerNames[currentPlayerIndex] + " has won the game!");
                System.out.println("Total turns played: " + totalTurns); // <--- Ektiposi sinolikon strofon
            } else if (boardFull) {
                printBoard(board, rows, cols);
                System.out.println("\nDRAW! The board is full.");
                System.out.println("Total turns played: " + totalTurns); // <--- Ektiposi sinolikon strofon
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % numOfPlayers;
            }
        }
        input.close();
    }

    // --- Boithitikes methodoi ---

    public static void printBoard(char[][] board, int rows, int cols) {
        System.out.println("\n--- Current Board ---");
        for (int r = 0; r < rows; r++) {
            System.out.print("| ");
            for (int c = 0; c < cols; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println("|");
        }
        System.out.print("  ");
        for (int c = 1; c <= cols; c++) {
            System.out.print(c + (c >= 10 ? "" : " "));
        }
        System.out.println("\n");
    }

    public static boolean checkWin(char[][] board, int rows, int cols, char t) {
        // Gia Horizontal
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - 4; c++) {
                if (board[r][c] == t && board[r][c+1] == t && board[r][c+2] == t && board[r][c+3] == t) return true;
            }
        }
        // Gia Vertical
        for (int r = 0; r <= rows - 4; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == t && board[r+1][c] == t && board[r+2][c] == t && board[r+3][c] == t) return true;
            }
        }
        // Gia Diagonals
        for (int r = 0; r <= rows - 4; r++) {
            for (int c = 0; c <= cols - 4; c++) {
                if (board[r][c] == t && board[r+1][c+1] == t && board[r+2][c+2] == t && board[r+3][c+3] == t) return true;
            }
        }
        for (int r = 3; r < rows; r++) {
            for (int c = 0; c <= cols - 4; c++) {
                if (board[r][c] == t && board[r-1][c+1] == t && board[r-2][c+2] == t && board[r-3][c+3] == t) return true;
            }
        }
        return false;
    }
}