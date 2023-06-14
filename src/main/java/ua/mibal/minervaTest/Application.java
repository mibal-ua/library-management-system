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


import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.component.ApplicationController;
import ua.mibal.minervaTest.component.ApplicationController.OwnFunction;
import ua.mibal.minervaTest.component.WindowManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class Application {

    private final ApplicationController applicationController;

    private final WindowManager windowManager;

    public Application(final WindowManager windowManager,
                       final ApplicationController applicationController) {
        this.windowManager = windowManager;
        this.applicationController = applicationController;
    }

    public void start() {
        Map<String, OwnFunction> commandMap = new HashMap<>();
        commandMap.put("1", applicationController::tab1);
        commandMap.put("2", applicationController::tab2);
        commandMap.put("3", applicationController::tab3);
        commandMap.put("esc", applicationController::esc);
        commandMap.put("help", applicationController::help);
        commandMap.put("search", applicationController::search);
        commandMap.put("s", applicationController::search);
        commandMap.put("look", applicationController::look);
        commandMap.put("edit", applicationController::edit);
        commandMap.put("add", applicationController::add);
        commandMap.put("delete", applicationController::delete);
        commandMap.put("del", applicationController::delete);
        commandMap.put("take", applicationController::take);
        commandMap.put("return", applicationController::returnn);

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
                commandMap.getOrDefault(command, ignored ->
                                windowManager.showToast(format("Unrecognizable command '%s'", command)))
                        .apply(args);
            }
        }
    }
}
