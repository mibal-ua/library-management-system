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

package ua.mibal.minervaTest.dao;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationDao extends Dao<Operation> {

    public OperationDao(QueryHelper queryHelper) {
        super(queryHelper, Operation.class);
    }

    @Override
    protected boolean appropriateSelectingAddingLogic(Operation operation, String arg, List<Operation> result) {
        if (operation.getId().equals(Long.valueOf(arg))) {
            result.add(operation);
            return false;
        }
        if (operation.getClient().getId().toString().equals(arg) ||
            operation.getDate().toString().contains(arg) ||
            operation.getOperationType().toString().equalsIgnoreCase(arg) ||
            operation.getClient().getName().contains(arg)) {
            result.add(operation);
        }
        return true;
    }
}
