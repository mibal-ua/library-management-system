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
import ua.mibal.minervaTest.component.ApplicationController.ArrayConsumer;
import ua.mibal.minervaTest.component.AppropriateBookOperationController;
import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.gui.WindowManager;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class Application {

    private final ApplicationController applicationController;
    private final AppropriateBookOperationController appropriateBookOperationController;
    private final WindowManager windowManager;

    public Application(WindowManager windowManager,
                       ApplicationController applicationController,
                       AppropriateBookOperationController appropriateBookOperationController) {
        this.windowManager = windowManager;
        this.applicationController = applicationController;
        this.appropriateBookOperationController = appropriateBookOperationController;
    }

    public void start() {
        final Map<String, ArrayConsumer<String>> router = Map.ofEntries(
                entry("1", applicationController::tab1),
                entry("2", applicationController::tab2),
                entry("3", applicationController::tab3),
                entry("esc", applicationController::esc),
                entry("help", applicationController::help),
                entry("search", applicationController::search),
                entry("s", applicationController::search),
                entry("look", applicationController::look),
                entry("edit", applicationController::edit),
                entry("add", applicationController::add),
                entry("delete", applicationController::delete),
                entry("del", applicationController::delete),
                entry("take", appropriateBookOperationController::take),
                entry("return", appropriateBookOperationController::returnn)
        );

        router.get("1").apply();
        while (true) {
            String[] input = windowManager.readCommandLine();
            final String command = input[0];
            final String[] args = Arrays.copyOfRange(input, 1, input.length);

            if (command.equals("exit") && windowManager.showDialogueToast(
                    "You really need to exit?", "YES", "NO"))
                return;
            router.getOrDefault(command,
                    applicationController::unrecognizable).apply(args);
        }
    }
}
