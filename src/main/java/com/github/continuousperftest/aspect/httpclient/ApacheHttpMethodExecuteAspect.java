/*
 * Copyright 2018 Continuous Performance Test
 * 
 * 
 * This file is part of Continuous Performance Test.
 * https://github.com/continuousperftest/agent-java
 * 
 * Continuous Performance Test is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * Continuous Performance Test is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Continuous
 * Performance Test. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.continuousperftest.aspect.httpclient;

import com.github.continuousperftest.entity.domain.Header;
import com.github.continuousperftest.entity.domain.Perfomance;
import com.github.continuousperftest.entity.domain.RequestAttribute;
import com.github.continuousperftest.entity.domain.ResponseAttribute;
import com.github.continuousperftest.queue.impl.PerfomanceMetricQueue;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Captures execution time of the method.
 * {@link org.apache.http.impl.client.CloseableHttpClient#doExecute}
 *
 * @author Aleh Struneuski
 */
@Aspect
public class ApacheHttpMethodExecuteAspect extends HttpMethodExecuteAspect {

  @Pointcut("execution(* org.apache.http.impl.client.CloseableHttpClient.doExecute(..))")
  public void doExecute() {
  }

  @Around("doExecute()")
  public Object httpMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    HttpHost targetHost = (HttpHost) args[0];
    HttpRequest request = (HttpRequest) args[1];

    Instant start = java.time.Instant.now();
    CloseableHttpResponse response = (CloseableHttpResponse) joinPoint.proceed();
    Instant end = java.time.Instant.now();
    Duration between = Duration.between(start, end);
    final long executionTime = between.toMillis();

    if (!IS_ENABLED) {
      return response;
    }

    Perfomance prefomance = new Perfomance();
    prefomance.setRequestAttribute(toRequestAttribute(targetHost, request));
    prefomance.setResponseAttribute(toResponseAttribute(response));
    prefomance.setExecutionTimeInMillis((int) executionTime);

    PerfomanceMetricQueue.getInstance().offer(prefomance);
    return response;
  }

  private ResponseAttribute toResponseAttribute(CloseableHttpResponse response) {
    ResponseAttribute responseAttribute = new ResponseAttribute();
    responseAttribute.setStatusCode(response.getStatusLine().getStatusCode());
    responseAttribute.setReasonPhrase(response.getStatusLine().getReasonPhrase());
    responseAttribute.setProtocolVersion(response.getStatusLine().getProtocolVersion().toString());
    responseAttribute.setHeaders(toHeaders(response.getAllHeaders()));
    return responseAttribute;
  }

  private RequestAttribute toRequestAttribute(HttpHost targetHost, HttpRequest request) {
    RequestAttribute requestAttribute = new RequestAttribute();
    requestAttribute.setHostName(targetHost.getSchemeName());
    requestAttribute.setHostName(targetHost.getHostName());
    requestAttribute.setPort(targetHost.getPort());
    requestAttribute.setHttpMethod(request.getRequestLine().getMethod());
    requestAttribute.setUri(request.getRequestLine().getUri());
    requestAttribute.setHeaders(toHeaders(request.getAllHeaders()));
    return requestAttribute;
  }

  private List<Header> toHeaders(org.apache.http.Header[] headers) {
    return Arrays.stream(headers).map(header -> new Header(header.getName(), header.getValue()))
        .collect(Collectors.toList());
  }
}
