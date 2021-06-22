package software.amazon.awssdk.services.dynamodb;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.dynamodb_v2.DynamoDBMetricUtil;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.client.config.SdkClientOption;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Weave(originalName = "software.amazon.awssdk.services.dynamodb.DefaultDynamoDbAsyncClient", type = MatchType.ExactClass)
final class DefaultDynamoDbAsyncClient_Instrumentation {

    private final SdkClientConfiguration clientConfiguration = Weaver.callOriginal();
    private final URI endpoint = clientConfiguration != null ? clientConfiguration.option(SdkClientOption.ENDPOINT) : null;

    @Trace//(async = true, leaf = true)
    public CompletableFuture<ScanResponse> scan(ScanRequest scanRequest) {
        DynamoDBMetricUtil.metrics(NewRelic.getAgent().getTracedMethod(), "scan", scanRequest.tableName(), endpoint);
        return Weaver.callOriginal();
    }

    @Trace(async = true, leaf = true)
    public CompletableFuture<GetItemResponse> getItem(GetItemRequest getItemRequest) {
        DynamoDBMetricUtil.metrics(NewRelic.getAgent().getTracedMethod(), "getItem", getItemRequest.tableName(), endpoint);
        return Weaver.callOriginal();
    }

}