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

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Book {

    private final String id;

    private final String title;

    private final String subtitle;

    private final String author;

    private final String publishedDate;

    private final String publisher;

    private final int pages;

    private final String description;

    private final String website;

    private final boolean isFree;

    public Book(final String id, final String title, final String subtitle, final String author,
                final String publishedDate, final String publisher, final int pages,
                final String description, final String website, final boolean isFree) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.pages = pages;
        this.description = description;
        this.website = website;
        this.isFree = isFree;
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

    public int getPages() {
        return pages;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isFree() {
        return isFree;
    }
}
