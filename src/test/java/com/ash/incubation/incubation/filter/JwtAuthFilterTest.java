package com.ash.incubation.incubation.filter;

import com.ash.incubation.incubation.configuration.UserInfoUserDetailService;
import com.ash.incubation.incubation.service.JWTService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {

    private final JWTService jwtService = mock(JWTService.class);
    private final UserInfoUserDetailService userDetailService = mock(UserInfoUserDetailService.class);
    private final JwtAuthFilter filter = new JwtAuthFilter(jwtService, userDetailService);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void continuesFilterChainWhenAuthorizationHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicInteger chainCalls = new AtomicInteger();
        FilterChain chain = (req, res) -> chainCalls.incrementAndGet();

        filter.doFilter(request, response, chain);

        assertEquals(1, chainCalls.get());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void authenticatesUserFromValidBearerTokenAndContinuesFilterChain() throws Exception {
        String token = "valid-token";
        UserDetails userDetails = User.withUsername("ash").password("encoded").authorities("ROLE_USER").build();
        when(jwtService.extractUsername(token)).thenReturn("ash");
        when(userDetailService.loadUserByUsername("ash")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicInteger chainCalls = new AtomicInteger();
        FilterChain chain = (req, res) -> chainCalls.incrementAndGet();

        filter.doFilter(request, response, chain);

        assertEquals(1, chainCalls.get());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("ash", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void continuesFilterChainWithoutAuthenticationWhenBearerTokenIsMalformed() throws Exception {
        String token = "malformed-token";
        when(jwtService.extractUsername(token)).thenThrow(new IllegalArgumentException("Malformed token"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicInteger chainCalls = new AtomicInteger();
        FilterChain chain = (req, res) -> chainCalls.incrementAndGet();

        filter.doFilter(request, response, chain);

        assertEquals(1, chainCalls.get());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailService, org.mockito.Mockito.never()).loadUserByUsername(org.mockito.ArgumentMatchers.anyString());
    }
}
