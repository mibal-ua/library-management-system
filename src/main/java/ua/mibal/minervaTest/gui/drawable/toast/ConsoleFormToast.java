package ua.mibal.minervaTest.gui.drawable.toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleFormToast extends ConsoleToast {

    private final Map<String, String> questions;
    private final String stopCommand;
    private final String[] instructions;
    private List<String> answers;

    public ConsoleFormToast(final String header,
                            final Map<String, String> questions,
                            final String stopCommand,
                            final String... instructions) {
        super(header);
        this.questions = questions;
        this.stopCommand = stopCommand;
        this.instructions = instructions;
    }

    @Override
    public ConsoleFormToast draw() {
        printAppropriateBody();
        return this;
    }

    @Override
    protected void printAppropriateBody() {
        for (String instruction : instructions)
            new ConsoleInfoToast(header, instruction)
                    .draw();
        final List<String> result = new ArrayList<>();

        for (Map.Entry<String, String> question : questions.entrySet()) {
            printBackground();
            printHeader(question.getKey());
            printBody(question.getValue());

            String input = readInput();
            if (input.equals(stopCommand)) return;
            result.add(input);
        }
        answers = result;
    }

    public Optional<List<String>> getAnswers() {
        return Optional.ofNullable(answers);
    }

    @Override
    public ConsoleFormToast perform(Runnable runnable) {
        super.perform(runnable);
        return this;
    }
}
