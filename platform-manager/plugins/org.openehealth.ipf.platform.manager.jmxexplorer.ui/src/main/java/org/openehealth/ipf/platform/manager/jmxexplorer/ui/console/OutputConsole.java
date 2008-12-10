/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.console;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Class which wraps the Eclipse console functionality. Every connection has its
 * own console instance.
 * 
 * @see ConsolePlugin
 * @see MessageConsole
 * 
 * @author Mitko Kolev
 */
public class OutputConsole {

    private static Log log = LogFactory.getLog(OutputConsole.class);

    private MessageConsole console;

    protected final static String MESAGE_DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";

    protected final static String NO_DATE_FORMAT = "NO_DATE_FORMAT";

    // private final IConnectionConfiguration connectionConfiguration;

    /**
     * Finds the console corresponding to this connection. If the console is not
     * available, creates it.
     * 
     * @param connectionConfiguration
     *            the connection for which the console should be returned
     */
    public OutputConsole(IConnectionConfiguration connectionConfiguration) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager consoleManager = plugin.getConsoleManager();
        IConsole[] existing = consoleManager.getConsoles();
        for (int i = 0; i < existing.length; i++)
            if (connectionConfiguration.getName().equals(existing[i].getName()))
                console = (MessageConsole) existing[i]; // no console found, so
        // create a new one
        if (console == null) {
            console = new MessageConsole(connectionConfiguration.getName(),
                    null);
            consoleManager.addConsoles(new IConsole[] { console });
        }
        // this.connectionConfiguration = connectionConfiguration;
        consoleManager.showConsoleView(console);
    }

    /**
     * Closes the console message stream after printing the java.lang.Trowable.
     * Appends new line at the end.
     * 
     * @param t
     *            the java.lang.Trowable to be printed
     */
    public void printOutputThrowable(Throwable t) {
        this.console.activate();
        MessageConsoleStream stream = null;
        PrintStream ps = null;
        try {
            stream = new MessageConsoleStream(console);
            StringBuffer header = addMessageHeaders(NO_DATE_FORMAT);
            // print header
            stream.write(header.toString());
            // print the Throwable
            ps = new PrintStream(stream);
            t.printStackTrace(ps);

            stream.write("\n");

        } catch (IOException e) {
            // log.error("Error writing to the console", e);
        } finally {
            closeConsoleStream(stream);
            if (ps != null) {
                ps.close();
            }

        }
    }

    /**
     * Prints a message string to this console. Closes the console message
     * stream after printing the message.Appends new line at the end. Prints the
     * given message in Bold.
     * 
     * @param message
     * @param bold
     *            true or false.
     */
    public void printMessage(String message, boolean bold) {
        this.console.activate();
        this.console.clearConsole();
        StringBuffer header = addMessageHeaders(MESAGE_DATE_FORMAT);
        MessageConsoleStream stream = null;
        try {
            stream = new MessageConsoleStream(console);
            if (bold == true)
                stream.setFontStyle(SWT.BOLD);
            // print the header
            stream.write(header.toString());
            // print the message
            stream.write(message);
            stream.write("\n");

        } catch (IOException e) {
            log.error("Error writing to the console", e);
        } finally {
            closeConsoleStream(stream);
        }
    }

    public void printOutputMessage(String message, boolean bold) {
        this.console.activate();
        StringBuffer header = addMessageHeaders(NO_DATE_FORMAT);
        MessageConsoleStream stream = null;
        try {
            stream = new MessageConsoleStream(console);
            if (bold == true)
                stream.setFontStyle(SWT.BOLD);
            // print the header
            stream.write(header.toString());
            // print the message
            stream.write(message);
            stream.write("\n");

        } catch (IOException e) {
            log.error("Error writing to the console", e);
        } finally {
            closeConsoleStream(stream);
        }
    }

    /**
     * Adds time and the name of the connection to every message which is
     * printed with the print message
     * 
     * @param connectionConfiguration
     *            the connection context of the console.
     * @param formatPattern
     *            the pattern to format the date.
     * @return
     */
    protected StringBuffer addMessageHeaders(String formatPattern) {
        StringBuffer buffer = new StringBuffer();
        if (!formatPattern.equals(NO_DATE_FORMAT)) {
            buffer.append(new SimpleDateFormat(formatPattern).format(new Date(
                    System.currentTimeMillis())));
        }
        buffer.append(" ");
        // buffer.append(connectionConfiguration.getName());
        // buffer.append(" ");
        return buffer;
    }

    protected void closeConsoleStream(MessageConsoleStream stream) {
        // close the stream
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ioe) {
                log.error("Exception closing the console stream", ioe);
            }
        }
    }

}
