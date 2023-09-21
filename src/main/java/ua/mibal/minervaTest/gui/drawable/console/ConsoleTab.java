package ua.mibal.minervaTest.gui.drawable.console;

import ua.mibal.minervaTest.gui.console.ConsoleUtils;
import ua.mibal.minervaTest.gui.drawable.Tab;
import ua.mibal.minervaTest.model.window.TabType;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.gui.console.ConsoleConstants.WINDOW_WIDTH;
import static ua.mibal.minervaTest.gui.console.ConsoleDataPrinter.bold;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleTab implements Tab {

    private final String[] headerWords;
    private final int boldWordIndex;
    private final Runnable body;
    private final TabType tabType;

    public ConsoleTab(Runnable body,
                      TabType tabType,
                      int boldWordIndex,
                      String... headerWords) {
        this.headerWords = headerWords;
        this.boldWordIndex = boldWordIndex;
        this.body = body;
        this.tabType = tabType;
    }

    public ConsoleTab(Runnable body,
                      TabType tabType,
                      String... headerWords) {
        this(body, tabType, 0, headerWords);
    }

    @Override
    public void draw() {
        ConsoleUtils.clear();
        System.out.println("\n" + implementHeaderWithSpaces() + "\n");
        requireNonNull(body).run();
        System.out.print("\n\n");
    }

    @Override
    public TabType getTabType() {
        return tabType;
    }

    private String implementHeaderWithSpaces() {
        final int symbolCount = Arrays.stream(headerWords)
                .map(String::length)
                .reduce(0, Integer::sum, Integer::sum);

        final int spaceLength = (WINDOW_WIDTH - symbolCount) / (headerWords.length + 1);
        final String space = " ".repeat(spaceLength);

        final StringBuilder header = new StringBuilder(space);
        for (int i = 0; i < headerWords.length; i++) {
            header.append(i == boldWordIndex
                            ? bold(headerWords[i])
                            : headerWords[i])
                    .append(space);
        }
        return header.toString();
    }
}
