package ru.hh.nab.telemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.jclient.common.HttpClientContext;
import ru.hh.jclient.common.HttpHeaders;
import ru.hh.jclient.common.Request;
import ru.hh.jclient.common.RequestBuilder;
import ru.hh.jclient.common.RequestContext;
import ru.hh.jclient.common.RequestDebug;
import ru.hh.jclient.common.exception.ResponseConverterException;

public class TelemetryListenerImpl implements RequestDebug {
  private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryListenerImpl.class);

  private static final TextMapGetter<HttpClientContext> GETTER = createGetter();
  private final Tracer tracer;
  private final TextMapPropagator textMapPropagator;
  private Span span;

  public TelemetryListenerImpl(Tracer tracer, TextMapPropagator textMapPropagator) {
    this.tracer = tracer;
    this.textMapPropagator = textMapPropagator;
  }

  @Override
  public Request onExecuteRequest(Request request, HttpClientContext httpClientContext) {
    Context context = textMapPropagator.extract(Context.current(), httpClientContext, GETTER);
    span = tracer.spanBuilder(
        request.getUrl()) //todo более общий
        .setParent(context)
        .setSpanKind(SpanKind.CLIENT)
        .setAttribute("requestTimeout", request.getRequestTimeout())
        .setAttribute("readTimeout", request.getReadTimeout())
        .startSpan();
    LOGGER.trace("spanStarted : {}", span);
    RequestBuilder requestBuilder = new RequestBuilder(request);

    HttpHeaders headers = new HttpHeaders();

    headers.add(request.getHeaders());
    try (Scope ignore = span.makeCurrent()) {
      textMapPropagator.inject(Context.current(), headers, HttpHeaders::add);
    }
    requestBuilder.setHeaders(headers);
    return requestBuilder.build();
  }


  @Override
  public ru.hh.jclient.common.Response onResponse(ru.hh.jclient.common.Response response) {
    span.setStatus(StatusCode.OK, String.format("code:%d; description:%s", response.getStatusCode(), response.getStatusText()));
    span.end();
    LOGGER.trace("span closed: {}", span);
    return response;
  }

  @Override
  public void onClientProblem(Throwable t) {
    span.setStatus(StatusCode.ERROR, t.getMessage());
    span.end();
    LOGGER.trace("span closed: {}", span);
  }

  private static TextMapGetter<HttpClientContext> createGetter() {
    return new TextMapGetter<>() {
      @Override
      public Iterable<String> keys(HttpClientContext carrier) {
        return carrier.getHeaders().keySet();
      }

      @Override
      public String get(HttpClientContext carrier, String key) {
        List<String> header = carrier.getHeaders().get(key);
        if (header == null || header.isEmpty()) {
          return "";
        }
        return header.get(0);
      }
    };
  }

  @Override
  public void onRequest(Request request, Optional<?> requestBodyEntity, RequestContext context) {

  }

  @Override
  public void onRetry(Request request, Optional<?> requestBodyEntity, int retryCount, RequestContext context) {

  }


  @Override
  public void onResponseConverted(Optional<?> result) {

  }


  @Override
  public void onConverterProblem(ResponseConverterException e) {

  }

  @Override
  public void onProcessingFinished() {

  }
}
