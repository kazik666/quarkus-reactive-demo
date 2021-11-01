package com.example.filters;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter, WriterInterceptor {

    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        log.info("Request with URL {}, method {}, media type {} and headers {}"
                , clientRequestContext.getUri(), clientRequestContext.getMethod()
                , clientRequestContext.getMediaType(), clientRequestContext.getHeaders());
        if (clientRequestContext.hasEntity()) {
            val filter = new OutputLoggingFilter(clientRequestContext.getEntityStream());
            clientRequestContext.setEntityStream(filter);
            clientRequestContext.setProperty(OutputLoggingFilter.ENTITY_LOGGER_PROPERTY, filter);
        }
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        val status = clientResponseContext.getStatus();
        val headers = clientResponseContext.getHeaders();

        log.info("Response with status {} and headers {}", status, headers);
        if (clientResponseContext.hasEntity() && clientResponseContext.getLength() > 0) {
            clientResponseContext.setEntityStream(logInboundEntity(clientResponseContext.getLength()
                    , clientResponseContext.getEntityStream()));
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException {
        writerInterceptorContext.proceed();
        val filter = (OutputLoggingFilter) writerInterceptorContext.getProperty(OutputLoggingFilter.ENTITY_LOGGER_PROPERTY);
        log.info("Request Body: {}", filter.getStream().toString());
    }

    private InputStream logInboundEntity(int length, InputStream stream) throws IOException {
        if(!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }

        stream.mark(length);
        var body = new String(IOUtils.readFully(stream, length));
        stream.reset();

        log.info("Response Body {}", body);

        return stream;
    }
}
