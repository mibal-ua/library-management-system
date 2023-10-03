package ua.mibal.minervaTest.gui.drawable.console.tab;

import ua.mibal.minervaTest.gui.drawable.console.ConsoleTab;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleListTab extends ConsoleTab {

    public <T extends Entity> ConsoleListTab(Runnable runnable,
                                             DataType dataType) {
        super(
                runnable,
                TabType.getRootTabOf(dataType),
                switch (dataType) {
                    case BOOK -> 0;
                    case CLIENT -> 1;
                    case HISTORY -> 2;
                    case NULL -> throw new IllegalArgumentException("Illegal DataType for ConsoleListTab");
                },
                "BOOKS", "CLIENTS", "HISTORY"
        );
    }
}
