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
import ua.mibal.minervaTest.component.request.RequestConfigurator;
import ua.mibal.minervaTest.component.request.RequestProcessor;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Request;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Application {

    private final DataOperator dataOperator;

    private final RequestConfigurator requestConfigurator;

    private final RequestProcessor requestProcessor;

    public Application(final DataOperator dataOperator,
                       final RequestConfigurator requestConfigurator,
                       final RequestProcessor requestProcessor) {
        this.dataOperator = dataOperator;
        this.requestConfigurator = requestConfigurator;
        this.requestProcessor = requestProcessor;
    }

    public void start() {
        Library library = dataOperator.getLibrary();
        while (!requestProcessor.isExit()) {
            final Request request = requestConfigurator.configure();
            requestProcessor.process(library, request);

            if (requestProcessor.isUpdate()) {
                library = dataOperator.getLibrary();
            }
        }
    }
}
