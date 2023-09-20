package ua.mibal.minervaTest.component.console;

import java.util.Scanner;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleUtils {

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
}
