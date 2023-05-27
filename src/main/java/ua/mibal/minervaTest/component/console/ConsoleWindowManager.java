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
import ua.mibal.minervaTest.model.window.State;
import static java.lang.String.format;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.BOLD;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.RESET;
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

    private CachedSearchArgs<Book> cachedSearchBookArgs;

    private CachedSearchArgs<Client> cachedSearchClientArgs;

    private CachedSearchArgs<Operation> cachedSearchOperationArgs;

    private Library cachedLibrary;

    public ConsoleWindowManager(final DataPrinter dataPrinter,
                                final UserInputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
    }

    @Override
    public void tab1(final Library library) {
        beforeAll();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                %sBOOKS%s               CLIENTS               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfBooks(library.getBooks());
        cachedLibrary = library;
        afterAll();
    }

    @Override
    public void tab2(final Library library) {
        beforeAll();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               %sCLIENTS%s               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfClients(library.getClients());
        cachedLibrary = library;
        afterAll();
    }

    @Override
    public void tab3(final Library library) {
        beforeAll();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               CLIENTS               %sHISTORY%s               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfOperations(library.getOperations(), library.getClients());
        cachedLibrary = library;
        afterAll();
    }

    @Override
    public void help() {
        beforeAll();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                                      %sHELP%s                                      ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
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
    public void showToast(final String message, final State currentTab) {
        printBackgroundAndMessage(message);

        // print click to continue
        String enter = "Click enter to continue...";
        goTo(14, (WINDOW_WIDTH - enter.length()) / 2);
        dataPrinter.printInfoMessage(enter);
        goTo(15, WINDOW_WIDTH / 2);
        inputReader.getUserInput();

        //refresh last screen
        printPrevState(currentTab);
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
    public boolean showDialogueToast(final String question, final String answer1, final String answer2,
                                     final State currentTab) {
        printBackgroundAndMessage(question);

        // dialogue input
        String enter = "1 - YES, 2 - NO";
        goTo(14, (WINDOW_WIDTH - enter.length()) / 2);
        dataPrinter.printInfoMessage(enter);
        goTo(15, WINDOW_WIDTH / 2);
        String input = inputReader.getUserInput();
        if (Objects.equals(input, "1")) {
            //refresh last screen
            printPrevState(currentTab);
            return true;
        }
        if (Objects.equals(input, "2")) {
            //refresh last screen
            printPrevState(currentTab);
            return false;
        }
        return showDialogueToast(question, answer1, answer2, currentTab);
    }

    @Override
    public void searchBookTab(final List<Book> books, final String[] args) {
        // TODO
    }

    @Override
    public void searchClientTab(final List<Client> clients, final String[] args) {
        // TODO
    }

    @Override
    public void searchOperationTab(final List<Operation> operations, final String[] args) {
        // TODO
    }

    @Override
    public Optional<Book> initBookToAdd(final Library library) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<Client> initClientToAdd(final Library library) {
        // TODO
        return Optional.empty();
    }

    private void beforeAll() {
        dataPrinter.clear();
    }

    private void afterAll() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage("");
    }

    private void printPrevState(final State state) {
        switch (state) {
            case TAB_1 -> tab1(cachedLibrary);
            case TAB_2 -> tab2(cachedLibrary);
            case TAB_3 -> tab3(cachedLibrary);
            case SEARCH_BOOK -> searchBookTab(
                cachedSearchBookArgs.data,
                cachedSearchClientArgs.args);
            case SEARCH_CLIENT -> searchClientTab(
                cachedSearchClientArgs.data,
                cachedSearchClientArgs.args);
            case SEARCH_HISTORY -> searchOperationTab(
                cachedSearchOperationArgs.data,
                cachedSearchOperationArgs.args);
        }
    }

    private record CachedSearchArgs<T>(List<T> data, String[] args) {

    }
}
