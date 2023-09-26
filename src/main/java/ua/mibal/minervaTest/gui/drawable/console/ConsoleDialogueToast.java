package ua.mibal.minervaTest.gui.drawable.console;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleDialogueToast extends ConsoleToast {

    private final String trueAnswer;
    private final String falseAnswer;
    private boolean userAnswer;

    /**
     * if user select `getAnswer()` return:
     * trueAnswer: true;
     * falseAnswer: false.
     */
    public ConsoleDialogueToast(final String header,
                                final String question,
                                final String trueAnswer,
                                final String falseAnswer) {
        super(header, question);
        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
    }

    @Override
    public ConsoleDialogueToast draw() {
        super.draw();
        return this;
    }

    @Override
    protected void printAppropriateBody() {
        printBody(body + "\n" +
                  "1 - " + trueAnswer + ", 2 - " + falseAnswer);
        final String input = readInput();
        if (input.equals("1") || input.equals("2"))
            userAnswer = input.equals("1");
        else
            draw();
    }

    public boolean getAnswer() {
        return userAnswer;
    }

    @Override
    public ConsoleDialogueToast perform(Runnable runnable) {
        super.perform(runnable);
        return this;
    }
}
