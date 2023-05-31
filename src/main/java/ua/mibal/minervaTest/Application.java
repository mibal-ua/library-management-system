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

    private final Map<String, OwnFunction> commandMap;

    private final WindowManager windowManager;

    public Application(final DataOperator dataOperator,
                       final WindowManager windowManager) {
        this.windowManager = windowManager;
        final ApplicationController applicationController = new ApplicationController(
            windowManager,
            dataOperator,
            dataOperator.getLibrary()
        );

        commandMap = new HashMap<>();
        commandMap.put("1", applicationController::tab1);
        commandMap.put("2", applicationController::tab2);
        commandMap.put("3", applicationController::tab3);
        commandMap.put("esc", applicationController::esc);
        commandMap.put("help", applicationController::help);
        commandMap.put("search", applicationController::search);
        commandMap.put("s", applicationController::search);
        commandMap.put("look", applicationController::look);
        commandMap.put("add", applicationController::add);
        commandMap.put("delete", applicationController::delete);
        commandMap.put("del", applicationController::delete);
        commandMap.put("take", applicationController::take);
        commandMap.put("return", applicationController::returnn);
    }

    public void start() {
        commandMap.get("1").apply(null);
        while (true) {
            String[] input = windowManager.readCommandLine();
            final String command = input[0];
            final String[] args = Arrays.copyOfRange(input, 1, input.length);

            if (command.equals("exit")) {
                final boolean isExit = windowManager.showDialogueToast(
                    "You really need to exit?", "YES", "NO");
                if (isExit) {
                    return;
                }
            } else {
                commandMap.getOrDefault(command,
                        ignored -> windowManager.showToast(format("Unrecognizable command '%s'", command)))
                    .apply(args);
            }
        }
    }

    @FunctionalInterface
    private interface OwnFunction {

        void apply(String[] args);
    }
}
