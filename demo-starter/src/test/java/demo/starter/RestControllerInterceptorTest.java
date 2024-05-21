package demo.starter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestControllerInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HandlerMethod handlerMethod;

    @InjectMocks
    private RestControllerInterceptor restControllerInterceptor;

    @Test
    public void testPreHandle() throws Exception {
        when(this.handlerMethod.getMethod()).thenReturn(this.getClass().getMethod("testPreHandle"));
        StringBuffer stringBuffer = new StringBuffer();
        when(this.request.getRequestURL()).thenReturn(stringBuffer);
        boolean result = restControllerInterceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
    }

    @Test
    public void testAfterCompletion() throws Exception {
        when(handlerMethod.getMethod()).thenReturn(this.getClass().getMethod("testAfterCompletion"));
        when(this.request.getAttribute("startTime")).thenReturn(System.currentTimeMillis());
        when(this.request.getRequestURL()).thenReturn(new StringBuffer());

        restControllerInterceptor.afterCompletion(request, response, handlerMethod, null);
    }
}