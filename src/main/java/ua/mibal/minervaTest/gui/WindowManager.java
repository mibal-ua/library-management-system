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

package ua.mibal.minervaTest.gui;

import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface WindowManager {

    void tab1(Supplier<List<Book>> books);

    void tab2(Supplier<List<Client>> clients);

    void tab3(Supplier<List<Operation>> operations);

    void help();

    String[] readCommandLine();

    void showToast(final String message);

    boolean showDialogueToast(String question, String answer1, String answer2);

    void searchBookTab(Supplier<List<Book>> books, String[] args);

    void searchClientTab(Supplier<List<Client>> clients, String[] args);

    void searchOperationTab(Supplier<List<Operation>> operations, String[] args);

    Optional<Book> initBookToAdd();

    Optional<Client> initClientToAdd();

    DataType getCurrentDataType();

    TabType getCurrentTabState();

    void drawPrevTab();

    void bookDetails(Supplier<Optional<Book>> book);

    void clientDetails(Supplier<Optional<Client>> client);

    void operationDetails(Supplier<Optional<Operation>> operation);

    Optional<Book> editBook(Book book);

    Optional<Client> editClient(Client client);

    WindowManager refresh();
}

