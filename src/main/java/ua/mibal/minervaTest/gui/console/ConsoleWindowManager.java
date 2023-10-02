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

package ua.mibal.minervaTest.gui.console;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.gui.DataPrinter;
import ua.mibal.minervaTest.gui.WindowManager;
import ua.mibal.minervaTest.gui.drawable.TabsStack;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleDialogueToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleFormToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleInfoToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleTab;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.model.window.TabType;
import ua.mibal.minervaTest.utils.Books;
import ua.mibal.minervaTest.utils.Clients;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.join;
import static ua.mibal.minervaTest.model.window.TabType.HELP_TAB;
import static ua.mibal.minervaTest.model.window.TabType.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.TabType.TAB_1;
import static ua.mibal.minervaTest.model.window.TabType.TAB_2;
import static ua.mibal.minervaTest.model.window.TabType.TAB_3;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ConsoleWindowManager implements WindowManager {

    private final DataPrinter dataPrinter;
    private final Stack<ConsoleTab> tabStack = new TabsStack();

    public ConsoleWindowManager(DataPrinter dataPrinter) {
        this.dataPrinter = dataPrinter;
    }

    // FIXME stub
    public void listTab(Supplier<List<? extends Entity>> listSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfEntities(listSupplier.get()),
                TabType.getRootTabOf(listSupplier.get().iterator().next().getClass()), // FIXME stub
                0, // FIXME stub
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void tab1(Supplier<List<Book>> booksSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfEntities(booksSupplier.get()),
                TAB_1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    public void tab2(Supplier<List<Client>> clientsSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfEntities(clientsSupplier.get()),
                TAB_2,
                1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    public void tab3(Supplier<List<Operation>> operationsSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfEntities(operationsSupplier.get()),
                TAB_3,
                2,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public <T extends Entity> void searchTab(Supplier<List<T>> entitiesSupplier, String[] args) {
        TabType tabType = getCurrentTabType();
        final String header = format("SEARCH IN " + tabType.name() + " BY '%s'", join(" ", args));
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfEntities(entitiesSupplier.get()),
                tabType,
                header
        )).draw();
    }

    @Override
    public void help() {
        tabStack.push(new ConsoleTab(
                dataPrinter::printHelp,
                HELP_TAB,
                "HELP"
        )).draw();
    }

    @Override
    public String[] readCommandLine() {
        return ConsoleUtils.readInput();
    }

    @Override
    public void showToast(String message) {
        new ConsoleInfoToast(message)
                .draw()
                .perform(this::refresh);
    }

    @Override
    public void showToast(String header, String message) {
        new ConsoleInfoToast(header, message)
                .draw()
                .perform(this::refresh);
    }

    @Override
    public boolean showDialogueToast(String question, String answer1, String answer2) {
        return new ConsoleDialogueToast(
                "Confirmation",
                question,
                answer1,
                answer2
        ).draw()
                .perform(this::refresh)
                .getAnswer();
    }

    @Override
    public <T extends Entity> Optional<T> initEntityToAdd(Class<T> entityClass) {
        // FIXME stub
        if (entityClass == Book.class) {
            return (Optional<T>) initBookToAdd();
        }
        if (entityClass == Client.class) {
            return (Optional<T>) initClientToAdd();
        }
        throw new IllegalStateException("Illegal for editing entity class " + entityClass);
    }

    private Optional<Book> initBookToAdd() {
        return new ConsoleFormToast(
                "Book adding",
                new LinkedHashMap<>() {{
                    put("Enter book title", "");
                    put("Enter subtitle", "");
                    put("Enter author", "");
                    put("Enter publish date in format 2018-12-31", "");
                    put("Enter book publisher", "");
                }},
                "/stop",
                "Lets create book! You can stop everywhere by entering '/stop'"
        ).draw()
                .perform(this::refresh)
                .getAnswers()
                .map(Books::ofMapping);
    }

    private Optional<Client> initClientToAdd() {
        return new ConsoleFormToast(
                "Client adding",
                Map.of("Enter client name", ""),
                "/stop",
                "Lets add client! You can stop everywhere by entering '/stop'"
        ).draw()
                .perform(this::refresh)
                .getAnswers()
                .map(Clients::ofMapping);
    }

    @Override
    public <T extends Entity> Optional<T> editEntity(T entity) {
        // FIXME stub
        if (entity instanceof Book book) {
            return (Optional<T>) editBook(book);
        }
        if (entity instanceof Client client) {
            return (Optional<T>) editClient(client);
        }
        throw new IllegalStateException("Illegal for editing entity class " + entity.getClass());
    }

    private Optional<Book> editBook(Book originalBook) {
        return new ConsoleFormToast(
                "Book editing",
                new LinkedHashMap<>() {{
                    put("Enter title", "prev book title: '" + originalBook.getTitle() + "'");
                    put("Enter subtitle", "prev book subtitle: '" + originalBook.getSubtitle() + "'");
                    put("Enter author", "prev book author: '" + originalBook.getAuthor() + "'");
                    put("Enter publish date in format 2018-12-31", "prev book publish date: '" + originalBook.getPublishedDate() + "'");
                    put("Enter book publisher", "prev book publisher: '" + originalBook.getPublisher() + "'");
                }},
                "/stop",
                "Lets edit book! You can stop by entering '/stop'",
                "If you wanna edit field, enter data.\nIf you wanna skip editing, click Enter."
        ).draw()
                .perform(this::refresh)
                .getAnswers()
                .map(answers -> {
                    Book editedBook = Books.copyOf(originalBook);
                    return Books.changeByMapping(editedBook, answers);
                });
    }

    private Optional<Client> editClient(final Client originalClient) {
        return new ConsoleFormToast(
                "Client editing",
                Map.of("Enter name", "prev client name: '" + originalClient.getName() + "'"),
                "/stop",
                "Lets edit client! You can stop by entering '/stop'",
                "If you wanna edit field, enter data.\nIf you wanna skip editing, click Enter."
        ).draw()
                .perform(this::refresh)
                .getAnswers()
                .map(answers -> {
                    Client editedClient = Clients.copyOf(originalClient);
                    return Clients.changeByMapping(editedClient, answers);
                });
    }

    @Override
    public Long getCurrentEntityId() throws IllegalArgumentException {
        return this.tabStack.peek().getEntityId();
    }

    @Override
    public DataType getCurrentDataType() {
        return this.tabStack.peek().getTabType().getDataType();
    }

    private TabType getCurrentTabType() {
        return this.tabStack.peek().getTabType();
    }

    @Override
    public boolean isDetailsTab() {
        return this.tabStack.peek().getTabType().isDetailsTab();
    }

    @Override
    public void drawPrevTab() {
        dataPrinter.clear();
        if (this.tabStack.size() > 1)
            this.tabStack.pop();
        this.tabStack.peek().draw();
    }

    @Override
    public <T extends Entity> void detailsTab(Supplier<Optional<T>> optionalSupplier) {
        Long id = optionalSupplier.get()
                .map(Entity::getId)
                .orElse(Long.valueOf(-1));
        tabStack.push(new ConsoleTab(
                () -> optionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printEntityDetails,
                        this::drawPrevTab
                ),
                LOOK_BOOK, // FIXME stub
                id,
                "BOOK DETAILS" // FIXME stub
        )).draw();
    }

    private void refresh() {
        dataPrinter.clear();
        this.tabStack.peek().draw();
    }
}
