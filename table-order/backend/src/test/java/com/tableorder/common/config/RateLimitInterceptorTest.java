package com.tableorder.common.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitInterceptorTest {

    private RateLimitInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new RateLimitInterceptor();
    }

    @Test
    void 제한내_요청허용() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        for (int i = 0; i < 10; i++) {
            assertThat(interceptor.preHandle(request, response, null)).isTrue();
        }
    }

    @Test
    void 제한초과_429응답() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        for (int i = 0; i < 10; i++) {
            interceptor.preHandle(request, response, null);
        }

        boolean result = interceptor.preHandle(request, response, null);

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(429);
    }
}
