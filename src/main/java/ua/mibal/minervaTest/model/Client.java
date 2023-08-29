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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "client")
    private Set<Book> books = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    private Set<Operation> operations = new HashSet<>();

    public Client(final String name) {
        this.name = name;
    }

    public Client(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Client() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    private void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    private void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public void addBooks(final Collection<Book> books) {
        books.forEach(book -> {
            this.books.add(book);
            book.setClient(this);
        });
    }

    public void removeBooks(final Collection <Book> books) {
        books.forEach(book -> {
            this.books.remove(book);
            book.setClient(null);
        });
    }

    public boolean doesHoldBook() {
        return !books.isEmpty();
    }
}
