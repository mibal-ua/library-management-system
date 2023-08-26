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

package ua.mibal.minervaTest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ua.mibal.minervaTest.model.Library.HaveId;
import ua.mibal.minervaTest.utils.IdUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Client implements Serializable {

    private String id;

    private String name;

    @JsonProperty("books")
    private List<String> booksIds;

    public Client(final String name, final List<String> booksIds) {
        this(IdUtils.generateId(), name, booksIds);
    }

    public Client(final String name) {
        this(IdUtils.generateId(), name, List.of());
    }

    public Client(final String id, final String name, final List<String> booksIds) {
        this.id = id;
        this.name = name;
        this.booksIds = new ArrayList<>(booksIds);
    }

    public Client() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getBooksIds() {
        return Collections.unmodifiableList(booksIds);
    }

    public void addBooks(final List<String> booksIds) {
        this.booksIds.addAll(booksIds);
    }

    public void removeBooks(final List<String> bookIdsToReturn) {
        this.booksIds.removeAll(bookIdsToReturn);
    }

    public boolean doesHoldBook() {
        return !booksIds.isEmpty();
    }
}
