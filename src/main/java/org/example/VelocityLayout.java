package org.example;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Date;

import static org.apache.log4j.EnhancedPatternLayout.DEFAULT_CONVERSION_PATTERN;

public class VelocityLayout extends Layout {
    private String pattern;
    private VelocityContext context;
    private VelocityEngine engine;


    public VelocityLayout() {
        this(DEFAULT_CONVERSION_PATTERN);
        this.context = new VelocityContext();
        this.engine = new VelocityEngine();
    }
    public VelocityLayout (String newPattern) {
        super();
        pattern = newPattern;
    }

    @Override
    public void activateOptions () {
    }
    @Override
    public String format (LoggingEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLogger()); // Not sure about the category output
        context.put("d", new Date(event.getTimeStamp()));
        context.put("m", event.getMessage());
        context.put("p", event.getLevel());
        context.put("t", event.getThreadName());
        context.put("n", "\n");
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", pattern);
        return writer.toString();
    }

    @Override
    public boolean ignoresThrowable () {
        return true;
    }
}


