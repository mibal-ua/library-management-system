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

import ua.mibal.minervaTest.component.console.ConsoleWindowManager.Tab;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;

import java.util.Stack;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class CacheManager {

    public final Stack<Tab> stack = new Stack<>();
    private Book cachedBook;
    private Client cachedClient;

    public void push(final Tab tab){
        stack.push(tab);
    }

    public void push(final Book book){
        this.cachedBook = book;
    }

    public void push(final Client client){
        this.cachedClient = client;
    }

    public Book getBook() {
        return cachedBook;
    }

    public Client getClient() {
        return cachedClient;
    }
}
