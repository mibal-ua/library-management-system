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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.of;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    private final static int WINDOW_HEIGHT = 25;

    public final static int WINDOW_WIDTH = 80;

    private final DataPrinter dataPrinter;

    private final Stack<State> tabsStack = new Stack<>();

    private final CacheManager cache = new CacheManager();

    private final TabsContainer tabs;

    public ConsoleWindowManager(final DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
        tabs = new TabsContainer(
                dataPrinter::clear,
                this::cacheTab,
                WINDOW_WIDTH
        );
    }

    // <<< Tabs >>>

    @Override
    public void tab1(final Library library) {
        tabs.tab1.setBody(() -> dataPrinter.printListOfBooks(library.getBooks()))
                .setDataCaching(() -> cache.cache(library))
                .draw();
    }

    @Override
    public void tab2(final Library library) {
        tabs.tab2
                .setBody(() -> dataPrinter.printListOfClients(library.getClients()))
                .setDataCaching(() -> cache.cache(library))
                .draw();
    }

    @Override
    public void tab3(final Library library) {
        tabs.tab3
                .setBody(() -> dataPrinter.printListOfOperations(
                        library.getOperations(),
                        library
                )).setDataCaching(() -> cache.cache(library))
                .draw();
    }

    @Override
    public void help() {
        tabs.help.draw();
    }

    @Override
    public void searchBookTab(final List<Book> books, final String[] args) {
        final String header = format("SEARCH IN BOOKS BY '%s'", join(" ", args));
        tabs.searchBookTab
                .setTabsNames(new String[]{header}, 0)
                .setBody(() -> dataPrinter.printListOfBooks(books))
                .setDataCaching(() -> cache.cache(books, args))
                .draw();
    }

    @Override
    public void searchClientTab(final List<Client> clients, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        tabs.searchClientTab
                .setTabsNames(new String[]{header}, 0)
                .setBody(() -> dataPrinter.printListOfClients(clients))
                .setDataCaching(() -> cache.cache(clients, args))
                .draw();
    }

    @Override
    public void searchOperationTab(final List<Operation> operations, final Library library, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        tabs.searchOperationTab
                .setTabsNames(new String[]{header}, 0)
                .setBody(() -> dataPrinter.printListOfOperations(operations, library))
                .setDataCaching(() -> cache.cache(operations, args))
                .draw();
    }

    @Override
    public void bookDetails(final Book book) {
        tabs.bookDetails
                .setBody(() -> dataPrinter.printBookDetails(book))
                .setDataCaching(() -> cache.cache(book))
                .draw();
    }

    @Override
    public void clientDetails(final Client client, final List<Book> books) {
        tabs.clientDetails
                .setBody(() -> dataPrinter.printClientDetails(client, books))
                .setDataCaching(() -> cache.cache(client, books))
                .draw();
    }

    @Override
    public void operationDetails(final Operation operation, final Client client, final List<Book> books) {
        tabs.operationDetails
                .setBody(() -> dataPrinter.printOperationDetails(operation, client, books))
                .setDataCaching(() -> cache.cache(operation, client, books))
                .draw();
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
        if (tabsStack.size() > 1) {
            tabsStack.pop();
        }
        drawTab(tabsStack.peek());
    }

    public void drawBackground() {
        drawTab(tabsStack.peek());
    }

    private void drawTab(final State tab) {
        switch (tab) {
            case TAB_1 -> tab1(cache.library);
            case TAB_2 -> tab2(cache.library);
            case TAB_3 -> tab3(cache.library);
            case HELP_TAB -> help();
            case SEARCH_BOOK -> searchBookTab(
                    cache.searchBook.data(),
                    cache.searchBook.args()
            );
            case SEARCH_CLIENT -> searchClientTab(
                    cache.searchClient.data(),
                    cache.searchClient.args()
            );
            case SEARCH_HISTORY -> searchOperationTab(
                    cache.searchOperation.data(),
                    cache.library,
                    cache.searchOperation.args()
            );
            case LOOK_BOOK -> bookDetails(cache.bookDetails);
            case LOOK_CLIENT -> clientDetails(
                    cache.clientDetails.client(),
                    cache.clientDetails.books()
            );
            case LOOK_HISTORY -> operationDetails(
                    cache.operationDetails.operation(),
                    cache.operationDetails.client(),
                    cache.operationDetails.books()
            );
        }
    }

    private void cacheTab(final State tab) {
        if (tab == TAB_1 || tab == TAB_2 || tab == TAB_3) {
            tabsStack.clear();
            tabsStack.push(tab);
        } else if (tabsStack.peek() != tab) {
            tabsStack.push(tab);
        }
    }

    // <<< Cached data Getters >>>

    @Override
    public DataType getCurrentDataType() {
        return tabsStack.peek().getDataType();
    }

    @Override
    public State getCurrentTabState() {
        return tabsStack.peek();
    }

    @Override
    public String getCachedBookId() {
        return cache.bookDetails.getId();
    }

    @Override
    public String getCachedClientId() {
        return cache.clientDetails.client().getId();
    }

    // <<< Console esc-sequences >>>

    private void goTo(final int row, final int column) {
        char escCode = 0x1B;
        System.out.printf("%c[%d;%df", escCode, row, column);
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
            drawPrevTab();
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
                throw new RuntimeException("you must first call the `.draw()` method.");
            }
            return answers;
        }
    }
}
