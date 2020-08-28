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
package org.hyperledger.oa.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import org.hyperledger.aries.api.jsonld.VerifiablePresentation;
import org.hyperledger.oa.api.CredentialType;
import org.hyperledger.oa.api.MyDocumentAPI;
import org.hyperledger.oa.api.PartnerAPI;
import org.hyperledger.oa.api.PartnerAPI.PartnerCredential;
import org.hyperledger.oa.model.MyDocument;
import org.hyperledger.oa.model.Partner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.core.util.CollectionUtils;
import lombok.NonNull;
import lombok.Setter;

@Singleton
public class Converter {

    public static final TypeReference<Map<String, Object>> MAP_TYPEREF = new TypeReference<>() {
    };

    @Inject
    @Setter
    private ObjectMapper mapper;

    public PartnerAPI toAPIObject(@NonNull Partner partner) {
        PartnerAPI result;
        if (partner.getVerifiablePresentation() == null) {
            result = new PartnerAPI();
        } else {
            result = toAPIObject(fromMap(partner.getVerifiablePresentation(), VerifiablePresentation.class));
        }
        result
                .setCreatedAt(Long.valueOf(partner.getCreatedAt().toEpochMilli()))
                .setUpdatedAt(Long.valueOf(partner.getUpdatedAt().toEpochMilli()))
                .setId(partner.getId().toString())
                .setValid(partner.getValid())
                .setAriesSupport(partner.getAriesSupport())
                .setState(partner.getState())
                .setAlias(partner.getAlias())
                .setIncoming(partner.getIncoming() != null ? partner.getIncoming() : Boolean.FALSE);

        return result;
    }

    public PartnerAPI toAPIObject(@NonNull VerifiablePresentation partner) {
        List<PartnerCredential> pc = new ArrayList<>();
        if (partner.getVerifiableCredential() != null) {
            partner.getVerifiableCredential().forEach(c -> {
                JsonNode node = mapper.convertValue(c.getCredentialSubject(), JsonNode.class);
                boolean indyCredential = false;
                if (CollectionUtils.isNotEmpty(c.getType())) {
                    indyCredential = c.getType().stream().anyMatch(t -> "IndyCredential".equals(t));
                }
                final PartnerCredential pCred = PartnerCredential
                        .builder()
                        .type(CredentialType.fromType(c.getType()))
                        .issuer(indyCredential ? c.getIndyIssuer() : c.getIssuer())
                        .schemaId(c.getSchemaId())
                        .credentialData(node)
                        .indyCredential(Boolean.valueOf(indyCredential))
                        .build();
                pc.add(pCred);
            });
        }
        return PartnerAPI.builder()
                .verifiablePresentation(partner)
                .credential(pc).build();
    }

    public Partner toModelObject(String did, PartnerAPI api) {
        return Partner
                .builder()
                .did(did)
                .valid(api.getValid())
                .verifiablePresentation(
                        api.getVerifiablePresentation() != null ? toMap(api.getVerifiablePresentation()) : null)
                .build();
    }

    /**
     * Converts the given credential to related model object.
     *
     * @param document credential payload
     * @return myCredential model object
     */
    public MyDocument toModelObject(@NonNull MyDocumentAPI document) {
        return updateMyCredential(document, new MyDocument());
    }

    /**
     * Updates the given myCredential with the data from the given credential
     *
     * @param apiDoc credential payload
     * @param myDoc  model object
     * @return myCredential model object, updated
     */
    public MyDocument updateMyCredential(@NonNull MyDocumentAPI apiDoc, @NonNull MyDocument myDoc) {
        Map<String, Object> data = toMap(apiDoc.getDocumentData());
        myDoc.setDocument(data);
        myDoc.setIsPublic(apiDoc.getIsPublic());
        myDoc.setType(apiDoc.getType());

        return myDoc;
    }

    public MyDocumentAPI toApiObject(@NonNull MyDocument myDoc) {
        return MyDocumentAPI.builder()
                .id(myDoc.getId())
                .createdDate(Long.valueOf(myDoc.getCreatedAt().toEpochMilli()))
                .updatedDate(Long.valueOf(myDoc.getUpdatedAt().toEpochMilli()))
                .documentData(fromMap(myDoc.getDocument(), JsonNode.class))
                .isPublic(myDoc.getIsPublic())
                .type(myDoc.getType())
                .build();
    }

    public Map<String, Object> toMap(@NonNull Object fromValue) {
        return mapper.convertValue(fromValue, MAP_TYPEREF);
    }

    public <T> T fromMap(@NonNull Map<String, Object> fromValue, @NotNull Class<T> type) {
        return mapper.convertValue(fromValue, type);
    }

    public <T> T fromMapString(@NonNull Map<String, String> fromValue, @NotNull Class<T> type) {
        return mapper.convertValue(fromValue, type);
    }

}