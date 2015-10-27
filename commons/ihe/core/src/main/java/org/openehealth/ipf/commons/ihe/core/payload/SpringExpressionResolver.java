package org.openehealth.ipf.commons.ihe.core.payload;

import org.apache.commons.lang3.Validate;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * {@link ExpressionResolver} implementation using Spring Expression language. You can also implement your
 * own resolver that is able to evaluate file path patterns
 *
 * @since 3.1
 */
public class SpringExpressionResolver implements ExpressionResolver {

    private final Expression expression;

    public SpringExpressionResolver(final String filePathPattern) {
        Validate.notEmpty(filePathPattern, "log file path/name pattern");
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
