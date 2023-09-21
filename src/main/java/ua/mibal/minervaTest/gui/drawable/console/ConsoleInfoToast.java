package ua.mibal.minervaTest.gui.drawable.console;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleInfoToast extends ConsoleToast {

    public ConsoleInfoToast(final String header,
                            final String body) {
        super(header, body);
    }

    public ConsoleInfoToast(final String header) {
        super(header);
    }


    @Override
    protected void printAppropriateBody() {
        waitToContinue(body);
    }
}
