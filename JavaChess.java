import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import Game;

public class JavaChess {
    public static void main(String[] args) {
        String userinput = "";
        Scanner scanner = new Scanner(System.in);
        while (!userinput.equals("start")) {
            System.out.println("Type 'start' to start the game");
            userinput = scanner.nextLine();
        }
        game(scanner);
    }

    public static void game(Scanner scanner) {
        Game game = new Game(scanner);
        game.PlayGame();
    }
}


