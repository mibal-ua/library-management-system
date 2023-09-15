/*
 * Copyright (c) 2023. http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.mibal.minervaTest.component.console;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.of;
import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.bold;
import static ua.mibal.minervaTest.model.window.TabType.HELP_TAB;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_HISTORY;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.TabType.TAB_1;
import static ua.mibal.minervaTest.model.window.TabType.TAB_2;
import static ua.mibal.minervaTest.model.window.TabType.TAB_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class NewConsoleWindowManager implements WindowManager {

    public final static int WINDOW_WIDTH = 80;
    public final static int WINDOW_HEIGHT = 25;

    private final DataPrinter dataPrinter;
    private final Stack<Tab> tabStack = new TabsStack();

    public NewConsoleWindowManager(DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    @Override
    public void tab1(Supplier<List<Book>> booksSupplier) {
        tabStack.push(new Tab(
                new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                0,
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                TAB_1
        )).draw();
    }

    @Override
    public void tab2(Supplier<List<Client>> clientsSupplier) {
        tabStack.push(new Tab(
                new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                1,
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                TAB_2
        )).draw();
    }

    @Override
    public void tab3(Supplier<List<Operation>> operationsSupplier) {
        tabStack.push(new Tab(
                new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                2,
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                TAB_3
        )).draw();
    }

    @Override
    public void searchBookTab(final Supplier<List<Book>> booksSupplier, final String[] args) {
        final String header = format("SEARCH IN BOOKS BY '%s'", join(" ", args));
        tabStack.push(new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                SEARCH_BOOK
        )).draw();
    }

    @Override
    public void searchClientTab(final Supplier<List<Client>> clientsSupplier, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        tabStack.push(new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                SEARCH_CLIENT
        )).draw();
    }

    @Override
    public void searchOperationTab(final Supplier<List<Operation>> operationsSupplier, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        tabStack.push(new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                SEARCH_HISTORY
        )).draw();
    }

    @Override
    public void help() {
        tabStack.push(new Tab(
                new String[]{"HELP"},
                0,
                () -> System.out.println("""

                                                          MAIN CONTROL
                        --------------------------------------------------------------------------------

                                                       1, 2, 3 - open tab

                                                          exit - to exit


                                                            IN TABS
                        --------------------------------------------------------------------------------

                                  search(s) ${query} - search element in current tab

                                          look ${id} - look at concrete item in list in current tab

                                                 add - to add element into list in current tab

                                   delete(del) ${id} - to delete element in list in current tab


                                                     IN CONCRETE BOOK/CLIENT
                        --------------------------------------------------------------------------------

                                                   edit - to edit this book/client

                                            delete(del) - to delete this book/client

                                                    esc - go to previous window


                                                       IN CONCRETE CLIENT
                        --------------------------------------------------------------------------------

                                                    take ${id} - to take book

                                                  return ${id} - to return book

                         """),
                HELP_TAB
        )).draw();
    }

    @Override
    public String[] readCommandLine() {
        goTo(24, 0);
        System.out.print("> ");
        String input = new Scanner(System.in).nextLine();
        return input.split(" ");
    }

    private void goTo(final int row, final int column) {
        char escCode = 0x1B;
        System.out.printf("%c[%d;%df", escCode, row, column);
    }

    @Override
    public void showToast(String message) {
        new InfoToast(message).draw();
    }

    @Override
    public boolean showDialogueToast(String question, String answer1, String answer2) {
        return new DialogueToast(question, answer1, answer2)
                .draw()
                .getAnswer();
    }


    @Override
    public Optional<Book> initBookToAdd() {
        List<String> questions = of(
                "Enter book title",
                "Enter subtitle",
                "Enter author",
                "Enter publish date (in format '2018-12-04')",
                "Enter book publisher"
        );

        Optional<List<String>> answers = new FormToast(
                "Lets create book! You can stop everywhere by entering '/stop'", questions, "/stop"
        ).draw().getAnswers();

        if (answers.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Book(
                iterator.next(),
                iterator.next(),
                iterator.next(),
                // TODO FIXME stub
                LocalDateTime.now(), // iterator.next(),
                iterator.next(),
                true
        ));
    }

    @Override
    public Optional<Client> initClientToAdd() {
        List<String> questions = of(
                "Enter client name"
        );

        Optional<List<String>> answers = new FormToast(
                "Lets add client! You can stop everywhere by entering '/stop'", questions, "/stop"
        ).draw().getAnswers();

        if (answers.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Client(
                // TODO FIXME stub
                iterator.next()
        ));
    }

    @Override
    public DataType getCurrentDataType() {
        return getCurrentTabState().getDataType();
    }

    @Override
    public TabType getCurrentTabState() {
        return this.tabStack.peek().getCurrentTabState();
    }

    @Override
    public void drawPrevTab() {
        dataPrinter.clear();
        if (this.tabStack.size() > 1)
            this.tabStack.pop();
        this.tabStack.peek().draw();
    }

    @Override
    public void bookDetails(Supplier<Optional<Book>> bookOptionalSupplier) {
        tabStack.push(new Tab(new String[]{"BOOK DETAILS"},
                0,
                () -> bookOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printBookDetails,
                        this::drawPrevTab
                ),
                LOOK_BOOK
        )).draw();
    }

    @Override
    public void clientDetails(Supplier<Optional<Client>> clientOptionalSupplier) {
        tabStack.push(new Tab(new String[]{"CLIENT DETAILS"},
                0,
                () -> clientOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printClientDetails,
                        this::drawPrevTab
                ),
                LOOK_CLIENT
        )).draw();
    }

    @Override
    public void operationDetails(Supplier<Optional<Operation>> operationOptionalSupplier) {
        tabStack.push(new Tab(new String[]{"OPERATION DETAILS"},
                0,
                () -> operationOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printOperationDetails,
                        this::drawPrevTab
                ),
                LOOK_HISTORY
        )).draw();
    }

    @Override
    public Optional<Book> editBook(Book originalBook) {
        List<String> messages = of(
                format("prev book title: '%s'", originalBook.getTitle()),
                format("prev book subtitle: '%s'", originalBook.getSubtitle()),
                format("prev book author: '%s'", originalBook.getAuthor()),
                format("prev book publish date: '%s'", originalBook.getPublishedDate()),
                format("prev book publisher: '%s'", originalBook.getPublisher())
        );

        new InfoToast("Lets edit book! You can stop by entering '/stop'").draw();
        new InfoToast("If you wanna edit this field, enter data").draw();
        Optional<List<String>> answersOptional = new FormToast(
                "If you wanna skip editing, click enter", messages, "/stop"
        ).draw().getAnswers();

        if (answersOptional.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        List<String> answers = answersOptional.get();

        return Optional.of(new Book(
                originalBook.getId(),
                answers.get(0).equals("")
                        ? originalBook.getTitle()
                        : answers.get(0),
                answers.get(1).equals("")
                        ? originalBook.getSubtitle()
                        : answers.get(1),
                answers.get(2).equals("")
                        ? originalBook.getAuthor()
                        : answers.get(2),
                answers.get(3).equals("")
                        ? originalBook.getPublishedDate()
                        // TODO FIXME stub
                        : LocalDateTime.now(), // answers.get(3),
                answers.get(4).equals("")
                        ? originalBook.getPublisher()
                        : answers.get(4),
                originalBook.isFree()
        ));
    }

    @Override
    public Optional<Client> editClient(final Client originalClient) {
        List<String> messages = of(
                format("prev client name: '%s'", originalClient.getName())
        );

        new InfoToast("Lets edit client! You can stop by entering '/stop'").draw();
        new InfoToast("If you wanna edit this field, enter data").draw();
        Optional<List<String>> answersOptional = new FormToast(
                "If you wanna skip editing, click enter", messages, "/stop"
        ).draw().getAnswers();

        if (answersOptional.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        List<String> answers = answersOptional.get();

        Client client = new Client(
                originalClient.getId(),
                answers.get(0).equals("")
                        ? originalClient.getName()
                        : answers.get(0)
        );
        originalClient.getBooks().forEach(client::addBook);
        return Optional.of(client);
    }

    @Override
    public WindowManager refresh() {
        dataPrinter.clear();
        this.tabStack.peek().draw();
        return this;
    }

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    public class Tab {

        private final String[] tabsNames;
        private final int boldTab;
        private final Runnable body;
        private final TabType currentTabState;

        public Tab(String[] tabsNames,
                   int boldTabIndex,
                   Runnable body,
                   TabType currentTabState) {
            this.tabsNames = tabsNames;
            this.boldTab = -1 < boldTabIndex && boldTabIndex < tabsNames.length
                    ? boldTabIndex
                    : -1;
            this.body = body;
            this.currentTabState = currentTabState;
        }

// TODO FIX
        public void draw() {
//            // beforeAll
            dataPrinter.clear();

            // header
            final int allWordsLength = Arrays
                    .stream(tabsNames)
                    .reduce(0,
                            (acc, str) -> acc + str.length(),
                            Integer::sum);
            final int spaceLength = (WINDOW_WIDTH - allWordsLength) / (tabsNames.length + 1);
            final String space = " ".repeat(spaceLength);

            final StringBuilder message = new StringBuilder(space);
            for (int i = 0; i < tabsNames.length; i++) {
                String tabsName = i == boldTab
                        ? bold(tabsNames[i])
                        : tabsNames[i];
                message.append(tabsName)
                        .append(space);
            }

            System.out.println();
            System.out.println(message);
            System.out.println();

            // body
            requireNonNull(body).run();

            // margin afterAll
            System.out.println();
            System.out.println();
        }

        public TabType getCurrentTabState() {
            return currentTabState;
        }
    }

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    protected abstract class Toast {

        private static final int TOAST_HEIGHT = 11;

        private static final int TOAST_WIDTH = 60;

        private static final int UPPER_START = (WINDOW_HEIGHT - TOAST_HEIGHT) / 2;

        private final String message;

        public Toast(final String message) {
            this.message = message;
        }

        public Toast draw() {
            printWindowBackground();
            printMessage(message);

            appropriateBody();
            refresh();
            return this;
        }

        protected void printWindowBackground() {
            int paddingLeft = 11;

            goTo(UPPER_START, paddingLeft);
            System.out.print("+----------------------------------------------------------+");
            for (int i = 1; i <= TOAST_HEIGHT - 2; i++) {
                goTo(UPPER_START + i, paddingLeft);
                System.out.print("|                                                          |");
            }
            goTo(UPPER_START + TOAST_HEIGHT - 1, paddingLeft);
            System.out.print("+----------------------------------------------------------+");
        }

        protected void printMessage(final String message) {
            // TODO add multiline feature
//            if (message.length() > TOAST_WIDTH - 4) {
//                final String[] messageWords = message.split(" ");
//                final int linesCount = message.length() / (TOAST_WIDTH - 4) + 1;
//
//                final List<String> lines = new ArrayList<>();
//
//                int indexes = messageWords.length / linesCount;
//
//                int from = 0;
//                int to = indexes;
//                for (int i = 0; i < linesCount; i++) {
//                    final String line = String.join(
//                            " ", Arrays.copyOfRange(
//                                    messageWords, from, to)
//                    );
//                    lines.add(line);
//                    from += indexes;
//                    to += indexes;
//                }
//                for (int i = lines.size() - 1; i >= 0; i--) {
//                    String line = lines.get(i);
//                    goTo(UPPER_START + 2 + i, (WINDOW_WIDTH - line.length()) / 2);
//                    System.out.print(line);
//                }
//                return;
//            }
            goTo(UPPER_START + 3, (WINDOW_WIDTH - message.length()) / 2);
            System.out.print(message);
        }

        protected void printQuestion(final String question) {
            // TODO add multiline feature
            goTo(UPPER_START + 5, (WINDOW_WIDTH - question.length()) / 2);
            System.out.print(question);
        }

        protected void waitToContinue() {
            printQuestion("Click enter to continue...");
            goTo(UPPER_START + 6, WINDOW_WIDTH / 2);
            new Scanner(System.in).nextLine();
        }

        protected String readInput() {
            goTo(UPPER_START + 6, WINDOW_WIDTH / 2 - 5);
            return new Scanner(System.in).nextLine();
        }

        protected abstract void appropriateBody();
    }

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

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    public class FormToast extends Toast {

        private final Iterator<String> questions;

        private final String stopCommand;

        private Optional<List<String>> answers;

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
        protected void appropriateBody() {
            waitToContinue();

            final List<String> result = new ArrayList<>();
            while (questions.hasNext()) {
                printWindowBackground();
                printMessage(questions.next());

                String input = readInput();

                if (input.equals(stopCommand)) {
                    answers = Optional.empty();
                    return;
                }
                result.add(input);
            }
            answers = Optional.of(result);
        }

        public Optional<List<String>> getAnswers() {
            if (answers == null) {
                throw new RuntimeException("You must first call the `.draw()` method.");
            }
            return answers;
        }
    }
}
