/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Base class for interceptors which store incoming and outgoing CXF/HTTP payload
 * into files with user-defined name patterns.
 * <p>
 * File name patterns can contain absolute and relative paths and must correspond to
 * <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/expressions.html">SpEL</a>
 * syntax, using square brackets for referencing placeholder parameters.
 * Supported are the following parameters:
 * <ul>
 *     <li><tt>sequenceId</tt>&nbsp;&mdash; internally generated sequential ID
 *          as a 12-digit positive long int, zero-padded.</li>
 *     <li><tt>processId</tt>&nbsp;&mdash; process ID consisting from the OS process
 *          number and the host name, e.g. <tt>"12345-myhostname"</tt>.</li>
 *     <li><tt>date('format_spec')</tt>&nbsp;&mdash; current date and time, formatted
 *          using {@link SimpleDateFormat} according to the given specification.</li>
 *     <li><tt>partialResponse</tt>&nbsp;&mdash; returns <code>true</code>,
 *          when the message under consideration is a WS-Addressing partial response.
 * </ul>
 * <br>
 * Example of a file name pattern:<br>
 * <tt>C:/IPF-LOGS/[processId]/[date('yyyyMMdd-HH00')]/[sequenceId]-server-output.txt</tt>
 *
 * @author Dmytro Rud
 */
abstract public class PayloadLogInterceptorBase extends AbstractSafeInterceptor {
    private static final transient Log LOG = LogFactory.getLog(PayloadLogInterceptorBase.class);

    private static final AtomicLong SEQUENCE_ID_GENERATOR = new AtomicLong(0L);
    private static final String SEQUENCE_ID_KEY =
            PayloadLogInterceptorBase.class.getName() + ".sequence.id";


    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();
    private static final TemplateParserContext SPEL_PARSER_CONTEXT =
            new TemplateParserContext("[", "]");

    ThreadLocal<Expression> SPEL_EXPRESSIONS = new ThreadLocal<Expression>() {
        @Override
        protected Expression initialValue() {
            return SPEL_PARSER.parseExpression(getFileNamePattern(), SPEL_PARSER_CONTEXT);
        }
    };


    private static boolean globallyEnabled = true;

    private String fileNamePattern;
    private boolean locallyEnabled;

    private int errorCountLimit = -1;
    private int errorCount;


    /**
     * Constructor.
     * @param phase
     *      CXF interceptor phase.
     * @param fileNamePattern
     *      SPEL-based file name pattern of each file to be created.
     */
    protected PayloadLogInterceptorBase(String phase, String fileNamePattern) {
        super(phase);
        setFileNamePattern(fileNamePattern);
        setLocallyEnabled(true);
    }


    /**
     * Returns message body payload ("plain" SOAP or a MIME structure) as a String.
     * @param message
     *      CXF message whose payload is of interest.
     * @return
     *      body as a String.
     */
    abstract protected String getBodyPayload(SoapMessage message);


    /**
     * Returns HTTP headers payload as a String.
     * @param message
     *      CXF message whose headers are of interest.
     * @return
     *      headers as a String.
     */
    abstract protected String getHeadersPayload(SoapMessage message);


