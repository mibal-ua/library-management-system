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

package ua.mibal.minervaTest.component.console;

import ua.mibal.minervaTest.model.window.State;
import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.component.console.ConsoleDataPrinter.bold;
import static ua.mibal.minervaTest.model.window.State.HELP_TAB;
import static ua.mibal.minervaTest.model.window.State.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.State.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.State.LOOK_HISTORY;
import static ua.mibal.minervaTest.model.window.State.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.State.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.State.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class TabsContainer {

    public final Tab tab1 = new Tab(
        new String[] { "BOOKS", "CLIENTS", "HISTORY" },
        0,
        TAB_1
    );

    public final Tab tab2 = new Tab(
        new String[] { "BOOKS", "CLIENTS", "HISTORY" },
        1,
        TAB_2
    );

    public final Tab tab3 = new Tab(
        new String[] { "BOOKS", "CLIENTS", "HISTORY" },
        2,
        TAB_3
    );

    public final Tab help = new Tab(
        new String[] { "HELP" },
        0,
        HELP_TAB
    ).setDataCaching((/* stub */) -> System.out.print(""))
        .setBody(() -> System.out.println("""
                                              
                                              MAIN CONTROL
            --------------------------------------------------------------------------------
                       
                                           1, 2, 3 - open tab
                                        
                                              exit - to exit
                                                
                                                
                                                IN TABS
            --------------------------------------------------------------------------------
                               
                      search(s) ${query} - search element in current tab
                                 
                              look ${id} - look at concrete item in list in current tab
                                     
                                     add - to add element into list in current tab
                                     
                       delete(del) ${id} - to delete element in list in current tab
                           
                           
                                         IN CONCRETE BOOK/CLIENT
            --------------------------------------------------------------------------------
                                         
                                       edit - to edit this book/client
                                      
                                delete(del) - to delete this book/client
                               
                                        esc - go to previous window
                             
                           
                                           IN CONCRETE CLIENT
            --------------------------------------------------------------------------------
                         
                                        take ${id} - to take book
                                
                                      return ${id} - to return book
                 
             """));

    public final Tab searchBookTab = new Tab(
        SEARCH_BOOK
    );

    public final Tab searchClientTab = new Tab(
        SEARCH_CLIENT
    );

    public final Tab searchOperationTab = new Tab(
        SEARCH_HISTORY
    );

    public final Tab bookDetails = new Tab(
        new String[] { "BOOK DETAILS" },
        0,
        LOOK_BOOK
    );

    public final Tab clientDetails = new Tab(
        new String[] { "CLIENT DETAILS" },
        0,
        LOOK_CLIENT
    );

    public final Tab operationDetails = new Tab(
        new String[] { "OPERATION DETAILS" },
        0,
        LOOK_HISTORY
    );

    private final Runnable clearScreen;

    private final Consumer<State> cacheTab;

    private final int windowWidth;

    public TabsContainer(final Runnable clearScreen,
                         final Consumer<State> cacheTab,
                         final int windowWidth) {
        this.clearScreen = clearScreen;
        this.cacheTab = cacheTab;
        this.windowWidth = windowWidth;
    }

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    public class Tab {

        private final State stateToCache;

        private String[] tabsNames;

        private int boldTab;

        private Runnable dataCaching;

        private Runnable body;

        public Tab(final String[] tabsNames,
                   final int boldTab,
                   final State stateToCache) {
            this.tabsNames = tabsNames;
            if (-1 < boldTab && boldTab < tabsNames.length) {
                this.boldTab = boldTab;
            } else {
                this.boldTab = -1;
            }
            this.stateToCache = stateToCache;
        }

        public Tab(final State stateToCache) {
            this.stateToCache = stateToCache;
        }

        public void draw() {
            // beforeAll
            clearScreen.run();

            // header
            final int allWordsLength = Arrays
                .stream(tabsNames)
                .reduce(0,
                    (acc, str) -> acc + str.length(),
                    Integer::sum);
            final int spaceLength = (windowWidth - allWordsLength) / (tabsNames.length + 1);
            final String space = " ".repeat(spaceLength);

            final StringBuilder message = new StringBuilder(space);
            for (int i = 0; i < tabsNames.length; i++) {
                String tabsName = i == boldTab
                    ? bold(tabsNames[i])
                    : tabsNames[i];
                message.append(tabsName)
                    .append(space);
            }

            System.out.println();
            System.out.println(message);
            System.out.println();

            // body
            requireNonNull(body);
            body.run();

            // caching
            requireNonNull(dataCaching);
            dataCaching.run();
            if (stateToCache != null) {
                cacheTab.accept(stateToCache);
            }

            // margin afterAll
            System.out.println();
            System.out.println();
        }

        public Tab setBody(final Runnable body) {
            this.body = body;
            return this;
        }

        public Tab setDataCaching(final Runnable dataCaching) {
            this.dataCaching = dataCaching;
            return this;
        }

        public Tab setTabsNames(final String[] tabsNames, final int boldTab) {
            this.tabsNames = tabsNames;
            if (-1 < boldTab && boldTab < tabsNames.length) {
                this.boldTab = boldTab;
            } else {
                this.boldTab = -1;
            }
            return this;
        }
    }
}
