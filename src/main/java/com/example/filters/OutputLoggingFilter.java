package com.example.filters;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputLoggingFilter extends FilterOutputStream {

    public static final String ENTITY_LOGGER_PROPERTY = OutputLoggingFilter.class.getName() + ".entityLogger";

    private ByteArrayOutputStream stream;

    public OutputLoggingFilter(OutputStream outputStream) {
        super(outputStream);
        this.stream = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        stream.write(b);
    }

    public ByteArrayOutputStream getStream() {
        return stream;
    }
}
