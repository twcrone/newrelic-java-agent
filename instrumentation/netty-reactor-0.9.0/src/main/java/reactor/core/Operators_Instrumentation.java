package reactor.core;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Weave(originalName = "reactor.core.publisher.Operators")
public class Operators_Instrumentation<T> {
    public static <T> CorePublisher<T> onLastAssembly(CorePublisher<T> source) {
        System.out.println("XXXXXXXX Operators onLastAssembly");
        return  Weaver.callOriginal();
    }

//    public void onComplete() {
//        System.out.println("XXXXXXXX Operators onComplete");
//        Weaver.callOriginal();
//    }
}