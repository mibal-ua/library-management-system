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
import static ua.mibal.minervaTest.model.Library.HaveId;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Book implements Serializable, HaveId {

    private String id;

    private String title;

    private String subtitle;

    private String author;

    @JsonProperty("published")
    private String publishedDate;

    private String publisher;

    @JsonProperty("isFree")
    private boolean isFree;

    public Book(final String title, final String subtitle, final String author,
                final String publishedDate, final String publisher,
                final boolean isFree) {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.isFree = isFree;
    }

    public Book() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(final boolean free) {
        isFree = free;
    }
}
