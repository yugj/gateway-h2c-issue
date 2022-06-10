# Error every three requests with h2c：CloseNowException This stream is not writable Every

**Environment desc**

api gateway (spring cloud gateway) invoke http service(tomcat)

* api gateway: spring cloud gateway 3.1.1, springboot2.7, java11
* http service: springboot2.7, tomcat embed 9.0.63, java11

**gateway main configuration:**

```
server:
  port: 8888
  http2:
    enabled: true
  ssl:
    enabled: false
    
@Configuration
public class NettyConfiguration {

    // https://projectreactor.io/docs/netty/release/reference/index.html#_http2
    @Bean
    public NettyWebServerFactoryCustomizer h2cServerCustomizer(Environment environment, ServerProperties serverProperties) {
        return new NettyWebServerFactoryCustomizer(environment, serverProperties) {
            @Override
            public void customize(NettyReactiveWebServerFactory factory) {
                factory.addServerCustomizers(httpServer -> httpServer.protocol(HttpProtocol.HTTP11, HttpProtocol.H2C));
                super.customize(factory);
            }
        };
    }


    @Bean
    public HttpClientCustomizer h2ClientCustomizer() {
        return httpClient -> httpClient.protocol(HttpProtocol.HTTP11, HttpProtocol.H2C);
    }
}
```

**Describe the bug**


When I send three requests to the gateway, an exception will appear, bug GET request is ok；As long as the Tomcat or the H2C of the gateway is closed, there will be no error


http service(tomcat) side trace:
```
2022-06-10 17:51:12.679  WARN 5312 --- [nio-8889-exec-7] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException: I/O error while reading input message; nested exception is org.apache.catalina.connector.ClientAbortException: java.nio.channels.ClosedChannelException]
2022-06-10 17:51:12.718 ERROR 5312 --- [nio-8889-exec-7] o.a.c.c.C.[Tomcat].[localhost]           : Exception Processing ErrorPage[errorCode=0, location=/error]

org.apache.catalina.connector.ClientAbortException: org.apache.coyote.CloseNowException: Connection [0], Stream [5], This stream is not writable
	at org.apache.catalina.connector.OutputBuffer.doFlush(OutputBuffer.java:310) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.connector.OutputBuffer.flush(OutputBuffer.java:273) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.connector.CoyoteOutputStream.flush(CoyoteOutputStream.java:118) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at java.base/java.io.FilterOutputStream.flush(FilterOutputStream.java:153) ~[na:na]
	at com.fasterxml.jackson.core.json.UTF8JsonGenerator.flush(UTF8JsonGenerator.java:1187) ~[jackson-core-2.13.3.jar:2.13.3]
	at com.fasterxml.jackson.databind.ObjectWriter.writeValue(ObjectWriter.java:1009) ~[jackson-databind-2.13.3.jar:2.13.3]
	at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.writeInternal(AbstractJackson2HttpMessageConverter.java:456) ~[spring-web-5.3.20.jar:5.3.20]
	at org.springframework.http.converter.AbstractGenericHttpMessageConverter.write(AbstractGenericHttpMessageConverter.java:104) ~[spring-web-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(AbstractMessageConverterMethodProcessor.java:290) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor.handleReturnValue(HttpEntityMethodProcessor.java:219) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(HandlerMethodReturnValueHandlerComposite.java:78) ~[spring-web-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:135) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:681) ~[tomcat-embed-core-9.0.63.jar:4.0.FR]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.20.jar:5.3.20]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.63.jar:4.0.FR]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.20.jar:5.3.20]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117) ~[spring-web-5.3.20.jar:5.3.20]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:102) ~[spring-web-5.3.20.jar:5.3.20]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:102) ~[spring-web-5.3.20.jar:5.3.20]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationDispatcher.invoke(ApplicationDispatcher.java:711) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationDispatcher.processRequest(ApplicationDispatcher.java:461) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationDispatcher.doForward(ApplicationDispatcher.java:385) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.ApplicationDispatcher.forward(ApplicationDispatcher.java:313) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.StandardHostValve.custom(StandardHostValve.java:403) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.StandardHostValve.status(StandardHostValve.java:249) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.StreamProcessor.service(StreamProcessor.java:426) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.StreamProcessor.process(StreamProcessor.java:87) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.StreamRunnable.run(StreamRunnable.java:35) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at java.base/java.lang.Thread.run(Thread.java:834) ~[na:na]
Caused by: org.apache.coyote.CloseNowException: Connection [0], Stream [5], This stream is not writable
	at org.apache.coyote.http2.Stream.doStreamCancel(Stream.java:257) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.Http2UpgradeHandler.reserveWindowSize(Http2UpgradeHandler.java:892) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.Stream$StreamOutputBuffer.flush(Stream.java:940) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.Stream$StreamOutputBuffer.flush(Stream.java:886) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.Stream$StreamOutputBuffer.flush(Stream.java:1009) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.Http2OutputBuffer.flush(Http2OutputBuffer.java:77) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.http2.StreamProcessor.flush(StreamProcessor.java:254) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.AbstractProcessor.action(AbstractProcessor.java:402) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.coyote.Response.action(Response.java:209) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	at org.apache.catalina.connector.OutputBuffer.doFlush(OutputBuffer.java:306) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	... 51 common frames omitted
Caused by: org.apache.coyote.http2.StreamException: Connection [0], Stream [5], This stream is not writable
	at org.apache.coyote.http2.Stream.doStreamCancel(Stream.java:249) ~[tomcat-embed-core-9.0.63.jar:9.0.63]
	... 60 common frames omitted


```

spring cloud gateway side trace:

```
2022-06-10 17:51:12.679  WARN 5317 --- [ctor-http-nio-2] r.netty.http.client.HttpClientConnect    : [f60a041a/3-1, L:0.0.0.0/0.0.0.0:51495] The connection observed an error

reactor.netty.http.client.PrematureCloseException: Connection prematurely closed BEFORE response

2022-06-10 17:51:12.705 ERROR 5317 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [186f1c37-3]  500 Server Error for HTTP POST "/http/demo/post"

reactor.netty.http.client.PrematureCloseException: Connection prematurely closed BEFORE response
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
Error has been observed at the following site(s):
	*__checkpoint ⇢ org.springframework.cloud.gateway.filter.WeightCalculatorWebFilter [DefaultWebFilterChain]
	*__checkpoint ⇢ org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
	*__checkpoint ⇢ HTTP POST "/http/demo/post" [ExceptionHandlingWebHandler]
Original Stack Trace:

```

**Sample**

sample application: https://github.com/yugj/gateway-h2c-issue

