/*
 * Copyright (c) 2022. http://t.me/mibal_ua
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
 *
 */

package ua.mibal.minervaTest.gui.console;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.gui.DataPrinter;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;
import ua.mibal.minervaTest.utils.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;
import static ua.mibal.minervaTest.model.window.DataType.BOOK;
import static ua.mibal.minervaTest.model.window.DataType.CLIENT;


/**
 * @author Michael Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ConsoleDataPrinter implements DataPrinter {

    @Override
    public <T extends Entity> void printEntityDetails(T entity, DataType dataType) {
        DataBundle<T> dataBundle = Entity.getConsoleDetailsBundle(dataType);

        if (dataBundle.getFields().isEmpty()) {
            throw new RuntimeException("Map with values is empty.");
        }

        int headerMaxLength = getKeyMaxLength(dataBundle.getHeaders());

        int valueMaxLength = (ConsoleConstants.WINDOW_WIDTH - headerMaxLength - 7);
        String template = "| %-" + headerMaxLength + "s | %-" + valueMaxLength + "s |%n";
        String divider = format("+-%s-+-%s-+%n", "-".repeat(headerMaxLength), "-".repeat(valueMaxLength));

        System.out.print(divider);

        List<String> headers = dataBundle.getHeaders();
        List<Function<T, Object>> fields = dataBundle.getFields();
        for (int i = 0; i < dataBundle.getHeaders().size(); i++) {
            String header = headers.get(i);
            String value = fields.get(i).apply(entity).toString();
            System.out.format(template, header, value);
            System.out.print(divider);
        }

        dataBundle.getFunction().ifPresent(fn -> {
            Set<? extends Entity> set = fn.apply(entity);
            if (dataType == CLIENT) {
                System.out.println("""
                                                          
                                                    Books that client holds
                        """);
                printListOfEntities(set, BOOK);
            }
        });
    }

    @Override
    public <T extends Entity> void printListOfEntities(Collection<T> data, DataType dataType) {
        if (data.isEmpty()) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }
        DataBundle<T> dataBundle = Entity.getConsoleListBundle(dataType);

        if (dataBundle.getSizes().isEmpty())
            throw new IllegalArgumentException("Sizes list is empty");

        StringBuilder dataTemplateBuilder = new StringBuilder("|");
        StringBuilder dividerBuilder = new StringBuilder("+");
        for (int i = 0; i < dataBundle.getSizes().size(); i++) {
            Integer size = dataBundle.getSizes().get(i);

            Function<Object, Object> trimStrAppendDotsFn = obj -> StringUtils.min(obj.toString(), size);
            dataBundle.getFields().set(i, trimStrAppendDotsFn.compose(dataBundle.getFields().get(i)));

            dataTemplateBuilder.append(" %-").append(size).append("s |");
            dividerBuilder.append("-").append("-".repeat(size)).append("-+");
        }

        String dataTemplate = dataTemplateBuilder.append("%n").toString();
        String divider = dividerBuilder.append("\n").toString();

        System.out.print(divider);
        System.out.printf(dataTemplate, dataBundle.getHeaders().toArray());
        System.out.print(divider);
        for (T el : data) {
            List<String> fields = dataBundle.getFields().stream()
                    .map(getter -> getter.apply(el).toString())
                    .toList();
            System.out.printf(dataTemplate, fields.toArray());
        }
        System.out.print(divider);
    }

    @Override
    public void clear() {
        ConsoleUtils.clear();
    }

    @Override
    public void printHelp() {
        System.out.println("""

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

                 """);
    }

    private int getKeyMaxLength(List<String> list) {
        return list.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }
}
