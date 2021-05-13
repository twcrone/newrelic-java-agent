/*
 *
 *  * Copyright 2020 New Relic Corporation. All rights reserved.
 *  * SPDX-License-Identifier: Apache-2.0
 *
 */

package reactor.core.publisher;

import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Publisher;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Weave(originalName = "reactor.core.publisher.Hooks")
public abstract class Hooks_Instrumentation {
    @NewField
    public static AtomicBoolean instrumented = new AtomicBoolean(false);
    @NewField
    public static AtomicBoolean asyncInstrumented = new AtomicBoolean(false);

    public static void onEachOperator(Function<? super Publisher<Object>, ? extends Publisher<Object>> onEachOperator) {
        System.out.println("XXXXX Hooks onEachOperator");
        Weaver.callOriginal();
    }

    public static void onEachOperator(String key, Function<? super Publisher<Object>, ? extends Publisher<Object>> onEachOperator) {
        System.out.println("XXXXX Hooks onEachOperator 2");
        Weaver.callOriginal();
    }

    public static void onLastOperator(Function<? super Publisher<Object>, ? extends Publisher<Object>> onLastOperator) {
        System.out.println("XXXXX Hooks onLastOperator");
        Weaver.callOriginal();
    }

    public static void onLastOperator(String key, Function<? super Publisher<Object>, ? extends Publisher<Object>> onLastOperator) {
        System.out.println("XXXXX Hooks onLastOperator 2");
        Weaver.callOriginal();
    }


}
