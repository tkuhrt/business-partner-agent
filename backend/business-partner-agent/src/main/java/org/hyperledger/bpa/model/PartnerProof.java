/*
  Copyright (c) 2020 - for information on the respective copyright owner
  see the NOTICE file and/or the repository at
  https://github.com/hyperledger-labs/business-partner-agent

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.hyperledger.bpa.model;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Aries proof that I received from a partner (aka connection).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Accessors(chain = true)
public class PartnerProof {

    @Id
    @AutoPopulated
    private UUID id;

    private UUID partnerId;

    @DateCreated
    private Instant createdAt;

    @Nullable
    private Instant issuedAt;

    @Nullable
    private Boolean valid;

    @Nullable
    private String state;

    private String presentationExchangeId;

    @Nullable
    private String issuer;

    @Nullable
    private String schemaId;

    @Nullable
    private String credentialDefinitionId;

    @Nullable
    private String role;

    @Nullable
    @TypeDef(type = DataType.JSON)
    private Map<String, Object> proof;
}
