package ua.mibal.minervaTest.component.console.drawable;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class InfoToast extends Toast {

    public InfoToast(final String message) {
        super(message);
    }

    @Override
    protected void appropriateBody() {
        waitToContinue();
    }
}
