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
import ua.mibal.minervaTest.model.Library;
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

    private Library library = new Library(List.of(), List.of(), List.of());

    private State state;

    private int linesCount = 0;

    public ConsoleWindowManager(final DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    private void redraw() {
        linesCount = 0;
        if (library == null) {
            throw new IllegalArgumentException("Variable library not set");
        }
        switch (state) {
            case WINDOW_1 -> drawWindow1();
            case WINDOW_2 -> drawWindow2();
            case WINDOW_3 -> drawWindow3();
            case HELP_WINDOW -> drawHelpWindow();
            default -> throw new IllegalArgumentException(format("Illegal state '%s'", state));
        }
        for (int i = 0; i < WINDOW_HEIGHT - linesCount - 1; i++) {
            dataPrinter.printlnInfoMessage("");
        }
    }

    private void drawWindow1() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                %sBOOKS%s               CLIENTS               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfBooks(library.getBooks());
        final int elCount = library.getBooks().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
    }

    private void drawWindow2() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               %sCLIENTS%s               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfClients(library.getClients());
        final int elCount = library.getClients().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
    }

    private void drawWindow3() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               CLIENTS               %sHISTORY%s               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfOperations(library.getOperations(), library.getClients());
        final int elCount = library.getOperations().size();
        linesCount = 3 + 3 + elCount + (elCount == 0 ? 0 : 1);
    }

    private void drawHelpWindow() {
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
    }

    @Override
    public WindowManager setState(final State state) {
        this.state = state;
        redraw();
        return this;
    }

    @Override
    public void setLibrary(final Library library) {
        this.library = library;
    }
}
