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
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.component.request.RequestProcessor;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Request;
import ua.mibal.minervaTest.model.command.DataType;
import static ua.mibal.minervaTest.model.command.CommandType.ADD;
import static ua.mibal.minervaTest.model.command.CommandType.DEL;
import static ua.mibal.minervaTest.model.command.CommandType.EXIT;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.WINDOW_1;
import java.util.Arrays;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Application {

    private final DataOperator dataOperator;

    private final WindowManager windowManager;

    private final RequestProcessor requestProcessor;

    // FIXME delete var
    private DataType currentDataType = WINDOW_1.getDataType();

    public Application(final DataOperator dataOperator,
                       final WindowManager windowManager,
                       final RequestProcessor requestProcessor) {
        this.dataOperator = dataOperator;
        this.windowManager = windowManager;
        this.requestProcessor = requestProcessor;
    }

    public void start() {
        Library library = dataOperator.getLibrary();
        windowManager.tab1(library);

        String[] input = windowManager.readCommandLine();
        while (true) {
            final String command = input[0];
            String[] args;
            if (input.length > 1) {
                args = Arrays.copyOfRange(input, 0, input.length - 2);
            }
            switch (command) {
                case "1" -> windowManager.tab1(library);
                case "2" -> windowManager.tab2(library);
                case "3" -> windowManager.tab3(library);
                case "help" -> windowManager.help();
                case "search", "s" -> {
                    // TODO
                }
                case "add" -> {
                    if (currentDataType == HISTORY) {
//                        dataPrinter.printErrorMessage("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(ADD, currentDataType));
                    }
                }
                case "delete", "del" -> {
                    if (currentDataType == HISTORY) {
//                        dataPrinter.printErrorMessage("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(DEL, currentDataType));
                    }
                }
                case "exit" -> {
                    requestProcessor.process(library, new Request(EXIT, null));
                }
//                default -> dataPrinter.printErrorMessage(format("Unrecognizable command '%s'", command));
            }
            input = windowManager.readCommandLine();
        }
    }
}
