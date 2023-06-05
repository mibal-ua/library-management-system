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

import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.of;
import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.bold;
import static ua.mibal.minervaTest.model.window.State.HELP_TAB;
import static ua.mibal.minervaTest.model.window.State.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.State.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.State.LOOK_HISTORY;
import static ua.mibal.minervaTest.model.window.State.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.State.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.State.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    public final static int WINDOW_WIDTH = 80;
    private final static int WINDOW_HEIGHT = 25;
    private final DataPrinter dataPrinter;

    private final CacheManager cache = new CacheManager();

    public ConsoleWindowManager(final DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    // <<< Tabs >>>

    @Override
    public void tab1(final Library library) {
        new Tab(new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                0,
                () -> dataPrinter.printListOfBooks(library.getBooks()),
                TAB_1,
                true
        ).draw();
    }

    @Override
    public void tab2(final Library library) {
        new Tab(new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                1,
                () -> dataPrinter.printListOfClients(library.getClients()),
                TAB_2,
                true
        ).draw();
    }

    @Override
    public void tab3(final Library library) {
        new Tab(new String[]{"BOOKS", "CLIENTS", "HISTORY"},
                2,
                () -> dataPrinter.printListOfOperations(
                        library.getOperations(),
                        library
                ),
                TAB_3,
                true
        ).draw();
    }

    @Override
    public void help() {
        new Tab(new String[]{"HELP"},
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
        ).draw();
    }

    @Override
    public void searchBookTab(final List<Book> books, final String[] args) {
        final String header = format("SEARCH IN BOOKS BY '%s'", join(" ", args));
        new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfBooks(books),
                SEARCH_BOOK
        ).draw();
    }

    @Override
    public void searchClientTab(final List<Client> clients, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfClients(clients),
                SEARCH_CLIENT
        ).draw();
    }

    @Override
    public void searchOperationTab(final List<Operation> operations, final Library library, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        new Tab(new String[]{header},
                0,
                () -> dataPrinter.printListOfOperations(operations, library),
                SEARCH_HISTORY
        ).draw();
    }

    @Override
    public void bookDetails(final Book book) {
        new Tab(new String[]{"BOOK DETAILS"},
                0,
                () -> dataPrinter.printBookDetails(book),
                LOOK_BOOK
        ).draw();
        cache.push(book);
    }

    @Override
    public void clientDetails(final Client client, final List<Book> books) {
        new Tab(new String[]{"CLIENT DETAILS"},
                0,
                () -> dataPrinter.printClientDetails(client, books),
                LOOK_CLIENT
        ).draw();
        cache.push(client);
    }

    @Override
    public void operationDetails(final Operation operation, final Client client, final List<Book> books) {
        new Tab(new String[]{"OPERATION DETAILS"},
                0,
                () -> dataPrinter.printOperationDetails(operation, client, books),
                LOOK_HISTORY
        ).draw();
    }

    // <<< Read >>>

    @Override
    public String[] readCommandLine() {
        goTo(24, 0);
        System.out.print("> ");
        String input = new Scanner(System.in).nextLine();
        return input.split(" ");
    }

    // <<< Toasts >>>

    @Override
    public void showToast(final String message) {
        new InfoToast(message).draw();
    }

    @Override
    public boolean showDialogueToast(final String question, final String answer1, final String answer2) {
        return new DialogueToast(question, answer1, answer2)
                .draw()
                .getAnswer();
    }

    @Override
    public Optional<Book> initBookToAdd(final Library library) {
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
            drawBackground();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Book(
                iterator.next(),
                iterator.next(),
                iterator.next(),
                iterator.next(),
                iterator.next(),
                true
        ));
    }

    @Override
    public Optional<Client> initClientToAdd(final Library library) {
        List<String> questions = of(
                "Enter client name"
        );

        Optional<List<String>> answers = new FormToast(
                "Lets add client! You can stop everywhere by entering '/stop'", questions, "/stop"
        ).draw().getAnswers();

        if (answers.isEmpty()) {
            drawBackground();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Client(
                iterator.next()
        ));
    }

    @Override
    public Optional<Book> editBook(final Book originalBook) {
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
            drawBackground();
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
                        : answers.get(3),
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
            drawBackground();
            return Optional.empty();
        }

        List<String> answers = answersOptional.get();

        return Optional.of(new Client(
                originalClient.getId(),
                answers.get(0).equals("")
                        ? originalClient.getName()
                        : answers.get(0),
                originalClient.getBooksIds()
        ));
    }

    // <<< Cached Tabs drawing >>>

    @Override
    public void drawPrevTab() {
        if (cache.stack.size() > 1) {
            cache.stack.pop();
        }
        cache.stack.peek().draw();
    }

    public void drawBackground() {
        cache.stack.peek().draw();
    }

    // <<< Cached data Getters >>>

    @Override
    public DataType getCurrentDataType() {
        return cache.stack.peek().getDataType();
    }

    @Override
    public State getCurrentTabState() {
        return cache.stack.peek().getCurrentTabState();
    }

    @Override
    public String getCachedBookId() {
        return cache.getBook().getId();
    }

    @Override
    public String getCachedClientId() {
        return cache.getClient().getId();
    }

    // <<< Console esc-sequences >>>

    private void goTo(final int row, final int column) {
        char escCode = 0x1B;
        System.out.printf("%c[%d;%df", escCode, row, column);
    }

    // <<< Tabs >>>

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    public class Tab {

        private final String[] tabsNames;

        private final int boldTab;

        private final Runnable body;

        private final State currentTabState;

        public Tab(final String[] tabsNames,
                   final int boldTab,
                   final Runnable body,
                   final State currentTabState) {
            this(tabsNames, boldTab, body, currentTabState, false);
        }

        public Tab(final String[] tabsNames,
                   final int boldTab,
                   final Runnable body,
                   final State currentTabState,
                   final boolean isRootTab) {
            this.tabsNames = tabsNames;
            if (-1 < boldTab && boldTab < tabsNames.length) {
                this.boldTab = boldTab;
            } else {
                this.boldTab = -1;
            }
            this.body = body;
            this.currentTabState = currentTabState;
            if (isRootTab) {
                cache.stack.clear();
            }
            cache.push(this);
        }


        public void draw() {
            // beforeAll
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
            requireNonNull(body);
            body.run();

            // margin afterAll
            System.out.println();
            System.out.println();
        }

        public DataType getDataType() {
            return currentTabState.getDataType();
        }

        public State getCurrentTabState() {
            return currentTabState;
        }
    }

    // <<< Toasts >>>

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
            drawBackground();
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
            ;
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
