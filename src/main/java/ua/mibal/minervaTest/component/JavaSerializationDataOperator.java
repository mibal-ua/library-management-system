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

import org.springframework.beans.factory.annotation.Value;
import ua.mibal.minervaTest.model.Library;

import java.io.*;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
// @Component
public class JavaSerializationDataOperator implements DataOperator {

    private final String path;

    public JavaSerializationDataOperator(@Value("${dataOperator.javaSerPath}") final String path) {
        this.path = path;
    }

    @Override
    public Library getLibrary() {
        Library library;
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(path);
            in = new ObjectInputStream(file);
            library = (Library) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return library;
    }

    @Override
    public void updateLibrary(final Library library) {
        FileOutputStream file = null;
        ObjectOutputStream out = null;
        try {
            file = new FileOutputStream(path);
            out = new ObjectOutputStream(file);
            out.writeObject(library);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
