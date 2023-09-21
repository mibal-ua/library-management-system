package ua.mibal.minervaTest.gui.console;

import java.util.Scanner;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleUtils {

    private static final String BOLD = "\033[1m";

    private static final String RESET = "\033[0m";

    public static String[] readInput() {
        goTo(24, 0);
        System.out.print("> ");
        String input = new Scanner(System.in).nextLine();
        return input.split(" ");
    }

    public static void goTo(final int row, final int column) {
        char escCode = 0x1B;
        System.out.printf("%c[%d;%df", escCode, row, column);
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String bold(final String str) {
        return BOLD + str + RESET;
    }
}
