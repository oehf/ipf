package org.openehealth.ipf.commons.ihe.core.payload;

/**
 * Interface for resolving expressions generating file paths
 * @since 3.1
 */
public interface ExpressionResolver {

    /**
     * Resolves the {@link PayloadLoggingContext} to obtain a path
     * for payload logging
     *
     * @param context
     * @return a path for payload logging
     */
    String resolveExpression(PayloadLoggingContext context);

}
