package ua.mibal.minervaTest.gui.drawable.console;

import ua.mibal.minervaTest.utils.StringUtils;

import java.util.List;
import java.util.Scanner;

import static ua.mibal.minervaTest.gui.console.ConsoleConstants.TOAST_HEIGHT;
import static ua.mibal.minervaTest.gui.console.ConsoleConstants.TOAST_WIDTH;
import static ua.mibal.minervaTest.gui.console.ConsoleConstants.UPPER_START;
import static ua.mibal.minervaTest.gui.console.ConsoleConstants.WINDOW_WIDTH;
import static ua.mibal.minervaTest.gui.console.ConsoleUtils.bold;
import static ua.mibal.minervaTest.gui.console.ConsoleUtils.goTo;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public abstract class ConsoleToast {

    protected final String header;
    protected final String body;

    public ConsoleToast(final String header, String body) {
        this.header = header;
        this.body = body;
    }

    public ConsoleToast(final String header) {
        this(header, "");
    }

    public ConsoleToast draw() {
        printBackground();
        printHeader(header);
        printAppropriateBody();
        return this;
    }

    protected void printBackground() {
        int paddingLeft = 11;

        goTo(UPPER_START, paddingLeft);
        System.out.print("+----------------------------------------------------------+");
        for (int i = 1; i <= TOAST_HEIGHT - 2; i++) {
            goTo(UPPER_START + i, paddingLeft);
            System.out.print("|                                                          |");
        }
        goTo(UPPER_START + TOAST_HEIGHT - 1, paddingLeft);
        System.out.print("+----------------------------------------------------------+");
    }

    // TODO add multiline feature
    protected void printHeader(final String message) {
        goTo(UPPER_START + 2, (WINDOW_WIDTH - message.length()) / 2);
        System.out.print(bold(message));
    }

    protected void printBody(final String body) {
        List<String> lines = StringUtils.divideStrToLines(body.trim(), TOAST_WIDTH - 4);

        if (lines.size() > 3)
            throw new IllegalArgumentException("So big body string='" + body + "'");

        int i = 3;
        for (String line : lines) {
            goTo(UPPER_START + i++, (WINDOW_WIDTH - line.length()) / 2);
            System.out.print(line);
        }
    }

    protected void waitToContinue(String body) {
        String toContinue = "Click enter to continue...";
        printBody(body.isEmpty()
                ? toContinue
                : body + "\n" + toContinue);
        goTo(UPPER_START + 6, WINDOW_WIDTH / 2);
        new Scanner(System.in).nextLine();
    }

    protected String readInput() {
        goTo(UPPER_START + 6, WINDOW_WIDTH / 2 - 5);
        return new Scanner(System.in).nextLine();
    }

    public ConsoleToast perform(Runnable runnable) {
        runnable.run();
        return this;
    }

    protected abstract void printAppropriateBody();
}
