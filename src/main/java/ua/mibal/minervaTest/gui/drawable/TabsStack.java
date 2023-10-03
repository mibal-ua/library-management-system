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

package ua.mibal.minervaTest.gui.drawable;

import ua.mibal.minervaTest.gui.drawable.tab.ConsoleTab;

import java.util.Stack;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class TabsStack extends Stack<ConsoleTab> {

    @Override
    public ConsoleTab push(ConsoleTab tab) {
        if (tab.getTabType().isRootTab() &&
            !this.isEmpty() &&
            this.peek().getTabType().isRootTab()) {
            this.pop();
        }
        return super.push(tab);
    }
}
