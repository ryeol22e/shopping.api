package com.project.shopping.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * 
 * @author
 * @date 2024.05.12
 */
public class XssServeltRequestWrapper extends HttpServletRequestWrapper {
    private XssEscapeFilter xssEscapeFilter;
    private String path = null;

    public XssServeltRequestWrapper(ServletRequest request, XssEscapeFilter xssEscapeFilter) {
        super((HttpServletRequest) request);
        this.xssEscapeFilter = xssEscapeFilter;

        String contextPath = ((HttpServletRequest) request).getContextPath();
        this.path = ((HttpServletRequest) request).getRequestURI().substring(contextPath.length());
    }

    @Override
    public String getParameter(String paramName) {
        String value = super.getParameter(paramName);
        return doFilter(paramName, value);
    }

    @Override
    public String[] getParameterValues(String paramName) {
        String[] values = super.getParameterValues(paramName);
        if (values == null) {
            return values;
        }
        for (int index = 0; index < values.length; index++) {
            values[index] = doFilter(paramName, values[index]);
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = super.getParameterMap();
        Map<String, String[]> newFilteredParamMap = new HashMap<>();

        Set<Map.Entry<String, String[]>> entries = paramMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            String paramName = entry.getKey();
            String[] valueObj = entry.getValue();
            String[] filteredValue = new String[valueObj.length];
            for (int index = 0; index < valueObj.length; index++) {
                filteredValue[index] = doFilter(paramName, String.valueOf(valueObj[index]));
            }

            newFilteredParamMap.put(entry.getKey(), filteredValue);
        }

        return newFilteredParamMap;
    }

    /**
     * @param paramName String
     * @param value String
     * @return String
     */
    private String doFilter(String paramName, String value) {
        return xssEscapeFilter.doFilter(path, paramName, value);
    }
}
