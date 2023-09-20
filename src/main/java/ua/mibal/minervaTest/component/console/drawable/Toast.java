package ua.mibal.minervaTest.component.console.drawable;

import java.util.Scanner;

import static ua.mibal.minervaTest.component.console.ConsoleConstants.TOAST_HEIGHT;
import static ua.mibal.minervaTest.component.console.ConsoleConstants.UPPER_START;
import static ua.mibal.minervaTest.component.console.ConsoleConstants.WINDOW_WIDTH;
import static ua.mibal.minervaTest.component.console.ConsoleUtils.goTo;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
abstract class Toast {

    private final String message;

    public Toast(final String message) {
        this.message = message;
    }

    public Toast draw() {
        printWindowBackground();
        printMessage(message);

        appropriateBody();

        return this;
    }

    protected void printWindowBackground() {
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

    protected void printMessage(final String message) {
        // TODO add multiline feature
//            if (message.length() > TOAST_WIDTH - 4) {
//                final String[] messageWords = message.split(" ");
//                final int linesCount = message.length() / (TOAST_WIDTH - 4) + 1;
//
//                final List<String> lines = new ArrayList<>();
//
//                int indexes = messageWords.length / linesCount;
//
//                int from = 0;
//                int to = indexes;
//                for (int i = 0; i < linesCount; i++) {
//                    final String line = String.join(
//                            " ", Arrays.copyOfRange(
//                                    messageWords, from, to)
//                    );
//                    lines.add(line);
//                    from += indexes;
//                    to += indexes;
//                }
//                for (int i = lines.size() - 1; i >= 0; i--) {
//                    String line = lines.get(i);
//                    goTo(UPPER_START + 2 + i, (WINDOW_WIDTH - line.length()) / 2);
//                    System.out.print(line);
//                }
//                return;
//            }
        goTo(UPPER_START + 3, (WINDOW_WIDTH - message.length()) / 2);
        System.out.print(message);
    }

    protected void printQuestion(final String question) {
        // TODO add multiline feature
        goTo(UPPER_START + 5, (WINDOW_WIDTH - question.length()) / 2);
        System.out.print(question);
    }

    protected void waitToContinue() {
        printQuestion("Click enter to continue...");
        goTo(UPPER_START + 6, WINDOW_WIDTH / 2);
        new Scanner(System.in).nextLine();
    }

    protected String readInput() {
        goTo(UPPER_START + 6, WINDOW_WIDTH / 2 - 5);
        return new Scanner(System.in).nextLine();
    }

    protected abstract void appropriateBody();
}
