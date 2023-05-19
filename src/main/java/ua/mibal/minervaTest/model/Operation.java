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
import java.io.Serializable;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Operation implements Serializable {

    private String date;

    private String clientId;

    @JsonProperty("operation")
    private String operationType;

    @JsonProperty("books")
    private List<String> booksIds;

    public Operation(final String date, final String clientId, final String operationType,
                     final List<String> booksIds) {
        this.date = date;
        this.clientId = clientId;
        this.operationType = operationType;
        this.booksIds = booksIds;
    }

    public Operation() {
    }

    public String getDate() {
        return date;
    }

    public String getClientId() {
        return clientId;
    }

    public String getOperationType() {
        return operationType;
    }

    public List<String> getBooksIds() {
        return booksIds;
    }
}
