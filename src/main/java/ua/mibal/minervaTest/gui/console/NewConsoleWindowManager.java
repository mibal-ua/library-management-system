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
import ua.mibal.minervaTest.gui.TabsStack;
import ua.mibal.minervaTest.gui.WindowManager;
import ua.mibal.minervaTest.gui.drawable.Tab;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleDialogueToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleFormToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleInfoToast;
import ua.mibal.minervaTest.gui.drawable.console.ConsoleTab;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
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
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                TAB_1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void tab2(Supplier<List<Client>> clientsSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                TAB_2,
                1,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void tab3(Supplier<List<Operation>> operationsSupplier) {
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                TAB_3,
                2,
                "BOOKS", "CLIENTS", "HISTORY"
        )).draw();
    }

    @Override
    public void searchBookTab(final Supplier<List<Book>> booksSupplier, final String[] args) {
        final String header = format("SEARCH IN BOOKS BY '%s'", join(" ", args));
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfBooks(booksSupplier.get()),
                SEARCH_BOOK,
                header
        )).draw();
    }

    @Override
    public void searchClientTab(final Supplier<List<Client>> clientsSupplier, final String[] args) {
        final String header = format("SEARCH IN CLIENTS BY '%s'", join(" ", args));
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfClients(clientsSupplier.get()),
                SEARCH_CLIENT,
                header
        )).draw();
    }

    @Override
    public void searchOperationTab(final Supplier<List<Operation>> operationsSupplier, final String[] args) {
        final String header = format("SEARCH IN OPERATIONS BY '%s'", join(" ", args));
        tabStack.push(new ConsoleTab(
                () -> dataPrinter.printListOfOperations(operationsSupplier.get()),
                SEARCH_HISTORY,
                header
        )).draw();
    }

    @Override
    public void help() {
        tabStack.push(new ConsoleTab(
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
                "HELP"
        )).draw();
    }

    @Override
    public String[] readCommandLine() {
        return ConsoleUtils.readInput();
    }

    @Override
    public void showToast(String message) {
        new ConsoleInfoToast(message).draw();
        refresh();
    }

    @Override
    public boolean showDialogueToast(String question, String answer1, String answer2) {
        ConsoleDialogueToast dialogueToast = new ConsoleDialogueToast(
                "Confirmation",
                question,
                answer1,
                answer2
        ).draw();
        refresh();
        return dialogueToast.getAnswer();
    }

    @Override
    public Optional<Book> initBookToAdd() {
        Map<String, String> questions = new LinkedHashMap<>();
        questions.put("Enter book title", "");
        questions.put("Enter subtitle", "");
        questions.put("Enter author", "");
        questions.put("Enter publish date in format 2018-12-31", "");
        questions.put("Enter book publisher", "");
        Optional<List<String>> answersOptional = new ConsoleFormToast(
                "Book adding",
                questions,
                "/stop",
                "Lets create book! You can stop everywhere by entering '/stop'"
        ).draw().getAnswers();
        refresh();
        return answersOptional.map(Books::ofMapping);
    }

    @Override
    public Optional<Client> initClientToAdd() {
        Map<String, String> questions = new LinkedHashMap<>();
        questions.put("Enter client name", "");
        Optional<List<String>> answersOptional = new ConsoleFormToast(
                "Client adding",
                questions,
                "/stop",
                "Lets add client! You can stop everywhere by entering '/stop'"
        ).draw().getAnswers();
        refresh();
        return answersOptional.map(Clients::ofMapping);
    }

    @Override
    public Optional<Book> editBook(Book originalBook) {
        Map<String, String> messages = new LinkedHashMap<>();
        messages.put("Enter title", "prev book title: '" + originalBook.getTitle() + "'");
        messages.put("Enter subtitle", "prev book subtitle: '" + originalBook.getSubtitle() + "'");
        messages.put("Enter author", "prev book author: '" + originalBook.getAuthor() + "'");
        messages.put("Enter publish date in format 2018-12-31", "prev book publish date: '" + originalBook.getPublishedDate() + "'");
        messages.put("Enter book publisher", "prev book publisher: '" + originalBook.getPublisher() + "'");
        Optional<List<String>> answersOptional = new ConsoleFormToast(
                "Book editing",
                messages,
                "/stop",
                "Lets edit book! You can stop by entering '/stop'",
                "If you wanna edit field, enter data.\nIf you wanna skip editing, click Enter."
        ).draw().getAnswers();
        refresh();
        return answersOptional.map(answers -> {
            Book editedBook = Books.copyOf(originalBook);
            return Books.changeByMapping(editedBook, answers);
        });
    }

    @Override
    public Optional<Client> editClient(final Client originalClient) {
        Map<String, String> messages = new LinkedHashMap<>();
        messages.put("Enter name", "prev client name: '" + originalClient.getName() + "'");
        Optional<List<String>> answersOptional = new ConsoleFormToast(
                "Client editing",
                messages,
                "/stop",
                "Lets edit client! You can stop by entering '/stop'",
                "If you wanna edit field, enter data.\nIf you wanna skip editing, click Enter."
        ).draw().getAnswers();
        refresh();
        return answersOptional.map(answers -> {
            Client editedClient = Clients.copyOf(originalClient);
            return Clients.changeByMapping(editedClient, answers);
        });
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
        tabStack.push(new ConsoleTab(
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
        tabStack.push(new ConsoleTab(
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
        tabStack.push(new ConsoleTab(
                () -> operationOptionalSupplier.get().ifPresentOrElse(
                        dataPrinter::printOperationDetails,
                        this::drawPrevTab
                ),
                LOOK_HISTORY,
                "OPERATION DETAILS"
        )).draw();
    }

    @Override
    public WindowManager refresh() {
        dataPrinter.clear();
        this.tabStack.peek().draw();
        return this;
    }
}
