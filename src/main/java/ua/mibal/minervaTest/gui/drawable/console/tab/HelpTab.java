package ua.mibal.minervaTest.gui.drawable.console.tab;

import ua.mibal.minervaTest.gui.drawable.console.ConsoleTab;

import static ua.mibal.minervaTest.model.window.TabType.HELP_TAB;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class HelpTab extends ConsoleTab {

    public HelpTab(Runnable runnable) {
        super(
                runnable,
                HELP_TAB,
                "HELP"
        );
    }
}
