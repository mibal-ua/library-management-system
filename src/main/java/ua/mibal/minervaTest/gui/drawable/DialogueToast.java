package ua.mibal.minervaTest.gui.drawable;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class DialogueToast extends Toast {

    private final String trueAnswer;
    private final String falseAnswer;
    private boolean userAnswer;

    /**
     * if user select `getAnswer()` return:
     * trueAnswer: true;
     * falseAnswer: false.
     */
    public DialogueToast(final String question,
                         final String trueAnswer,
                         final String falseAnswer) {
        super(question);
        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
    }

    @Override
    public DialogueToast draw() {
        super.draw();
        return this;
    }

    @Override
    protected void printAppropriateBody() {
        printQuestion("1 - " + trueAnswer + ", 2 - " + falseAnswer);
        final String input = readInput();
        if (input.equals("1") || input.equals("2")) {
            userAnswer = input.equals("1");
        } else {
            draw();
        }
    }

    public boolean getAnswer() {
        return userAnswer;
    }
}
