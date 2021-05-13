package reactor.core;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Subscription;

@Weave(type = MatchType.Interface, originalName = "org.reactivestreams.Subscriber")
public class Subscriber_Instrumentation<T> {

    public void onSubscribe(Subscription s) {
        System.out.println("XXXXXXXX Subscriber onSubscribe");
        Weaver.callOriginal();
    }

    public void onNext(T t) {
        System.out.println("XXXXXXXX Subscriber onNext");
        Weaver.callOriginal();
    }

    public void onError(Throwable t) {
        System.out.println("XXXXXXXX Subscriber onError");
        Weaver.callOriginal();
    }

    public void onComplete() {
        System.out.println("XXXXXXXX Subscriber onComplete");
        Weaver.callOriginal();
    }
}
