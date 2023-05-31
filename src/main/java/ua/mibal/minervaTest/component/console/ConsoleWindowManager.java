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
import ua.mibal.minervaTest.component.UserInputReader;
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.State;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.of;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    private final static int WINDOW_HEIGHT = 25;

    private final static int WINDOW_WIDTH = 80;

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    private final Stack<State> tabsStack = new Stack<>();

    private final CacheManager cache = new CacheManager();

    private final TabsContainer tabs;

    public ConsoleWindowManager(final DataPrinter dataPrinter,
                                final UserInputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
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
        tabs.tab2.setBody(() -> dataPrinter.printListOfClients(library.getClients()))
            .setDataCaching(() -> cache.cache(library))
            .draw();
    }

    @Override
    public void tab3(final Library library) {
        tabs.tab3.setBody(() -> dataPrinter.printListOfOperations(
                library.getOperations(),
                library.getClients())
            ).setDataCaching(() -> cache.cache(library))
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
            .setTabsNames(new String[] { header }, 0)
            .setBody(() -> dataPrinter.printListOfBooks(books))
            .setDataCaching(() -> cache.cache(books, args))
            .draw();
    }

    @Override
    public void searchClientTab(final List<Client> clients, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        tabs.searchClientTab
            .setTabsNames(new String[] { header }, 0)
            .setBody(() -> dataPrinter.printListOfClients(clients))
            .setDataCaching(() -> cache.cache(clients, args))
            .draw();
    }

    @Override
    public void searchOperationTab(final List<Operation> operations, final List<Client> clients, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        tabs.searchOperationTab
            .setTabsNames(new String[] { header }, 0)
            .setBody(() -> dataPrinter.printListOfOperations(operations, clients))
            .setDataCaching(() -> cache.cache(operations, args))
            .draw();
    }

    @Override
    public void bookDetails(final Book book) {
        List<String> keyVal = of(
            "ID", book.getId(),
            "Title", book.getTitle(),
            "Subtitle", book.getSubtitle(),
            "Publisher", book.getPublisher(),
            "Publish date", book.getPublishedDate(),
            "Free", book.isFree() ? "YES" : "NO"
        );
        tabs.bookDetails
            .setBody(() -> {
                System.out.println("+--------------+---------------------------------------------------------------+");
                for (int i = 0; i < keyVal.size(); i += 2) {
                    String key = keyVal.get(i);
                    String val = keyVal.get(i + 1);
                    System.out.format("| %-12s | %-61s |%n", key, val);
                    System.out.println(
                        "+--------------+---------------------------------------------------------------+");
                }
            }).setDataCaching(() -> cache.cache(book))
            .draw();
    }

    @Override
    public void clientDetails(final Client client, final List<Book> books) {
        List<String> keyVal = of(
            "ID", client.getId(),
            "Name", client.getName()
        );
        tabs.clientDetails
            .setBody(() -> {
                System.out.println("+------+-----------------------------------------------------------------------+");
                for (int i = 0; i < keyVal.size(); i += 2) {
                    String key = keyVal.get(i);
                    String val = keyVal.get(i + 1);
                    System.out.format("| %-4s | %-69s |%n", key, val);
                    System.out.println(
                        "+------+-----------------------------------------------------------------------+");
                }
                System.out.println("""
                                                      
                                                Books that client holds
                    """);
                dataPrinter.printListOfBooks(books);
            }).setDataCaching(() -> cache.cache(client, books))
            .draw();
    }

    @Override
    public void operationDetails(final Operation operation, final Client client, final List<Book> books) {
        List<String> keyVal = of(
            "ID", operation.getId(),
            "Date", operation.getDate(),
            "Client", client.getName(),
            "Type", operation.getOperationType()
        );
        tabs.operationDetails
            .setBody(() -> {
                System.out.println("+--------+---------------------------------------------------------------------+");
                for (int i = 0; i < keyVal.size(); i += 2) {
                    String key = keyVal.get(i);
                    String val = keyVal.get(i + 1);
                    System.out.format("| %-6s | %-67s |%n", key, val);
                    System.out.println(
                        "+--------+---------------------------------------------------------------------+");
                }
            }).setDataCaching(() -> cache.cache(operation, client, books))
            .draw();
    }

    // <<< Read >>>

    @Override
    public String[] readCommandLine() {
        goTo(24, 0);
        dataPrinter.printInfoMessage("> ");
        String input = inputReader.getUserInput();
        return input.split(" ");
    }

    // <<< Toasts >>>

    @Override
    public void showToast(final String message) {
        printBackgroundAndMessage(message);

        // print click to continue
        String enter = "Click enter to continue...";
        goTo(14, (WINDOW_WIDTH - enter.length()) / 2);
        dataPrinter.printInfoMessage(enter);
        goTo(15, WINDOW_WIDTH / 2);
        inputReader.getUserInput();

        //refresh last screen
        drawBackground();
    }

    private void printBackgroundAndMessage(final String message) {
        int padding = 10;

        // print background of window
        goTo(8, padding);
        dataPrinter.printInfoMessage("+----------------------------------------------------------+");
        for (int i = 0; i < 10; i++) {
            goTo(i + 9, padding);
            dataPrinter.printInfoMessage("|                                                          |");
        }
        goTo(19, padding);
        dataPrinter.printInfoMessage("+----------------------------------------------------------+");

        // print message
        goTo(12, (WINDOW_WIDTH - message.length()) / 2);
        dataPrinter.printInfoMessage(message);
    }

    private Optional<List<String>> form(final List<String> messages, final String stopCommand) {
        List<String> answers = new ArrayList<>();
        for (final String message : messages) {

            // print question
            printBackgroundAndMessage(message);

            // Receive input
            goTo(14, WINDOW_WIDTH / 2 - 5);
            final String input = inputReader.getUserInput();
            if (Objects.equals(input, stopCommand)) {
                return Optional.empty();
            }
            answers.add(input);
        }
        return Optional.of(answers);
    }

    @Override
    public boolean showDialogueToast(final String question, final String answer1, final String answer2) {
        printBackgroundAndMessage(question);

        // dialogue input
        final String enter = format("1 - %s, 2 - %s", answer1, answer2);
        goTo(14, (WINDOW_WIDTH - enter.length()) / 2);
        dataPrinter.printInfoMessage(enter);
        goTo(15, WINDOW_WIDTH / 2);
        String input = inputReader.getUserInput();

        // refresh last screen and return
        if (Objects.equals(input, "1")) {
            drawBackground();
            return true;
        }
        if (Objects.equals(input, "2")) {
            drawBackground();
            return false;
        }
        return showDialogueToast(question, answer1, answer2);
    }

    @Override
    public Optional<Book> initBookToAdd(final Library library) {
        List<String> messages = of(
            "Enter book title",
            "Enter subtitle",
            "Enter author",
            "Enter publish date (in format '2018-12-04')",
            "Enter book publisher"
        );

        showToast("Lets create book! You can stop everywhere by entering '/stop'");
        Optional<List<String>> answers = form(messages, "/stop");

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
        List<String> messages = of(
            "Enter client name"
        );

        showToast("Lets add client! You can stop everywhere by entering '/stop'");
        Optional<List<String>> answers = form(messages, "/stop");

        if (answers.isEmpty()) {
            drawBackground();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Client(
            iterator.next(),
            List.of()
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

        showToast("Lets edit book! You can stop everywhere by entering '/stop'");
        showToast("If you want to edit some fields, enter new data");
        showToast("If you want to skip field, just click enter");
        Optional<List<String>> answersOptional = form(messages, "/stop");

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

        showToast("Lets edit client! You can stop everywhere by entering '/stop'");
        showToast("If you want to edit some fields, enter new data");
        showToast("If you want to skip field, just click enter");
        Optional<List<String>> answersOptional = form(messages, "/stop");

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
                cache.library.getClients(),
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
        dataPrinter.printInfoMessage(format("%c[%d;%df", escCode, row, column));
    }
}
