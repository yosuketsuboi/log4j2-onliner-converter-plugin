/*
 * Copyright (C) 2014 Yosuke Tsuboi
 * 
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

/**
 * @author yosuketsuboi
 *
 */
@Plugin(name = "OnlinerMessagePatternConverter", category = "Converter")
@ConverterKeys({ "om", "olrmsg" })
public final class OnlinerMessagePatternConverter extends
        LogEventPatternConverter {

    private final String[] formats;

    private final Configuration config;

    /**
     * Private constructor.
     * 
     * @param options
     *            options, may be null.
     */
    private OnlinerMessagePatternConverter(final Configuration config,
            final String[] options) {
        super("Message", "message");
        formats = options;
        this.config = config;
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param config
     *            The Configuration.
     * @param options
     *            options, may be null.
     * @return instance of pattern converter.
     */
    public static OnlinerMessagePatternConverter newInstance(
            final Configuration config, final String[] options) {
        return new OnlinerMessagePatternConverter(config, options);
    }

    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final Message msg = event.getMessage();
        if (msg != null) {
            String result;
            if (msg instanceof MultiformatMessage) {
                result = ((MultiformatMessage) msg)
                        .getFormattedMessage(formats);
            } else {
                result = msg.getFormattedMessage();
            }
            if (result != null) {
                toAppendTo
                        .append((config != null && result.contains("${") ? config
                                .getStrSubstitutor().replace(event, result)
                                : result).replaceAll("\\\\n", "\\\\\\\\n")
                                .replaceAll("\n", "\\\\n"));
            } else {
                toAppendTo.append("null");
            }
        }
    }

}
