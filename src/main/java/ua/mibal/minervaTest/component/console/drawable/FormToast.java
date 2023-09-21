package ua.mibal.minervaTest.component.console.drawable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class FormToast extends Toast {

    private final Iterator<String> questions;
    private final String stopCommand;
    private List<String> answers;

    public FormToast(final String info,
                     final List<String> questions,
                     final String stopCommand) {
        super(info);
        this.questions = questions.iterator();
        this.stopCommand = stopCommand;
    }

    @Override
    public FormToast draw() {
        super.draw();
        return this;
    }

    @Override
    protected void printAppropriateBody() {
        waitToContinue();

        final List<String> result = new ArrayList<>();
        while (questions.hasNext()) {
            printWindowBackground();
            printMessage(questions.next());
            String input = readInput();
            if (input.equals(stopCommand)) return;
            result.add(input);
        }
        answers = result;
    }

    public Optional<List<String>> getAnswers() {
        return Optional.ofNullable(answers);
    }
}
