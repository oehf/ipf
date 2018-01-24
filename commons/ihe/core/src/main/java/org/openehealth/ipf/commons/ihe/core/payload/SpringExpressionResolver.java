/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.payload;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static java.util.Objects.requireNonNull;

/**
 * {@link ExpressionResolver} implementation using Spring Expression language. You can also implement your
 * own resolver that is able to evaluate file path patterns
 *
 * @since 3.1
 */
public class SpringExpressionResolver implements ExpressionResolver {

    private final Expression expression;

    public SpringExpressionResolver(final String filePathPattern) {
        requireNonNull(filePathPattern, "log file path/name pattern must not be null");
        final ExpressionParser parser = new SpelExpressionParser();
        final TemplateParserContext parserContext = new TemplateParserContext("[", "]");
        parser.parseExpression(filePathPattern, parserContext);
        expression = parser.parseExpression(filePathPattern, parserContext);
    }

    @Override
    public String resolveExpression(PayloadLoggingContext context) {
        return expression.getValue(context, String.class);
    }

}
