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

package ua.mibal.minervaTest.gui;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.gui.drawable.DialogueToast;
import ua.mibal.minervaTest.gui.drawable.FormToast;
import ua.mibal.minervaTest.gui.drawable.InfoToast;
import ua.mibal.minervaTest.gui.drawable.Tab;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.of;
import static ua.mibal.minervaTest.model.window.TabType.HELP_TAB;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_HISTORY;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.TabType.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.TabType.TAB_1;
import static ua.mibal.minervaTest.model.window.TabType.TAB_2;
import static ua.mibal.minervaTest.model.window.TabType.TAB_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class NewConsoleWindowManager implements WindowManager {

    private final DataPrinter dataPrinter;
    private final Stack<Tab> tabStack = new TabsStack();

    public NewConsoleWindowManager(DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    @Override
    public void tab1(Supplier<List<Book>> booksSupplier) {
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                TAB_1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void tab2(Supplier<List<Client>> clientsSupplier) {
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                TAB_2,
                1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void tab3(Supplier<List<Operation>> operationsSupplier) {
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                TAB_3,
                2,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void searchBookTab(final Supplier<List<Book>> booksSupplier, final String[] args) {
        final String header = format("SEARCH IN BOOKS BY '%s'", join(" ", args));
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                SEARCH_BOOK,
                header
        )).draw();
    }

    @Override
    public void searchClientTab(final Supplier<List<Client>> clientsSupplier, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                SEARCH_CLIENT,
                header
        )).draw();
    }

    @Override
    public void searchOperationTab(final Supplier<List<Operation>> operationsSupplier, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        tabStack.push(new Tab(
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                SEARCH_HISTORY,
                header
        )).draw();
    }

    @Override
    public void help() {
        tabStack.push(new Tab(
                () -> System.out.println("""

                                                          MAIN CONTROL
                        --------------------------------------------------------------------------------

                                                       1, 2, 3 - open tab

                                                          exit - to exit


                                                            IN TABS
                        --------------------------------------------------------------------------------

                                  search(s) ${query} - search element in current tab

                                          look ${id} - look at concrete item in list in current tab

                                                 add - to add element into list in current tab

                                   delete(del) ${id} - to delete element in list in current tab


                                                     IN CONCRETE BOOK/CLIENT
                        --------------------------------------------------------------------------------

                                                   edit - to edit this book/client

                                            delete(del) - to delete this book/client

                                                    esc - go to previous window


                                                       IN CONCRETE CLIENT
                        --------------------------------------------------------------------------------

                                                    take ${id} - to take book

                                                  return ${id} - to return book

                         """),
                HELP_TAB,
                0,
                "HELP"
        )).draw();
    }

    @Override
    public String[] readCommandLine() {
        return ConsoleUtils.readInput();
    }

    @Override
    public void showToast(String message) {
        new InfoToast(message).draw();
        refresh();
    }

    @Override
    public boolean showDialogueToast(String question, String answer1, String answer2) {
        DialogueToast dialogueToast = new DialogueToast(question, answer1, answer2)
                .draw();
        refresh();
        return dialogueToast.getAnswer();
    }


    @Override
    public Optional<Book> initBookToAdd() {
        List<String> questions = of(
                "Enter book title",
                "Enter subtitle",
                "Enter author",
                "Enter publish date (in format '2018-12-04')",
                "Enter book publisher"
        );

        Optional<List<String>> answers = new FormToast(
                "Lets create book! You can stop everywhere by entering '/stop'", questions, "/stop"
        ).draw().getAnswers();
        refresh();

        if (answers.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Book(
                iterator.next(),
                iterator.next(),
                iterator.next(),
                // TODO FIXME stub
                LocalDateTime.now(), // iterator.next(),
                iterator.next(),
                true
        ));
    }

    @Override
    public Optional<Client> initClientToAdd() {
        List<String> questions = of(
                "Enter client name"
        );

        Optional<List<String>> answers = new FormToast(
                "Lets add client! You can stop everywhere by entering '/stop'", questions, "/stop"
        ).draw().getAnswers();
        refresh();

        if (answers.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        Iterator<String> iterator = answers.get().listIterator();
        return Optional.of(new Client(
                // TODO FIXME stub
                iterator.next()
        ));
    }

    @Override
    public DataType getCurrentDataType() {
        return getCurrentTabState().getDataType();
    }

    @Override
    public TabType getCurrentTabState() {
        return this.tabStack.peek().getTabType();
    }

    @Override
    public void drawPrevTab() {
        dataPrinter.clear();
        if (this.tabStack.size() > 1)
            this.tabStack.pop();
        this.tabStack.peek().draw();
    }

    @Override
    public void bookDetails(Supplier<Optional<Book>> bookOptionalSupplier) {
        tabStack.push(new Tab(
                () -> bookOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printBookDetails,
                        this::drawPrevTab
                ),
                LOOK_BOOK,
                "BOOK DETAILS"
        )).draw();
    }

    @Override
    public void clientDetails(Supplier<Optional<Client>> clientOptionalSupplier) {
        tabStack.push(new Tab(
                () -> clientOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printClientDetails,
                        this::drawPrevTab
                ),
                LOOK_CLIENT,
                "CLIENT DETAILS"
        )).draw();
    }

    @Override
    public void operationDetails(Supplier<Optional<Operation>> operationOptionalSupplier) {
        tabStack.push(new Tab(
                () -> operationOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printOperationDetails,
                        this::drawPrevTab
                ),
                LOOK_HISTORY,
                "OPERATION DETAILS"
        )).draw();
    }

    @Override
    public Optional<Book> editBook(Book originalBook) {
        List<String> messages = of(
                format("prev book title: '%s'", originalBook.getTitle()),
                format("prev book subtitle: '%s'", originalBook.getSubtitle()),
                format("prev book author: '%s'", originalBook.getAuthor()),
                format("prev book publish date: '%s'", originalBook.getPublishedDate()),
                format("prev book publisher: '%s'", originalBook.getPublisher())
        );

        new InfoToast("Lets edit book! You can stop by entering '/stop'").draw();
        refresh();
        new InfoToast("If you wanna edit this field, enter data").draw();
        refresh();
        Optional<List<String>> answersOptional = new FormToast(
                "If you wanna skip editing, click enter", messages, "/stop"
        ).draw().getAnswers();
        refresh();

        if (answersOptional.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        List<String> answers = answersOptional.get();

        return Optional.of(new Book(
                originalBook.getId(),
                answers.get(0).equals("")
                        ? originalBook.getTitle()
                        : answers.get(0),
                answers.get(1).equals("")
                        ? originalBook.getSubtitle()
                        : answers.get(1),
                answers.get(2).equals("")
                        ? originalBook.getAuthor()
                        : answers.get(2),
                answers.get(3).equals("")
                        ? originalBook.getPublishedDate()
                        // TODO FIXME stub
                        : LocalDateTime.now(), // answers.get(3),
                answers.get(4).equals("")
                        ? originalBook.getPublisher()
                        : answers.get(4),
                originalBook.isFree()
        ));
    }

    @Override
    public Optional<Client> editClient(final Client originalClient) {
        List<String> messages = of(
                format("prev client name: '%s'", originalClient.getName())
        );

        new InfoToast("Lets edit client! You can stop by entering '/stop'").draw();
        refresh();
        new InfoToast("If you wanna edit this field, enter data").draw();
        refresh();
        Optional<List<String>> answersOptional = new FormToast(
                "If you wanna skip editing, click enter", messages, "/stop"
        ).draw().getAnswers();
        refresh();

        if (answersOptional.isEmpty()) {
            refresh();
            return Optional.empty();
        }

        List<String> answers = answersOptional.get();

        Client client = new Client(
                originalClient.getId(),
                answers.get(0).equals("")
                        ? originalClient.getName()
                        : answers.get(0)
        );
        originalClient.getBooks().forEach(client::addBook);
        return Optional.of(client);
    }

    @Override
    public WindowManager refresh() {
        dataPrinter.clear();
        this.tabStack.peek().draw();
        return this;
    }
}
