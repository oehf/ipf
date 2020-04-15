/*
 * Copyright 2012 the original author or authors.
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.requireNonNull;


/**
 * Base class for interceptors which store incoming and outgoing payload
 * into files with user-defined name patterns, or to the regular Java log.
 * <p>
 * File name patterns can contain absolute and relative paths and must correspond to the
 * <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/expressions.html">SpEL</a>
 * syntax, using square brackets for referencing placeholder parameters.
 * In the base version, the following parameters are supported
 * (this set can be extended in derived classes):
 * <ul>
 * <li><tt>sequenceId</tt>&nbsp;&mdash; internally generated sequential ID
 * as a 12-digit positive long int, zero-padded.</li>
 * <li><tt>processId</tt>&nbsp;&mdash; process ID consisting from the OS process
 * number and the host name, e.g. <tt>"12345-myhostname"</tt>.</li>
 * <li><tt>date('format_spec')</tt>&nbsp;&mdash; current date and time, formatted
 * using {@link java.text.SimpleDateFormat} according to the given specification.</li>
 * </ul>
 * <br>
 * Example of a file name pattern:<br>
 * <tt>C:/IPF-LOGS/[processId]/[date('yyyyMMdd-HH00')]/[sequenceId]-server-output.txt</tt>
 * <br>
 * After a pre-configured count of failed trials to create a file, the logger will be switched off.
 * <p>
 * As an alternative to SpEL, the user can provide another {@link ExpressionResolver expression resolver}.
 * <p>
 * Furthermore, the behavior of this class is regulated application-widely by the following Boolean
 * system properties:
 * <ul>
 * <li><tt>org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase.CONSOLE</tt>&nbsp;&mdash;
 * when set to <code>true</code>, then the message payload will be logged using regular
 * Java logging mechanisms  (level DEBUG) instead of being written into files whose names
 * are created from the pattern.</li>
 * <li><tt>org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase.DISABLED</tt>&nbsp;&mdash;
 * when set to <code>true</code>, then no logging will be performed at all.</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
abstract public class PayloadLoggerBase<T extends PayloadLoggingContext> {
    private static final transient Logger LOG = LoggerFactory.getLogger(PayloadLoggerBase.class);

    private static final AtomicLong SEQUENCE_ID_GENERATOR = new AtomicLong(0L);

    // CXF message property
    public static final String SEQUENCE_ID_PROPERTY_NAME =
            PayloadLoggerBase.class.getName() + ".sequence.id";

    // Java system properties
    public static final String PROPERTY_CONSOLE = PayloadLoggerBase.class.getName() + ".CONSOLE";
    public static final String PROPERTY_DISABLED = PayloadLoggerBase.class.getName() + ".DISABLED";

    private boolean enabled = true;

    private int errorCountLimit = -1;
    private final AtomicInteger errorCount = new AtomicInteger(0);

    private ExpressionResolver resolver;

    protected static Long getNextSequenceId() {
        return SEQUENCE_ID_GENERATOR.getAndIncrement();
    }

    protected void doLogPayload(T context, String charsetName, String... payloadPieces) {
        // check whether we can process
        if (!canProcess()) {
            return;
        }
        if ((errorCountLimit >= 0) && (errorCount.get() >= errorCountLimit)) {
            LOG.warn("Error count limit has bean reached, reset the counter to enable further trials");
            return;
        }

        if (Boolean.getBoolean(PROPERTY_CONSOLE)) {
            // use regular Java logging
            if (LOG.isDebugEnabled()) {
                String output = String.join("", payloadPieces);
                LOG.debug(output);
            }
        } else {
            // compute the file path and write payload pieces into this file
            String path = resolver.resolveExpression(context);
            Writer writer = null;
            try {
                FileOutputStream outputStream = FileUtils.openOutputStream(new File(path), true);
                writer = (charsetName != null) ?
                        new OutputStreamWriter(outputStream, charsetName) :
                        new OutputStreamWriter(outputStream);
                for (String payloadPiece : payloadPieces) {
                    writer.write(payloadPiece);
                }
                errorCount.set(0);
            } catch (IOException e) {
                errorCount.incrementAndGet();
                LOG.warn("Cannot write into " + path, e);
            } finally {
                IOUtils.closeQuietly(writer);
            }
        }
    }


    public boolean canProcess() {
        if ((!enabled) || Boolean.getBoolean(PROPERTY_DISABLED)) {
            LOG.trace("Message payload logging is disabled");
            return false;
        }
        return true;
    }


    /**
     * Resets count of occurred errors, can be used e.g. via JMX.
     */
    public void resetErrorCount() {
        errorCount.set(0);
    }

    /**
     * @return <code>true</code> if this logging interceptor instance is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled <code>true</code> when this logging interceptor instance should be enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return <code>true</code> if this logging interceptor instance is enabled.
     * @deprecated use {@link #isEnabled()}
     */
    @Deprecated
    public boolean isLocallyEnabled() {
        return isEnabled();
    }

    /**
     * @param locallyEnabled <code>true</code> when this logging interceptor instance should be enabled.
     * @deprecated use {@link #setEnabled(boolean)}
     */
    @Deprecated
    public void setLocallyEnabled(boolean locallyEnabled) {
        setEnabled(locallyEnabled);
    }

    /**
     * @return <code>true</code> when logging interceptors are generally enabled.
     * @see #isEnabled()
     * @deprecated use environment variable {@link #PROPERTY_DISABLED}
     */
    public static boolean isGloballyEnabled() {
        return !Boolean.getBoolean(PROPERTY_DISABLED);
    }

    /**
     * @param globallyEnabled <code>true</code> when logging interceptors shall be generally enabled.
     * @see #setLocallyEnabled(boolean)
     * @deprecated use environment variable {@link #PROPERTY_DISABLED}
     */
    public static void setGloballyEnabled(boolean globallyEnabled) {
        System.setProperty(PROPERTY_DISABLED, Boolean.toString(!globallyEnabled));
    }

    /**
     * @return maximal allowed count of file creation errors,
     * negative value (the default) means "no limit".
     */
    public int getErrorCountLimit() {
        return errorCountLimit;
    }

    /**
     * Configures maximal allowed count of file creation errors.
     *
     * @param errorCountLimit maximal allowed count of file creation errors,
     *                        negative value (the default) means "no limit".
     */
    public void setErrorCountLimit(int errorCountLimit) {
        this.errorCountLimit = errorCountLimit;
    }

    public ExpressionResolver getExpressionResolver() {
        return resolver;
    }

    public void setExpressionResolver(ExpressionResolver resolver) {
        this.resolver = requireNonNull(resolver);
    }
}
