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

import ua.mibal.minervaTest.dao.Dao;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.State;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface WindowManager {

    void tab1(List<Book> books);

    void tab2(List<Client> clients);

    void tab3(List<Operation> operations, Dao<Client> clientDao);

    void help();

    String[] readCommandLine();

    void showToast(final String message);

    boolean showDialogueToast(String question, String answer1, String answer2);

    void searchBookTab(List<Book> books, String[] args);

    void searchClientTab(List<Client> clients, String[] args);

    void searchOperationTab(List<Operation> operations, Dao<Client> clientDao, String[] args);

    Optional<Book> initBookToAdd();

    Optional<Client> initClientToAdd();

    DataType getCurrentDataType();

    State getCurrentTabState();

    void drawPrevTab();

    void bookDetails(Book book);

    void clientDetails(Client client, List<Book> books);

    void operationDetails(Operation operation, Client client, List<Book> books);

    String getCachedBookId();

    String getCachedClientId();

    Optional<Book> editBook(Book book);

    Optional<Client> editClient(Client client);
}

