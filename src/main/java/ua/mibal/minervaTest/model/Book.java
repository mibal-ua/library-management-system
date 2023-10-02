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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Entity
@Table(name = "book")
public class Book implements Serializable, ua.mibal.minervaTest.model.Entity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle", nullable = false)
    private String subtitle;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "published", nullable = false)
    private LocalDateTime publishedDate;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "free", nullable = false)
    private boolean free;

    @JoinColumn(name = "client_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    public Book(final Long id,
                final String title,
                final String subtitle,
                final String author,
                final LocalDateTime publishedDate,
                final String publisher,
                final boolean free) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.free = free;
    }

    public Book(final String title,
                final String subtitle,
                final String author,
                final LocalDateTime publishedDate,
                final String publisher,
                final boolean free) {
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.free = free;
    }

    public Book() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Optional<Client> getClient() {
        return Optional.ofNullable(client);
    }

    public void setClient(Client client) {
        this.client = client;
        this.free = client == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean isReadyToDelete() {
        return free;
    }

    @Override
    public String getNotDeleteReason() {
        return "isn't free";
    }

    @Override
    public String getName() {
        return title;
    }
}
