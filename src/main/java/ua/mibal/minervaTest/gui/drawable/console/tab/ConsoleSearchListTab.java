package ua.mibal.minervaTest.gui.drawable.console.tab;

import ua.mibal.minervaTest.gui.drawable.console.ConsoleTab;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.TabType;

import static java.lang.String.join;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleSearchListTab extends ConsoleTab {

    public <T extends Entity> ConsoleSearchListTab(Runnable runnable,
                                                   TabType tabType,
                                                   String[] args) {
        super(
                runnable,
                tabType,
                String.format("SEARCH IN " + tabType.name() + " BY '%s'", join(" ", args))
        );
    }
}
