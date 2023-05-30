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


import ua.mibal.minervaTest.component.ApplicationController;
import ua.mibal.minervaTest.component.DataOperator;
import ua.mibal.minervaTest.component.WindowManager;
import static java.lang.String.format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Application {

    private final DataOperator dataOperator;

    private final WindowManager windowManager;

    public Application(final DataOperator dataOperator,
                       final WindowManager windowManager) {
        this.dataOperator = dataOperator;
        this.windowManager = windowManager;
    }

    public void start() {
        ApplicationController applicationController =
            new ApplicationController(windowManager, dataOperator, dataOperator.getLibrary());

        Map<String, OwnFunction> commandMap = new HashMap<>(Map.of(
            "1", (args) -> applicationController.tab1(),
            "2", (args) -> applicationController.tab2(),
            "3", (args) -> applicationController.tab3(),
            "esc", (esc) -> applicationController.esc(),
            "help", (args) -> applicationController.help(),
            "exit", applicationController::exit,
            "search", applicationController::search,
            "look", applicationController::look,
            "s", applicationController::search,
            "add", applicationController::add
        ));
        commandMap.putAll(Map.of(
            "delete", applicationController::delete,
            "del", applicationController::delete,
            "take", applicationController::take,
            "return", applicationController::returnn,
            "exit", applicationController::exit
        ));

        applicationController.tab1();
        while (true) {
            String[] input = windowManager.readCommandLine();
            final String command = input[0];
            final String[] args = Arrays.copyOfRange(input, 1, input.length);

            commandMap.getOrDefault(command,
                    com -> windowManager.showToast(format("Unrecognizable command '%s'", command)))
                .apply(args);
        }
    }

    @FunctionalInterface
    private interface OwnFunction {

        void apply(String[] args);
    }
}
