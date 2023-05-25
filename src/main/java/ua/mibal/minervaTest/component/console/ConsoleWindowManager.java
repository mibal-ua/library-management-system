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

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    private final static int WINDOW_HEIGHT = 25;

    private final static int WINDOW_WIDTH = 80;

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    private State state;

    private int linesCount = 0;

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
        final int elCount = library.getBooks().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
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
        final int elCount = library.getClients().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
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
        final int elCount = library.getOperations().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
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

        linesCount = 3 + 36;
        afterAll();
    }

    @Override
    public String[] readCommandLine() {
        dataPrinter.printInfoMessage("> ");
        String input = inputReader.getUserInput();
        return input.split(" ");
    }

    @Override
    public void showToast(final String message) {
        // TODO
    }

    @Override
    public boolean showDialogueToast(final String question, final String answer1, final String answer2) {
        // TODO
        return false;
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

    private void beforeAll() {
        linesCount = 0;
        dataPrinter.clear();
    }

    private void afterAll() {
        for (int i = 0; i < WINDOW_HEIGHT - linesCount - 1; i++) {
            dataPrinter.printlnInfoMessage("");
        }
        linesCount = 0;
    }
}
