package com.nr.instrumentation.reactor.netty;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.nr.instrumentation.NettyReactorConfig;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.Fuseable;
import reactor.core.Scannable;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import java.util.function.BiFunction;
import java.util.function.Function;

// Based on OpenTelemetry code
// https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/reactor-3.1/library/src/main/java/io/opentelemetry/instrumentation/reactor/TracingSubscriber.java
public class TokenLinkingSubscriberEdit<T> implements CoreSubscriber<T> {
    private final Token token;
    private final Subscriber<? super T> subscriber;
    private Context context;

    public TokenLinkingSubscriberEdit(Subscriber<? super T> subscriber, Context ctx) {
        System.out.println("PPPP TokenLinkingSubscriberEdit con");
        this.subscriber = subscriber;
        this.context = ctx;
        // newrelic-token is added by spring-webflux instrumentation
        this.token = ctx.getOrDefault("newrelic-token", null);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("PPPP TokenLinkingSubscriberEdit onSubscribe");
        withNRToken(() -> subscriber.onSubscribe(subscription));
    }

    @Override
    public void onNext(T o) {
        System.out.println("PPPP TokenLinkingSubscriberEdit onNext");
        withNRToken(() -> subscriber.onNext(o));
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("PPPP TokenLinkingSubscriberEdit onError");
        withNRError(() -> subscriber.onError(throwable), throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("PPPP TokenLinkingSubscriberEdit onComplete");
        withNRToken(subscriber::onComplete);
    }

    @Override
    public Context currentContext() {
        System.out.println("PPPP TokenLinkingSubscriberEdit currentContext");
        return context;
    }

    @Trace(async = true, excludeFromTransactionTrace = true)
    private void withNRToken(Runnable runnable) {
        if (token != null && AgentBridge.getAgent().getTransaction(false) == null) {
            token.link();
        }
        runnable.run();
    }

    @Trace(async = true, excludeFromTransactionTrace = true)
    private void withNRError(Runnable runnable, Throwable throwable) {
        if (token != null && token.isActive()) {
            token.link();
            if (NettyReactorConfig.errorsEnabled) {
                NewRelic.noticeError(throwable);
            }
        }
        runnable.run();
    }

    public static <T> Function<? super Publisher<T>, ? extends Publisher<T>> tokenLift() {
        System.out.println("PPPP TokenLinkingSubscriberEdit tokenLift");
        return Operators.lift(new TokenLifter<>());
    }

    private static class TokenLifter<T>
            implements BiFunction<Scannable, CoreSubscriber<? super T>, CoreSubscriber<? super T>> {

        public TokenLifter() {
            System.out.println("PPPP TokenLinkingSubscriberEdit TokenLifter");
        }


        @Override
        public CoreSubscriber<? super T> apply(Scannable publisher, CoreSubscriber<? super T> sub) {
            System.out.println("PPPP TokenLinkingSubscriberEdit TokenLifter apply");
            // if Flux/Mono #just, #empty, #error
            if (publisher instanceof Fuseable.ScalarCallable) {
                return sub;
            }
            Token token = sub.currentContext().getOrDefault("newrelic-token", null);
            if (token != null ) {
                return new TokenLinkingSubscriberEdit<>(sub, sub.currentContext());
            }
            return sub;
        }
    }
}