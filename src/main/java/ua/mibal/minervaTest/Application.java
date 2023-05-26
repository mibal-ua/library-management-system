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
import ua.mibal.minervaTest.model.window.State;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.CommandType.ADD;
import static ua.mibal.minervaTest.model.command.CommandType.DEL;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.State.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.State.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;
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
    private State currentTab = TAB_1;

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
            final String[] args = Arrays.copyOfRange(input, 1, input.length);
            switch (command) {
                case "1" -> {
                    windowManager.tab1(library);
                    currentTab = TAB_1;
                }
                case "2" -> {
                    windowManager.tab2(library);
                    currentTab = TAB_2;
                }
                case "3" -> {
                    windowManager.tab3(library);
                    currentTab = TAB_3;
                }
                case "help" -> windowManager.help();
                case "search", "s" -> {
                    if (args.length == 0) {
                        windowManager.showToast("You need to enter 'search' with ${query}");
                        break;
                    }
                    switch (currentTab.getDataType()) {
                        case BOOK -> {
                            windowManager.searchBookTab(library.findBooks(args), args);
                            currentTab = SEARCH_BOOK;
                        }
                        case CLIENT -> {
                            windowManager.searchClientTab(library.findClients(args), args);
                            currentTab = SEARCH_CLIENT;
                        }
                        case HISTORY -> {
                            windowManager.searchOperationTab(library.findOperations(args), args);
                            currentTab = SEARCH_HISTORY;
                        }
                    }
                }
                case "add" -> {
                    if (currentTab.getDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(ADD, currentDataType));
                    }
                }
                case "delete", "del" -> {
                    if (currentTab.getDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually");
                    } else {
                        // TODO
                        requestProcessor.process(library, new Request(DEL, currentDataType));
                    }
                }
                case "exit" -> {
                    final boolean isExit = windowManager.showDialogueToast(
                        "You really need to exit?", "YES", "NO");
                    if (isExit) {
                        return;
                    }
                }
                default -> windowManager.showToast(format("Unrecognizable command '%s'", command));
            }
            input = windowManager.readCommandLine();
        }
    }
}
