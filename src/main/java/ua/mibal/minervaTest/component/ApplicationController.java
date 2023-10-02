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

package ua.mibal.minervaTest.component;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.dao.book.BookRepository;
import ua.mibal.minervaTest.dao.client.ClientRepository;
import ua.mibal.minervaTest.dao.operation.OperationRepository;
import ua.mibal.minervaTest.gui.WindowManager;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.service.Service;

import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.DataType.NULL;
import static ua.mibal.minervaTest.utils.StringUtils.isNumber;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ApplicationController {

    private final WindowManager windowManager;

    private final BookRepository bookRepository;
    private final OperationRepository operationRepository;
    private final ClientRepository clientRepository;

    public ApplicationController(WindowManager windowManager,
                                 BookRepository bookRepository,
                                 OperationRepository operationRepository,
                                 ClientRepository clientRepository) {
        this.windowManager = windowManager;
        this.bookRepository = bookRepository;
        this.operationRepository = operationRepository;
        this.clientRepository = clientRepository;
    }

    public void tab1(final String[] ignored) {
        windowManager.tab1(bookRepository::findAll);
    }

    public void tab2(final String[] ignored) {
        windowManager.tab2(clientRepository::findAllFetchBooks);
    }

    public void tab3(final String[] ignored) {
        windowManager.tab3(operationRepository::findAllFetchBookClient);
    }

    public void esc(final String[] ignored) {
        windowManager.drawPrevTab();
    }

    public void help(final String[] ignored) {
        windowManager.help();
    }

    public void search(final String[] args) {
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'search' with ${query}");
            return;
        }
        DataType dataType = windowManager.getCurrentDataType();
        if (dataType == NULL) {
            windowManager.showToast("You can not use command 'search' in this tab.");
            return;
        }

        Service<? extends Entity> service = Service.getInstance(dataType);
        windowManager.searchTab(() -> service.searchBy(args), args);
    }

    public void look(final String[] args) {
        if (args.length == 0 || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'look' with ${id}");
            return;
        }
        DataType dataType = windowManager.getCurrentDataType();
        if (dataType == NULL) {
            windowManager.showToast("You can not use command 'look' in this tab.");
            return;
        }

        final Long id = Long.valueOf(args[0]);
        Service<? extends Entity> service = Service.getInstance(dataType);
        windowManager.detailsTab(() -> service.details(id));
    }

    public void edit(final String[] args) {
        DataType dataType = windowManager.getCurrentDataType();
        if (dataType == HISTORY || dataType == NULL) {
            windowManager.showToast("You can not use command 'edit' in this tab.");
            return;
        }
        final boolean isDetailsTab = windowManager.isDetailsTab();
        if (!isDetailsTab && (args.length == 0 || !isNumber(args[0]))) {
            windowManager.showToast("You need to enter 'edit' with ${id}");
            return;
        }

        final Long id = isDetailsTab
                ? windowManager.getCurrentEntityId()
                : Long.valueOf(args[0]);
        Service<? extends Entity> service = Service.getInstance(dataType);
        service.findById(id).ifPresentOrElse(
                e -> windowManager.editEntity(e).ifPresent(editedE -> {
                    service.update(editedE);
                    windowManager.showToast(dataType.simpleName() + " successfully updated!");
                }),
                () -> windowManager.showToast("Oops, there are no " + dataType.simplePluralName() + " with this id=" + id)
        );
    }

    public void add(final String[] ignored) {
        DataType dataType = windowManager.getCurrentDataType();
        if (dataType == HISTORY || dataType == NULL) {
            windowManager.showToast("You can not use command 'add' in this tab.");
            return;
        }

        Service<? extends Entity> service = Service.getInstance(dataType);
        windowManager.initEntityToAdd(dataType.getEntityClass()).ifPresent(e -> {
            service.save(e);
            windowManager.showToast(dataType.simpleName() + " successfully added!");
        });
    }

    public void delete(final String[] args) {
        DataType dataType = windowManager.getCurrentDataType();
        if (dataType == HISTORY || dataType == NULL) {
            windowManager.showToast("You can not use command 'delete' in this tab.");
            return;
        }
        final boolean isDetailsTab = windowManager.isDetailsTab();
        if (!isDetailsTab && (args.length == 0 || !isNumber(args[0]))) {
            windowManager.showToast("You need to enter 'delete' with ${id}");
            return;
        }

        final Long id = isDetailsTab
                ? windowManager.getCurrentEntityId()
                : Long.valueOf(args[0]);
        Service<? extends Entity> service = Service.getInstance(dataType);
        service.findById(id).ifPresentOrElse(
                entityToDel -> {
                    if (!entityToDel.isReadyToDelete()) {
                        String reason = entityToDel.getNotDeleteReason();
                        windowManager.showToast(dataType.simpleName() + reason,
                                "Oops, but " + dataType.simpleName() + " '" +
                                entityToDel.getName() + "' " + reason);
                    } else if (windowManager.showDialogueToast("You really need to delete " +
                                                               dataType.simpleName() + " '" +
                                                               entityToDel.getName() + "'?",
                            "YES", "NO")) {
                        service.delete(entityToDel);
                        windowManager.showToast(dataType.simpleName() + " successfully deleted.");
                    }
                },
                () -> windowManager.showToast("Oops, there are no " + dataType.simplePluralName() + " with this id=" + id)
        );
    }


    public void unrecognizable(String[] ignored) {
        windowManager.showToast("Unrecognizable command");
    }

    @FunctionalInterface
    public interface ArrayConsumer<T> {

        void apply(T... args);
    }
}
