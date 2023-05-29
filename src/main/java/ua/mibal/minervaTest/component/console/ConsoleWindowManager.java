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
import static java.util.List.of;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.BOLD;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.RESET;
import static ua.mibal.minervaTest.model.window.State.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.State.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.State.LOOK_HISTORY;
import static ua.mibal.minervaTest.model.window.State.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.State.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.State.SEARCH_HISTORY;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    private final static int WINDOW_HEIGHT = 25;

    private final static int WINDOW_WIDTH = 80;

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    private State currentTab = State.TAB_1;

    private State prevTab;

    private CachedSearchArgs<Book> cachedSearchBookArgs;

    private CachedSearchArgs<Client> cachedSearchClientArgs;

    private CachedSearchArgs<Operation> cachedSearchOperationArgs;

    private CachedClientDetailsArgs cachedClientDetailsArgs;

    private CachedOperationDetailsArgs cachedOperationDetailsArgs;

    private Library cachedLibrary;

    private Book cachedBook;

    public ConsoleWindowManager(final DataPrinter dataPrinter,
                                final UserInputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
    }

    @Override
    public void tab1(final Library library) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                %sBOOKS%s               CLIENTS               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfBooks(library.getBooks());
        cachedLibrary = library;
        prevTab = currentTab = State.TAB_1;

        afterAll();
    }

    @Override
    public void tab2(final Library library) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               %sCLIENTS%s               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfClients(library.getClients());
        cachedLibrary = library;
        prevTab = currentTab = State.TAB_2;

        afterAll();
    }

    @Override
    public void tab3(final Library library) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               CLIENTS               %sHISTORY%s               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfOperations(library.getOperations(), library.getClients());
        cachedLibrary = library;
        prevTab = currentTab = State.TAB_3;

        afterAll();
    }

    @Override
    public void help() {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                                      %sHELP%s                                      ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");

        // instructions
        dataPrinter.printlnInfoMessage("""
                                              
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
                 
             """);
        prevTab = currentTab;
        currentTab = State.HELP_TAB;

        afterAll();
    }

    @Override
    public String[] readCommandLine() {
        goTo(24, 0);
        dataPrinter.printInfoMessage("> ");
        String input = inputReader.getUserInput();
        return input.split(" ");
    }

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

    private void goTo(final int row, final int column) {
        char escCode = 0x1B;
        dataPrinter.printInfoMessage(String.format("%c[%d;%df", escCode, row, column));
    }

    @Override
    public boolean showDialogueToast(final String question, final String answer1, final String answer2) {
        printBackgroundAndMessage(question);

        // dialogue input
        String enter = "1 - YES, 2 - NO";
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
    public void searchBookTab(final List<Book> books, final String[] args) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        final String message = "SEARCH IN BOOKS BY '" + String.join(" ", args) + "'";
        goTo(2, (WINDOW_WIDTH - message.length()) / 2);
        dataPrinter.printlnInfoMessage(BOLD + message + RESET);
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfBooks(books);
        this.cachedSearchBookArgs = new CachedSearchArgs<>(books, args);
        prevTab = currentTab;
        currentTab = SEARCH_BOOK;

        afterAll();
    }

    @Override
    public void searchClientTab(final List<Client> clients, final String[] args) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        final String message = "SEARCH IN CLIENTS BY '" + String.join(" ", args) + "'";
        goTo(2, (WINDOW_WIDTH - message.length()) / 2);
        dataPrinter.printlnInfoMessage(BOLD + message + RESET);
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfClients(clients);
        this.cachedSearchClientArgs = new CachedSearchArgs<>(clients, args);
        prevTab = currentTab;
        currentTab = SEARCH_CLIENT;

        afterAll();
    }

    @Override
    public void searchOperationTab(final List<Operation> operations, final List<Client> clients, final String[] args) {
        beforeAll();

        // header
        dataPrinter.printlnInfoMessage("");
        final String message = "SEARCH IN OPERATIONS BY '" + String.join(" ", args) + "'";
        goTo(2, (WINDOW_WIDTH - message.length()) / 2);
        dataPrinter.printlnInfoMessage(BOLD + message + RESET);
        dataPrinter.printlnInfoMessage("");

        // table
        dataPrinter.printListOfOperations(operations, clients);
        this.cachedSearchOperationArgs = new CachedSearchArgs<>(operations, args);
        prevTab = currentTab;
        currentTab = SEARCH_HISTORY;

        afterAll();
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
    public DataType getCurrentDataType() {
        return currentTab.getDataType();
    }

    @Override
    public void bookDetails(final Book book) {
        beforeAll();

        System.out.println("""
                                              
                                              BOOK DETAILS
            """);

        List<String> keyVal = of(
            "ID", book.getId(),
            "Title", book.getTitle(),
            "Subtitle", book.getSubtitle(),
            "Publisher", book.getPublisher(),
            "Publish date", book.getPublishedDate(),
            "Free", book.isFree() ? "YES" : "NO"
        );

        System.out.println("+--------------+---------------------------------------------------------------+");
        for (int i = 0; i < keyVal.size(); i += 2) {
            String key = keyVal.get(i);
            String val = keyVal.get(i + 1);
            System.out.format("| %-12s | %-61s |%n", key, val);
            System.out.println("+--------------+---------------------------------------------------------------+");
        }
        cachedBook = book;
        prevTab = currentTab;
        currentTab = LOOK_BOOK;

        afterAll();
    }

    @Override
    public void clientDetails(final Client client, final List<Book> books) {
        beforeAll();

        System.out.println("""
                                              
                                             CLIENT DETAILS
            """);

        List<String> keyVal = of(
            "ID", client.getId(),
            "Name", client.getName()
        );

        System.out.println("+------+-----------------------------------------------------------------------+");
        for (int i = 0; i < keyVal.size(); i += 2) {
            String key = keyVal.get(i);
            String val = keyVal.get(i + 1);
            System.out.format("| %-4s | %-69s |%n", key, val);
            System.out.println("+------+-----------------------------------------------------------------------+");
        }

        System.out.println("""
                                              
                                        Books that client holds
            """);
        dataPrinter.printListOfBooks(books);

        cachedClientDetailsArgs = new CachedClientDetailsArgs(client, books);
        prevTab = currentTab;
        currentTab = LOOK_CLIENT;

        afterAll();
    }

    @Override
    public void operationDetails(final Operation operation, final Client client, final List<Book> books) {
        beforeAll();

        System.out.println("""
                                              
                                            OPERATION DETAILS
            """);

        List<String> keyVal = of(
            "Date", operation.getDate(),
            "Client", client.getName(),
            "Type", operation.getOperationType()
        );

        System.out.println("+--------+---------------------------------------------------------------------+");
        for (int i = 0; i < keyVal.size(); i += 2) {
            String key = keyVal.get(i);
            String val = keyVal.get(i + 1);
            System.out.format("| %-6s | %-67s |%n", key, val);
            System.out.println("+--------+---------------------------------------------------------------------+");
        }

        System.out.println("""
                                              
                                                  Books
            """);
        dataPrinter.printListOfBooks(books);

        cachedOperationDetailsArgs = new CachedOperationDetailsArgs(operation, client, books);
        prevTab = currentTab;
        currentTab = LOOK_HISTORY;

        afterAll();
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

    private void beforeAll() {
        dataPrinter.clear();
    }

    private void afterAll() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage("");
    }

    @Override
    public void drawPrevTab() {
        drawTab(prevTab);
    }

    public void drawBackground() {
        drawTab(currentTab);
    }

    private void drawTab(final State tab) {
        switch (tab) {
            case TAB_1 -> tab1(cachedLibrary);
            case TAB_2 -> tab2(cachedLibrary);
            case TAB_3 -> tab3(cachedLibrary);
            case HELP_TAB -> help();
            case SEARCH_BOOK -> searchBookTab(
                cachedSearchBookArgs.data,
                cachedSearchClientArgs.args);
            case SEARCH_CLIENT -> searchClientTab(
                cachedSearchClientArgs.data,
                cachedSearchClientArgs.args);
            case SEARCH_HISTORY -> searchOperationTab(
                cachedSearchOperationArgs.data,
                cachedLibrary.getClients(),
                cachedSearchOperationArgs.args);
            case LOOK_BOOK -> bookDetails(cachedBook);
            case LOOK_CLIENT -> clientDetails(
                cachedClientDetailsArgs.client,
                cachedClientDetailsArgs.books);
            case LOOK_HISTORY -> operationDetails(
                cachedOperationDetailsArgs.operation,
                cachedOperationDetailsArgs.client,
                cachedOperationDetailsArgs.books);
        }
    }

    private record CachedSearchArgs<T>(List<T> data, String[] args) {

    }

    private record CachedClientDetailsArgs(Client client, List<Book> books) {

    }

    private record CachedOperationDetailsArgs(Operation operation, Client client, List<Book> books) {

    }
}
