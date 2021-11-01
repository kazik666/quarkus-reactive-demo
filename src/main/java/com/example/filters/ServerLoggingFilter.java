package com.example.filters;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Provider
@Slf4j
public class ServerLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter, WriterInterceptor {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        val method = context.getMethod();
        val path = info.getPath();
        val address = request.remoteAddress().toString();
        val headers = context.getHeaders();

        log.info("Request {} {} from IP {} with headers {}", method, path, address, headers);
        if(context.hasEntity() &&  context.getLength() > 0) {
            context.setEntityStream(
                    this.logInboundEntity(context.getLength(), context.getEntityStream()));
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        val status = responseContext.getStatus();
        val headers = responseContext.getHeaders();

        log.info("Response with status {} and headers {}", status, headers);
        if (responseContext.hasEntity() &&
                headers.getOrDefault("Content-Type", Collections.emptyList()).stream().noneMatch(this::isHtml)) {
            val filter = new OutputLoggingFilter(responseContext.getEntityStream());
            responseContext.setEntityStream(filter);
            requestContext.setProperty(OutputLoggingFilter.ENTITY_LOGGER_PROPERTY, filter);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException {
        writerInterceptorContext.proceed();
        val filter = (OutputLoggingFilter) writerInterceptorContext.getProperty(OutputLoggingFilter.ENTITY_LOGGER_PROPERTY);
        if (filter != null && filter.getStream() != null) {
            log.info("Response Body: {}", filter.getStream().toString());
        }
    }

    private InputStream logInboundEntity(int length, InputStream stream) throws IOException {
        if(!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }

        stream.mark(length);
        var body = new String(IOUtils.readFully(stream, length));
        stream.reset();

        log.info("Request Body {}", body);

        return stream;
    }

    private boolean isHtml(Object value) {
        if (value instanceof MediaType) {
            val mediaType = (MediaType) value;

            return "text".equals(mediaType.getType()) && "html".equals(mediaType.getSubtype());
        }

        return false;
    }
}
