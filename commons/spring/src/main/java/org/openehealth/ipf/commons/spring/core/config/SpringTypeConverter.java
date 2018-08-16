/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.spring.core.config;

import org.openehealth.ipf.commons.core.config.TypeConverter;
import org.springframework.core.convert.ConversionService;

/**
 * {@link TypeConverter} implementation baacked by a Spring {@link ConversionService}
 *
 * @since 3.1
 */
public class SpringTypeConverter implements TypeConverter {

    private final ConversionService conversionService;

    public SpringTypeConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return conversionService != null && conversionService.canConvert(sourceType, targetType);
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }
}
