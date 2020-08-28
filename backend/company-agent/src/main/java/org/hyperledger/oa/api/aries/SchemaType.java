/**
 * Copyright (c) 2020 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/organizational-agent
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
package org.hyperledger.oa.api.aries;

import java.util.Optional;

public enum SchemaType {

    BANK_ACCOUNT("M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0");

    private String schemaId;

    SchemaType(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public static Optional<SchemaType> fromString(String id) {
        SchemaType result = null;
        if (BANK_ACCOUNT.getSchemaId().equals(id)) {
            result = BANK_ACCOUNT;
        }
        return Optional.ofNullable(result);
    }
}