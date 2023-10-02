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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Entity
@Table(name = "operation")
public class Operation implements Serializable, ua.mibal.minervaTest.model.Entity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @JoinColumn(name = "client_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Client client;

    @Column(name = "operation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    public Operation(Long id, LocalDateTime date, Client client, OperationType operationType, Book book) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.operationType = operationType;
        this.book = book;
    }

    public Operation(LocalDateTime date, Client client, OperationType operationType, Book book) {
        this.date = date;
        this.client = client;
        this.operationType = operationType;
        this.book = book;
    }

    public Operation() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Book getBook() {
        return book;
    }

    private void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean isReadyToDelete() {
        throw new IllegalArgumentException("You are trying to change history manually");
    }

    @Override
    public String getNotDeleteReason() {
        throw new IllegalArgumentException("You are trying to change history manually");
    }

    @Override
    public String getName() {
        throw new IllegalArgumentException("You are trying to change history manually");
    }
}
