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

package ua.mibal.minervaTest.component;

import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;

import java.util.Collection;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface DataPrinter {

    void printListOfBooks(Collection<Book> books);

    void printListOfClients(Collection<Client> clients);

    void printListOfOperations(Collection<Operation> operations);

    void printBookDetails(Book book);

    void printClientDetails(Client client);

    void printOperationDetails(Operation operation);

    void clear();
}
