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

package ua.mibal.minervaTest;


import ua.mibal.minervaTest.component.DataOperator;
import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.component.UserInputReader;
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.component.request.RequestProcessor;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Request;
import ua.mibal.minervaTest.model.command.DataType;
import ua.mibal.minervaTest.model.window.State;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.CommandType.ADD;
import static ua.mibal.minervaTest.model.command.CommandType.DEL;
import static ua.mibal.minervaTest.model.command.CommandType.EXIT;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.WINDOW_1;
import static ua.mibal.minervaTest.model.window.State.WINDOW_2;
import static ua.mibal.minervaTest.model.window.State.WINDOW_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Application {

    private final DataOperator dataOperator;

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    private final WindowManager windowManager;

    private final RequestProcessor requestProcessor;

    private State state = WINDOW_1;

    private DataType currentDataType = state.getDataType();

    public Application(final DataOperator dataOperator,
                       final DataPrinter dataPrinter,
                       final UserInputReader inputReader,
                       final WindowManager consoleWindowManager,
                       final RequestProcessor requestProcessor) {
        this.dataOperator = dataOperator;
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
        this.windowManager = consoleWindowManager;
        this.requestProcessor = requestProcessor;
    }

    public void start() {
        Library library = dataOperator.getLibrary();

        windowManager.setLibrary(library);
        windowManager.setState(state);

        while (true) {
            dataPrinter.printInfoMessage("> ");
            final String input = inputReader.getUserInput();
            switch (input) {
                case "1" -> state = WINDOW_1;
                case "2" -> state = WINDOW_2;
                case "3" -> state = WINDOW_3;
                case "search", "s" -> {
                    // TODO
                }
                case "add" -> {
                    if (currentDataType == HISTORY) {
                        dataPrinter.printErrorMessage("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(ADD, currentDataType));
                    }
                }
                case "delete", "del" -> {
                    if (currentDataType == HISTORY) {
                        dataPrinter.printErrorMessage("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(DEL, currentDataType));
                    }
                }
                case "exit" -> {
                    requestProcessor.process(library, new Request(EXIT, null));
                }
                default -> dataPrinter.printErrorMessage(format("Unrecognizable arg '%s'", input));
            }
            windowManager.setState(state);
            currentDataType = state.getDataType();
        }
    }
}
