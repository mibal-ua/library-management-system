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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.Library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class JsonDataOperator implements DataOperator {

    private final String sourcePath;

    public JsonDataOperator(@Value("${dataOperator.jsonPath}") final String path) {
        this.sourcePath = path;
    }

    @Override
    public Library getLibrary() {
        final String data = readFile();
        return stringToLibrary(data);
    }

    @Override
    public void updateLibrary(final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(new File(sourcePath), library);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile() {
        File source = new File(sourcePath);
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner reader = new Scanner(source)) {
            while (reader.hasNextLine()) {
                stringBuilder.append(reader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    protected Library stringToLibrary(final String data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Library.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
