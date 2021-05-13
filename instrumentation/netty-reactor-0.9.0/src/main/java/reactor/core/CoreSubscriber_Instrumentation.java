package reactor.core;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Subscription;

@Weave(type = MatchType.Interface, originalName = "reactor.core.CoreSubscriber")
public class CoreSubscriber_Instrumentation<T> {

    public void onSubscribe(Subscription s) {
        System.out.println("XXXXXXXX CoreSubscriber onSubscribe");
        Weaver.callOriginal();
    }
}
