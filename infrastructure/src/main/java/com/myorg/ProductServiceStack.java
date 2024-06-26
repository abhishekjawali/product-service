package com.myorg;

import com.myorg.core.InfrastructureStack;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

import com.myorg.constructs.ProductServiceGetLambdaConstruct;

public class ProductServiceStack extends Stack {
    private InfrastructureStack infrastructureStack;

    public ProductServiceStack(final Construct scope, final String id, final StackProps props, final InfrastructureStack infrastructureStack) {
        super(scope, id, props);

        // Get previously created infrastructure stack
        this.infrastructureStack = infrastructureStack;

        new ProductServiceGetLambdaConstruct(this, "ProductServiceGetLambdaConstruct", infrastructureStack);

    }

}