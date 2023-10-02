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

import ua.mibal.minervaTest.model.Entity;

import java.util.Arrays;

import static ua.mibal.minervaTest.model.window.DataType.BOOK;
import static ua.mibal.minervaTest.model.window.DataType.CLIENT;
import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.DataType.NULL;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public enum TabType {

    TAB_1(BOOK),

    TAB_2(CLIENT),

    TAB_3(HISTORY),

    HELP_TAB(NULL),

    SEARCH_BOOK(BOOK),

    SEARCH_CLIENT(CLIENT),

    SEARCH_HISTORY(HISTORY),

    LOOK_BOOK(BOOK),

    LOOK_CLIENT(CLIENT),

    LOOK_HISTORY(HISTORY);

    private final DataType dataType;

    TabType(final DataType dataType) {
        this.dataType = dataType;
    }

    public static TabType getRootTabOf(Class<? extends Entity> clazz) {
        return Arrays.stream(values())
                .filter(tabType -> tabType.isRootTab() &&
                                   tabType.getDataType() == DataType.valueOf(clazz))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Illegal class '" + clazz + "' for TabType instance"));
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isDetailsTab() {
        return this == LOOK_BOOK ||
               this == LOOK_CLIENT;
    }

    public boolean isRootTab() {
        return this == TAB_1 ||
               this == TAB_2 ||
               this == TAB_3;
    }
}
