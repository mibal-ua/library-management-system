package ua.mibal.minervaTest.gui.drawable.console;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleInfoToast extends ConsoleToast {

    public ConsoleInfoToast(final String message) {
        super(message);
    }

    @Override
    protected void printAppropriateBody() {
        waitToContinue();
    }
}
