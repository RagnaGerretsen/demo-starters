package demo.starter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.util.UUID;

@Slf4j
@Component
public class RestControllerInterceptor implements HandlerInterceptor {

    private static final String MDC_HTTP_STATUS_CODE = "httpStatusCode";
    private static final String MDC_RESPONSE_TIME = "responseTime";
    private static final String MDC_REST_SERVICE = "restService";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String methodName = getMethodName(handler);
        final long currentTimeMillis = System.currentTimeMillis();
        putMdcValues(methodName);

        log.info("************ START {} RequestURL:{} QueryString:{}", methodName,
                request.getRequestURL().toString(), request.getQueryString());
        request.setAttribute("startTime", currentTimeMillis);
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) {
        final ContentCachingResponseWrapper contentCachingResponseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

        try {
            if (log.isInfoEnabled()) {
                final String methodName = getMethodName(handler);
                final long startTimeMillis = (Long) request.getAttribute("startTime");
                final long responseTimeMillis = System.currentTimeMillis() - startTimeMillis;
                MDC.put(MDC_HTTP_STATUS_CODE, String.valueOf(response.getStatus()));
                MDC.put(MDC_RESPONSE_TIME, String.valueOf(responseTimeMillis));

                if (contentCachingResponseWrapper != null) {
                    log.info("Response: {}", new String(contentCachingResponseWrapper.getContentAsByteArray()));
                }

                log.info("************ END {} RequestURL:{} QueryString:{} Time Taken:{}", methodName,
                        request.getRequestURL().toString(), request.getQueryString(), responseTimeMillis);
            }
        } finally {
            MDC.clear();
        }
    }

    @SuppressWarnings("pmd:MDCPutWithoutRemove")
    private void putMdcValues(final String methodName) {
        MDC.put(MDC_REST_SERVICE, methodName);
    }

    private String getMethodName(final Object handler) {
        String methodName = "";
        if (handler instanceof HandlerMethod handlerMethod) {
            methodName = handlerMethod.getMethod().getName();
        }
        return methodName;
    }

}
