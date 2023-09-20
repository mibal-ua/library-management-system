package ua.mibal.minervaTest.component.console.drawable;

import static java.lang.String.format;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class DialogueToast extends Toast {

    private final String answer1;

    private final String answer2;

    private boolean userAnswer;

    /**
     * if user select `getAnswer()` return:
     * answer1: true;
     * answer2: false.
     */
    public DialogueToast(final String question,
                         final String answer1,
                         final String answer2) {
        super(question);
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    @Override
    public DialogueToast draw() {
        super.draw();
        return this;
    }

    @Override
    protected void appropriateBody() {
        printQuestion(format("1 - %s, 2 - %s", answer1, answer2));

        final String input = readInput();

        if (input.equals("1")) {
            userAnswer = true;
            return;
        }
        if (input.equals("2")) {
            userAnswer = false;
            return;
        }
        draw();
    }

    public boolean getAnswer() {
        return userAnswer;
    }
}
