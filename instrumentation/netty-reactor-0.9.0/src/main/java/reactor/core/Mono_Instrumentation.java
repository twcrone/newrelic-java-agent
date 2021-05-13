package reactor.core;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.reactor.netty.TokenLinkingSubscriberEdit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Hooks_Instrumentation;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static com.nr.instrumentation.reactor.netty.TokenLinkingSubscriberEdit.tokenLift;

@Weave(originalName = "reactor.core.publisher.Mono")
public class Mono_Instrumentation<T> {
    @NewField
    private AgentBridge.TokenAndRefCount tokenAndRefCount;

    protected static <T> Mono<T> onAssembly(Mono<T> source) {
        System.out.println("XXXXXXXX onAssembly outside: "  + Hooks_Instrumentation.asyncInstrumented.get());
        Hooks.onEachOperator(TokenLinkingSubscriberEdit.class.getName(), tokenLift());
        System.out.println("XXXXXXXX Mono onAssembly");
        return Weaver.callOriginal();
    }

    protected static <T> Mono<T> onLastAssembly(Mono<T> source) {
        System.out.println("YYYYYYYYY Mono onLastAssembly");
        return Weaver.callOriginal();
    }

    public void subscribe(Subscriber<? super T> s) {
        System.out.println("YYYYYYYYY Mono subscribe");
        Weaver.callOriginal();
    }

    public final Mono<T> doOnSubscribe(Consumer<? super Subscription> onSubscribe) {
        System.out.println("YYYYYYYYY Mono doOnSubscribe");
        return Weaver.callOriginal();
    }
}