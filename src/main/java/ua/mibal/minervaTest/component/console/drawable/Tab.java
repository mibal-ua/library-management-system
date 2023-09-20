package ua.mibal.minervaTest.component.console.drawable;

import ua.mibal.minervaTest.component.console.ConsoleUtils;
import ua.mibal.minervaTest.model.window.TabType;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.component.console.ConsoleConstants.WINDOW_WIDTH;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.bold;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Tab {

    private final String[] tabsNames;
    private final int boldTab;
    private final Runnable body;
    private final TabType currentTabState;

    public Tab(Runnable body,
               TabType currentTabState,
               int boldTabIndex,
               String... tabsNames) {
        this.tabsNames = tabsNames;
        this.boldTab = -1 < boldTabIndex && boldTabIndex < tabsNames.length
                ? boldTabIndex
                : -1;
        this.body = body;
        this.currentTabState = currentTabState;
    }

    public Tab(Runnable body,
               TabType currentTabState,
               String... tabsNames) {
        this(body, currentTabState, 0, tabsNames);
    }

    public void draw() {
        ConsoleUtils.clear();

        // header
        final int allWordsLength = Arrays
                .stream(tabsNames)
                .reduce(0,
                        (acc, str) -> acc + str.length(),
                        Integer::sum);
        final int spaceLength = (WINDOW_WIDTH - allWordsLength) / (tabsNames.length + 1);
        final String space = " ".repeat(spaceLength);

        final StringBuilder message = new StringBuilder(space);
        for (int i = 0; i < tabsNames.length; i++) {
            String tabsName = i == boldTab
                    ? bold(tabsNames[i])
                    : tabsNames[i];
            message.append(tabsName)
                    .append(space);
        }

        System.out.println();
        System.out.println(message);
        System.out.println();

        // body
        requireNonNull(body).run();

        // margin afterAll
        System.out.println();
        System.out.println();
    }

    public TabType getCurrentTabState() {
        return currentTabState;
    }
}
