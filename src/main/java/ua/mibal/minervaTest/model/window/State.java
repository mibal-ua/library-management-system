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

package ua.mibal.minervaTest.model.window;

import ua.mibal.minervaTest.model.command.DataType;
import static ua.mibal.minervaTest.model.command.DataType.BOOK;
import static ua.mibal.minervaTest.model.command.DataType.CLIENT;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public enum State {

    WINDOW_1(BOOK),

    WINDOW_2(CLIENT),

    WINDOW_3(HISTORY),

    HELP_WINDOW(null);

    private final DataType dataType;

    State(final DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }
}
