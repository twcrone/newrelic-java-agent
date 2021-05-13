package reactor.core;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Weave(type = MatchType.Interface, originalName = "org.reactivestreams.Publisher")
public class Publisher_Instrumentation<T> {
    public void subscribe(Subscriber<? super T> s) {
        System.out.println("XXXXXXXX Publisher subscribe");
        Weaver.callOriginal();
    }
}
