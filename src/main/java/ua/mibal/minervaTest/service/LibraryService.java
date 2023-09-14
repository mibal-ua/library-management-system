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

package ua.mibal.minervaTest.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.mibal.minervaTest.dao.BookDao;
import ua.mibal.minervaTest.dao.ClientDao;
import ua.mibal.minervaTest.dao.OperationDao;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Service
@Transactional
public class LibraryService {

    private final BookDao bookDao;
    private final OperationDao operationDao;
    private final ClientDao clientDao;

    public LibraryService(final BookDao bookDao,
                          final OperationDao operationDao,
                          final ClientDao clientDao) {
        this.bookDao = bookDao;
        this.operationDao = operationDao;
        this.clientDao = clientDao;
    }

    public void takeBook(Client client, Book bookToTake) {
        // TODO
    }

    public void returnBook(Client client, Book bookToReturn) {
        // TODO
    }
}
