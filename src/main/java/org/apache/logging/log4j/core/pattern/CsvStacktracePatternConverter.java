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

/**
 * @author yosuketsuboi
 *
 */
@Plugin(name = "CsvStacktracePatternConverter", category = "Converter")
@ConverterKeys({ "sc", "stcsv" })
public final class CsvStacktracePatternConverter extends
        LogEventPatternConverter {

    /**
     * Private constructor.
     * 
     * @param options
     *            options, may be null.
     */
    private CsvStacktracePatternConverter() {
        super("CsvStacktrace", "csvstacktrace");
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
    public static CsvStacktracePatternConverter newInstance(
            final Configuration config, final String[] options) {
        return new CsvStacktracePatternConverter();
    }

    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        Throwable t = event.getThrown();
        if (t != null) {
            StringBuilder sb = new StringBuilder();
            do {
                if (sb.length() > 0) {
                    sb.append(" caused by ");
                }
                sb.append(t);
                sb.append("[");
                boolean first = true;
                for (StackTraceElement element : t.getStackTrace()) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(element);
                    first = false;
                }
                sb.append("]");
            } while ((t = t.getCause()) != null);
            toAppendTo.append(sb);
        }
    }
}
