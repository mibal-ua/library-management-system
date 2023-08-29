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

package ua.mibal.minervaTest.dao;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.Book;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class BookDao extends Dao<Book> {

    public BookDao(QueryHelper queryHelper) {
        super(queryHelper, Book.class);
    }

    @Override
    protected boolean appropriateSelectingAddingLogic(Book book, String arg, List<Book> result) {
        if (book.getId().toString().equals(arg)) {
            result.add(book);
            return false;
        }
        if (book.getTitle().contains(arg) || // TODO add search by Date
            book.getAuthor().contains(arg) ||
            book.getPublisher().contains(arg)) {
            result.add(book);
        }
        return true;
    }
}