    @Override
    protected void process(SoapMessage message) {
        // check whether we can process
        if (! (globallyEnabled && locallyEnabled)) {
            LOG.debug("File-based logging is disabled");
            return;
        }
        if ((errorCountLimit >= 0) && (errorCount >= errorCountLimit)) {
            LOG.warn("Error count limit has bean reached, reset the counter to enable further trials");
            return;
        }

        // determine sequence ID
        Long sequenceId = findContextualProperty(message, SEQUENCE_ID_KEY);
        if (sequenceId == null) {
            sequenceId = SEQUENCE_ID_GENERATOR.getAndIncrement();
            message.setContextualProperty(SEQUENCE_ID_KEY, sequenceId);
        }

        // parse file name pattern and resolve references
        Parameters parameters = new Parameters(
                String.format("%012d", sequenceId),
                Boolean.TRUE.equals(message.get(Message.PARTIAL_RESPONSE_MESSAGE)));
        String path = SPEL_EXPRESSIONS.get().getValue(parameters, String.class);

        // write payload pieces into the file
        Writer writer = null;
        try {
            FileOutputStream outputStream = FileUtils.openOutputStream(new File(path), true);
            String charsetName = (String) message.get(Message.ENCODING);
            writer = (charsetName != null) ?
                    new OutputStreamWriter(outputStream, charsetName) :
                    new OutputStreamWriter(outputStream);
            writer.write(getHeadersPayload(message));
            writer.write(getBodyPayload(message));
            errorCount = 0;
        } catch (IOException e) {
            ++errorCount;
            LOG.warn("Cannot write into " + path, e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }


    /**
     * Appends generic HTTP headers to the given String builder.
     * @param message
     *      CXF message which contains the headers.
     * @param sb
     *      target String builder instance.
     */
    protected static void appendGenericHttpHeaders(SoapMessage message, StringBuilder sb) {
        Object encoding = message.get(Message.ENCODING);
        if (encoding != null) {
            sb.append("Character set: ").append(encoding).append('\n');
        }
        sb.append('\n');

        Map<String, List<String>> httpHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (httpHeaders != null) {
            for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
                for (String header : entry.getValue()) {
                    sb.append(entry.getKey()).append(": ").append(header).append('\n');
                }
            }
            sb.append('\n');
        }
    }


    /**
     * Resets count of occurred errors, can be used e.g. via JMX.
     */
    public void resetErrorCount() {
        errorCount = 0;
    }


    /**
     * @return <code>true</code> if this logging interceptor is enabled.
     * @see #isGloballyEnabled()
     */
    public boolean isLocallyEnabled() {
        return locallyEnabled;
    }

    /**
     * @param locallyEnabled
     *          <code>true</code> when this logging interceptor instance should be enabled.
     * @see #setGloballyEnabled(boolean)
     */
    public void setLocallyEnabled(boolean locallyEnabled) {
        this.locallyEnabled = locallyEnabled;
    }

    /**
     * @return <code>true</code> when logging interceptors are generally enabled.
     * @see #isLocallyEnabled()
     */
    public static boolean isGloballyEnabled() {
        return globallyEnabled;
    }

    /**
     * @param globallyEnabled
     *          <code>true</code> when logging interceptor should be generally enabled.
     * @see #setLocallyEnabled(boolean)
     */
    public static void setGloballyEnabled(boolean globallyEnabled) {
        PayloadLogInterceptorBase.globallyEnabled = globallyEnabled;
    }

    /**
     * @return
     *      file name pattern for payload logs.
     */
    public String getFileNamePattern() {
        return fileNamePattern;
    }

    /**
     * Sets the file name pattern for payload logs.
     * @param fileNamePattern
     *      file name pattern for payload logs.
     */
    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = Validate.notEmpty(fileNamePattern, "log file path/name pattern");
    }

    /**
     * @return maximal allowed count of file creation errors, negative value means "no limit".
     */
    public int getErrorCountLimit() {
        return errorCountLimit;
    }

    /**
     * Configures maximal allowed count of file creation errors.
     * @param errorCountLimit
     *      maximal allowed count of file creation errors, negative value means "no limit".
     */
    public void setErrorCountLimit(int errorCountLimit) {
        this.errorCountLimit = errorCountLimit;
    }



    /**
     * The "root object" for the resolution of file name patterns.
     */
    private static class Parameters {
        private static final String PROCESS_ID;
        private final Date currentDate;
        private final String sequenceId;
        private final boolean partialResponse;

        static {
            RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
            PROCESS_ID = mx.getName().replace("@", "-");
        }

        private Parameters(String sequenceId, boolean partialResponse) {
            this.sequenceId = sequenceId;
            this.partialResponse = partialResponse;
            this.currentDate = new Date();
        }

        public String getProcessId() {
            return PROCESS_ID;
        }

        public String getSequenceId() {
            return sequenceId;
        }

        public boolean isPartialResponse() {
            return partialResponse;
        }

        public String date(String formatSpecification) {
            return new SimpleDateFormat(formatSpecification).format(currentDate);
        }
    }
}
