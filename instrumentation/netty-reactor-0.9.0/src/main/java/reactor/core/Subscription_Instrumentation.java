package reactor.core;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

@Weave(type = MatchType.Interface, originalName = "org.reactivestreams.Subscription")
public class Subscription_Instrumentation {

    public void request(long n) {
        System.out.println("XXXXXXXX Subscription request");
        Weaver.callOriginal();
    }

    public void cancel() {
        System.out.println("XXXXXXXX Subscription cancel");
        Weaver.callOriginal();
    }
}
