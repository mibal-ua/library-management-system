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
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Client implements Serializable, HaveId {

    private String id;

    private String name;

    @JsonProperty("books")
    private List<String> booksIds;

    public Client(final String name, final List<String> booksIds) {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        this.name = name;
        this.booksIds = booksIds;
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
        return booksIds;
    }

    public void setBooksIds(final List<String> booksIds) {
        this.booksIds = booksIds;
    }
}
