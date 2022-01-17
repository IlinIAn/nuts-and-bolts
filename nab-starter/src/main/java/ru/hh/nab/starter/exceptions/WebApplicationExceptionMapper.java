package ru.hh.nab.starter.exceptions;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import static ru.hh.jclient.common.HttpStatuses.INTERNAL_SERVER_ERROR;
import static ru.hh.nab.starter.exceptions.NabExceptionMapper.LOW_PRIORITY;

@Provider
@Priority(LOW_PRIORITY)
public class WebApplicationExceptionMapper extends NabExceptionMapper<WebApplicationException> {
  public WebApplicationExceptionMapper() {
    super(null, LoggingLevel.ERROR_WITH_STACK_TRACE);
  }

  @Override
  protected void logException(WebApplicationException wae, LoggingLevel loggingLevel) {
    if (wae.getCause() == null && wae.getResponse().getStatus() != INTERNAL_SERVER_ERROR) {
      return;
    }
    if (wae.getResponse().getStatus() < 500) {
      loggingLevel = LoggingLevel.INFO_WITH_STACK_TRACE;
    }
    super.logException(wae, loggingLevel);
  }

  @Override
  protected Response serializeException(Response.StatusType statusCode, WebApplicationException exception) {
    return exception.getResponse();
  }
}
