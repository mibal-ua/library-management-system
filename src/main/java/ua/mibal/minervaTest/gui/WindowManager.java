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

import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface WindowManager {

    <T extends Entity> void listTab(Supplier<List<T>> listSupplier, DataType dataType);

    <T extends Entity> void searchTab(Supplier<List<T>> o, String[] args);

    <T extends Entity> void detailsTab(Supplier<Optional<T>> o);

    <T extends Entity> Optional<T> editEntity(T e, DataType dataType);

    <T extends Entity> Optional<T> initEntityToAdd(DataType dataType);

    void help();

    void showToast(final String message);

    void showToast(String header, String message);

    boolean showDialogueToast(String question, String answer1, String answer2);

    String[] readCommandLine();

    DataType getCurrentDataType();

    void drawPrevTab();

    Long getCurrentEntityId();

    boolean isDetailsTab();
}

