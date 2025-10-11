package org.pwss.file_integrity_scanner.config.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a rate limiting filter that restricts the number of
 * requests
 * from any given IP address to specified endpoints within a certain time
 * window.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    /**
     * Maximum requests allowed per minute for any single IP address.
     */
    private static final int MAX_REQUESTS = 5;

    /**
     * Time window in milliseconds, representing the duration over which to count
     * requests.
     */
    private static final long TIME_WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(1);

    /**
     * A thread-safe map to store request counts for each IP address.
     */
    private final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();

    /**
     * Scheduled executor service for resetting the request count at regular
     * intervals.
     */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * URL prefix of restricted endpoints where rate limiting will be applied.
     */
    private final String RESTRICTED_ENDPOINTS_URL = "/api/user";

    /**
     * Constructor that schedules a task to reset request counts at fixed intervals.
     */
    public RateLimitingFilter() {
        // Schedule a task to reset request counts every minute
        scheduler.scheduleAtFixedRate(this::resetRequestCounts, TIME_WINDOW_MILLIS, TIME_WINDOW_MILLIS,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Processes each request and applies rate limiting if the endpoint is
     * restricted.
     *
     * @param request     The HTTP servlet request object
     * @param response    The HTTP servlet response object
     * @param filterChain The filter chain to pass the request along for further
     *                    processing
     * @throws ServletException If a servlet error occurs during request processing
     * @throws IOException      If an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.startsWith(RESTRICTED_ENDPOINTS_URL)) {
            String ipAddress = getClientIP(request);

            if (isRateLimitExceeded(ipAddress)) {
                response.sendError(429);
            } else {
                incrementRequestCount(ipAddress);
                filterChain.doFilter(request, response);
            }
        } else {
            // For other endpoints, just continue the filter chain without rate limiting
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Retrieves the IP address of the client making the request.
     *
     * @param request The HTTP servlet request object
     * @return The client's IP address as a String
     */
    private final String getClientIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * Checks if the rate limit for a given IP address has been exceeded.
     *
     * @param ipAddress The IP address to check
     * @return True if the rate limit is exceeded, false otherwise
     */
    private final boolean isRateLimitExceeded(String ipAddress) {
        Integer count = requestCounts.get(ipAddress);
        return count != null && count >= MAX_REQUESTS;
    }

    /**
     * Increments the request count for a given IP address.
     *
     * @param ipAddress The IP address to increment the count for
     */
    private final void incrementRequestCount(String ipAddress) {
        requestCounts.merge(ipAddress, 1, Integer::sum);
    }

    /**
     * Resets all stored request counts to zero.
     */
    private final void resetRequestCounts() {
        requestCounts.clear();
    }

}
