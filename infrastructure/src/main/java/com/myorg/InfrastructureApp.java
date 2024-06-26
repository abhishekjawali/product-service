package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Aspects;

import com.myorg.core.InfrastructureStack;

import io.github.cdklabs.cdknag.AwsSolutionsChecks;
import io.github.cdklabs.cdknag.NagPackSuppression;
import io.github.cdklabs.cdknag.NagSuppressions;

import java.util.List;

import com.myorg.ProductServiceStack;

public final class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        var infrastructureStack = new InfrastructureStack(app, "InfrastructureStack");
        var productServiceStack = new ProductServiceStack(app, "ProductServiceStack", null, infrastructureStack);


        //Add CDK-NAG checks: https://github.com/cdklabs/cdk-nag
        //Add suppression to exclude certain findings that are not needed for Workshop environment
        Aspects.of(app).add(new AwsSolutionsChecks());
        var suppression = List.of(
            new NagPackSuppression.Builder().id("AwsSolutions-APIG4").reason("The workshop environment does not require API-Gateway authorization").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-COG4").reason("The workshop environment does not require Cognito User Pool authorization").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-RDS3").reason("Workshop environment does not need a Multi-AZ setup to reduce cost").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-IAM4").reason("AWS Managed policies are acceptable for the workshop").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-RDS10").reason("Workshop environment is ephemeral and the database should be deleted by the end of the workshop").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-RDS11").reason("Database is in a private subnet and can use the default port").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-APIG2").reason("API Gateway request validation is not needed for workshop").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-APIG1").reason("API Gateway access logging not needed for workshop setup").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-APIG6").reason("API Gateway access logging not needed for workshop setup").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-VPC7").reason("Workshop environment does not need VPC flow logs").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-SMG4").reason("Ephemeral workshop environment does not need to rotate secrets").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-RDS2").reason("Workshop non-sensitive test database does not need encryption at rest").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-APIG3").reason("Workshop API Gateways do not need AWS WAF assigned" ).build(),
            new NagPackSuppression.Builder().id("AwsSolutions-RDS13").reason("Workshop Database does not need backups").build(),
            new NagPackSuppression.Builder().id("AwsSolutions-L1").reason("AWS Serverless Java Container doesn't support the latest runtime yet.").build()
        );

        NagSuppressions.addStackSuppressions(infrastructureStack, suppression);
        NagSuppressions.addStackSuppressions(productServiceStack, suppression);
        app.synth();
    }
}
