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

import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Operation;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class CacheManager {

    protected CachedSearchArgs<Book> searchBook;

    protected CachedSearchArgs<Client> searchClient;

    protected CachedSearchArgs<Operation> searchOperation;

    protected CachedClientDetailsArgs clientDetails;

    protected CachedOperationDetailsArgs operationDetails;

    protected Book bookDetails;

    protected Library library;

    // bookDetails
    public void cache(final Book book) {
        bookDetails = book;
    }

    // clientDetails
    public void cache(final Client client, final List<Book> books) {
        clientDetails = new CachedClientDetailsArgs(client, books);
    }

    // operationDetails
    public void cache(final Operation operation, final Client client, final List<Book> books) {
        operationDetails = new CachedOperationDetailsArgs(operation, client, books);
    }

    // tab 1/2/3
    public void cache(final Library library) {
        this.library = library;
    }

    // search${dataType}Tab
    public <T> void cache(final List<T> data, final String[] args) {
        // FIXME
        Class<T> clazz = (Class<T>) data.get(0).getClass();
        if (clazz == Book.class) {
            this.searchBook = new CachedSearchArgs<>((List<Book>) data, args);
        } else if (clazz == Client.class) {
            this.searchClient = new CachedSearchArgs<>((List<Client>) data, args);
        } else if (clazz == Operation.class) {
            this.searchOperation = new CachedSearchArgs<>((List<Operation>) data, args);
        } else {
            throw new IllegalStateException("Unexpected value: " + clazz);
        }
    }


    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    protected record CachedSearchArgs<T>(List<T> data, String[] args) {

    }

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    protected record CachedClientDetailsArgs(Client client, List<Book> books) {

    }

    /**
     * @author Mykhailo Balakhon
     * @link t.me/mibal_ua
     */
    protected record CachedOperationDetailsArgs(Operation operation, Client client, List<Book> books) {

    }

}
