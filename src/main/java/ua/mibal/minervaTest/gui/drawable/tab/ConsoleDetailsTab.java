package ua.mibal.minervaTest.gui.drawable.tab;

import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleDetailsTab extends ConsoleTab {

    public <T extends Entity> ConsoleDetailsTab(Runnable runnable,
                                                DataType dataType,
                                                Supplier<Optional<T>> optionalSupplier) {

        super(
                runnable,
                TabType.getDetailsTabOf(dataType),
                optionalSupplier.get()
                        .map(Entity::getId)
                        .orElse(null),
                dataType.simpleName().toUpperCase() + " DETAILS"
        );
    }
}
