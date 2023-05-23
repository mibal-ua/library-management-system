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

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleWindowManager implements WindowManager {

    private final static int WINDOW_HEIGHT = 25;

    private final static int WINDOW_WIDTH = 80;

    private final DataPrinter dataPrinter;

    private Library library;

    private State state;

    public ConsoleWindowManager(final DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    private void redraw() {
        dataPrinter.clear();
        if (library == null) {
            throw new IllegalArgumentException("Variable library not set");
        }
        switch (state) {
            case WINDOW_1 -> drawWindow1();
            case WINDOW_2 -> drawWindow2();
            case WINDOW_3 -> drawWindow3();
            default -> throw new IllegalArgumentException(format("Illegal state '%s'", state));
        }
    }

    private void drawWindow1() {
        dataPrinter.clear();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                %sBOOKS%s               CLIENTS               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfBooks(library.getBooks());
    }

    private void drawWindow2() {
        dataPrinter.clear();
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               %sCLIENTS%s               HISTORY               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfClients(library.getClients());
    }

    private void drawWindow3() {
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printlnInfoMessage(format(
            "                BOOKS               CLIENTS               %sHISTORY%s               ",
            BOLD, RESET));
        dataPrinter.printlnInfoMessage("");
        dataPrinter.printListOfOperations(library.getOperations(), library.getClients());
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
