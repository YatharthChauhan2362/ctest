/*
 * SonarQube Cryptography Plugin
 * Copyright (C) 2024 IBM
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.plugin.rules.detection.bc.other;

import com.ibm.engine.model.context.CipherContext;
import com.ibm.engine.model.factory.ValueActionFactory;
import com.ibm.engine.rule.IDetectionRule;
import com.ibm.engine.rule.builder.DetectionRuleBuilder;
import com.ibm.plugin.rules.detection.bc.digest.BcDigests;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.sonar.plugins.java.api.tree.Tree;

public final class BcSM2Engine {

    private BcSM2Engine() {
        // nothing
    }

    private static final String ENGINE_NAME = "SM2Engine";
    private static final String ENGINE_TYPE = "org.bouncycastle.crypto.engines.SM2Engine";

    private static final IDetectionRule<Tree> CONSTRUCTOR_1 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes(ENGINE_TYPE)
                    .forConstructor()
                    .shouldBeDetectedAs(new ValueActionFactory<>(ENGINE_NAME))
                    .withoutParameters()
                    .buildForContext(new CipherContext(Map.of("kind", "ASYMMETRIC_CIPHER_ENGINE")))
                    .inBundle(() -> "Bc")
                    .withDependingDetectionRules(BcSM2EngineInit.rules());

    private static final IDetectionRule<Tree> CONSTRUCTOR_2 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes(ENGINE_TYPE)
                    .forConstructor()
                    .shouldBeDetectedAs(new ValueActionFactory<>(ENGINE_NAME))
                    .withMethodParameter("org.bouncycastle.crypto.Digest")
                    .addDependingDetectionRules(BcDigests.rules())
                    .buildForContext(new CipherContext(Map.of("kind", "ASYMMETRIC_CIPHER_ENGINE")))
                    .inBundle(() -> "Bc")
                    .withDependingDetectionRules(BcSM2EngineInit.rules());

    private static final IDetectionRule<Tree> CONSTRUCTOR_3 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes(ENGINE_TYPE)
                    .forConstructor()
                    .shouldBeDetectedAs(new ValueActionFactory<>(ENGINE_NAME))
                    .withMethodParameter("org.bouncycastle.crypto.Digest")
                    .addDependingDetectionRules(BcDigests.rules())
                    .withMethodParameter("org.bouncycastle.crypto.engines.SM2Engine$Mode")
                    .buildForContext(new CipherContext(Map.of("kind", "ASYMMETRIC_CIPHER_ENGINE")))
                    .inBundle(() -> "Bc")
                    .withDependingDetectionRules(BcSM2EngineInit.rules());

    private static final IDetectionRule<Tree> CONSTRUCTOR_4 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes(ENGINE_TYPE)
                    .forConstructor()
                    .shouldBeDetectedAs(new ValueActionFactory<>(ENGINE_NAME))
                    .withMethodParameter("org.bouncycastle.crypto.engines.SM2Engine$Mode")
                    .buildForContext(new CipherContext(Map.of("kind", "ASYMMETRIC_CIPHER_ENGINE")))
                    .inBundle(() -> "Bc")
                    .withDependingDetectionRules(BcSM2EngineInit.rules());

    @Nonnull
    public static List<IDetectionRule<Tree>> rules() {
        return List.of(CONSTRUCTOR_1, CONSTRUCTOR_2, CONSTRUCTOR_3, CONSTRUCTOR_4);
    }
}