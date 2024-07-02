package com.myorg.constructs;

import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.myorg.core.InfrastructureStack;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class ProductServiceGetLambdaConstruct extends Construct {

    private final InfrastructureStack infrastructureStack;

    public ProductServiceGetLambdaConstruct(Construct scope, String id, InfrastructureStack infrastructureStack) {
        super(scope, id);
        this.infrastructureStack = infrastructureStack;

        var productServiceGetLambda = createProductServiceLambdaFunction(
                "product-service-get",
                "com.productservice.handler.ProductServiceGetRequestHandler::handleRequest");

        var productServicePostLambda = createProductServiceLambdaFunction(
                "product-service-post",
                "com.productservice.handler.ProductServicePostRequestHandler::handleRequest");

        var productServicePutLambda = createProductServiceLambdaFunction(
                "product-service-put",
                "com.productservice.handler.ProductServicePutRequestHandler::handleRequest");

        var restBasicApi = setupRestApi(productServiceGetLambda, productServicePostLambda, productServicePutLambda);

        new CfnOutput(scope, "ApiEndpointBasic", CfnOutputProps.builder()
                .value(restBasicApi.getUrl())
                .build());

    }

    private RestApi setupRestApi(Function getLambda, Function postLambda, Function putLambda) {
        var restApi = LambdaRestApi.Builder.create(this, "ProductServiceApi")
                .restApiName("ProductServiceApi")
                .handler(postLambda)
                .proxy(false)
                .build();

        Resource productResource = restApi.getRoot().addResource("products");
        productResource.addMethod("POST", new LambdaIntegration(postLambda));
        productResource.addMethod("GET", new LambdaIntegration(getLambda));

        Resource productResourceById = productResource.addResource("{id}");
        productResourceById.addMethod("GET", new LambdaIntegration(getLambda));
        productResourceById.addMethod("PUT", new LambdaIntegration(putLambda));
        return restApi;
    }

    private Function createProductServiceLambdaFunction(String name, String handler) {

        Function lambda = Function.Builder.create(this, name)
                .runtime(Runtime.JAVA_17)
                .memorySize(2048)
                .functionName(name)
                .timeout(Duration.seconds(29))
                .code(Code.fromAsset("..//software/target/product-service.jar"))
                .handler(handler)
                .vpc(infrastructureStack.getVpc())
                .securityGroups(List.of(infrastructureStack.getApplicationSecurityGroup()))
                .environment(new HashMap<>() {
                    {
                        //put("DB_PASSWORD", infrastructureStack.getDatabaseSecretString());
                        put("DB_CONNECTION_URL", infrastructureStack.getDatabaseJDBCConnectionString());
                        put("DB_USER", "postgres");
                        put("JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1");
                    }
                })
                .build();

        return lambda;
    }
}
