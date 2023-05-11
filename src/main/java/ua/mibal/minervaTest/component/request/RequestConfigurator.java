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

package ua.mibal.minervaTest.component.request;

import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.component.UserInputReader;
import ua.mibal.minervaTest.model.Request;
import ua.mibal.minervaTest.model.command.CommandType;
import ua.mibal.minervaTest.model.command.DataType;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.CommandType.EXIT;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class RequestConfigurator {

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    public RequestConfigurator(final DataPrinter dataPrinter, final UserInputReader inputReader) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
    }

    public Request configure() {
        dataPrinter.printInfoMessage("Enter command you need:");
        dataPrinter.printInfoMessage("""
            
            /{command-type} {data-type}
                        
            command-types:
            - GET
            - POST
            - PATCH
                        
            data-types:
            - BOOK
            - CLIENT
            - HISTORY
              
            or /exit
            """);
        while (true) {
            String input = inputReader.getUserInput();
            if (!input.startsWith("/")) {
                dataPrinter.printInfoMessage("Command must starts with '/' symbol");
                dataPrinter.printInfoMessage("Enter command:");
                continue;
            }

            if (input.equalsIgnoreCase("/exit")) {
                return new Request(EXIT, null);
            }

            String[] command = input.substring(1).split(" ");
            if (command.length < 2) {
                dataPrinter.printInfoMessage("Command is too short");
                dataPrinter.printInfoMessage("Enter command:");
                continue;
            }

            String commandTypeStr = command[0];
            if (!CommandType.contains(commandTypeStr)) {
                dataPrinter.printInfoMessage(format(
                    "Unrecognizable command type '%s'", commandTypeStr
                ));
                dataPrinter.printInfoMessage("Enter command:");
                continue;
            }
            CommandType commandType = CommandType.valueOf(commandTypeStr.toUpperCase());

            String dataTypeStr = command[1];
            if (!DataType.contains(dataTypeStr)) {
                dataPrinter.printInfoMessage(format(
                    "Unrecognizable data type '%s'", dataTypeStr
                ));
                dataPrinter.printInfoMessage("Enter command:");
                continue;
            }
            DataType dataType = DataType.valueOf(dataTypeStr.toUpperCase());

            return new Request(commandType, dataType);
        }
    }
}
