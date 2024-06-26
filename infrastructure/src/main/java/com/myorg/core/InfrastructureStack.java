package com.myorg.core;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.events.EventBus;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;
import java.util.List;

public class InfrastructureStack extends Stack {

    private IVpc vpc;
    private DatabaseSecret databaseSecret;
    private DatabaseInstance database;
    private ISecurityGroup applicationSecurityGroup;

    public InfrastructureStack(final Construct parent, final String id) {
        this(parent, id, null);

        vpc = createProductServicesVpc();
        databaseSecret = createDatabaseSecret();
        database = createRDSPostgresInstance(vpc, databaseSecret);
        applicationSecurityGroup = new SecurityGroup(this, "ApplicationSecurityGroup",
                SecurityGroupProps
                        .builder()
                        .securityGroupName("applicationSG")
                        .vpc(vpc)
                        .allowAllOutbound(true)
                        .build());

        //new DatabaseSetupConstruct(this, "UnicornDatabaseConstruct");
    }

    private IVpc createProductServicesVpc() {
        return Vpc.Builder.create(this, "ProductServiceVpc")
                .vpcName("ProductServiceVpc")
                .build();
    }

    private SecurityGroup createDatabaseSecurityGroup(IVpc vpc) {
        var databaseSecurityGroup = SecurityGroup.Builder.create(this, "DatabaseSG")
                .securityGroupName("DatabaseSG")
                .allowAllOutbound(false)
                .vpc(vpc)
                .build();

        databaseSecurityGroup.addIngressRule(
                Peer.ipv4("10.0.0.0/16"),
                Port.tcp(5432),
                "Allow Database Traffic from local network");

        return databaseSecurityGroup;
    }

    private DatabaseInstance createRDSPostgresInstance(IVpc vpc, DatabaseSecret databaseSecret) {

        var databaseSecurityGroup = createDatabaseSecurityGroup(vpc);
        var engine = DatabaseInstanceEngine.postgres(PostgresInstanceEngineProps.builder().version(PostgresEngineVersion.VER_13).build());

        return DatabaseInstance.Builder.create(this, "ProduceService")
                .engine(engine)
                .vpc(vpc)
                .allowMajorVersionUpgrade(true)
                .backupRetention(Duration.days(0))
                .databaseName("products")
                .instanceIdentifier("products")
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MEDIUM))
                .vpcSubnets(SubnetSelection.builder()
                        .subnetType(SubnetType.PRIVATE_WITH_EGRESS)
                        .build())
                .securityGroups(List.of(databaseSecurityGroup))
                .credentials(Credentials.fromSecret(databaseSecret))
                .build();
    }

    private DatabaseSecret createDatabaseSecret() {
        return DatabaseSecret.Builder
                .create(this, "postgres")
                .secretName("products-db-secret")
                .username("postgres").build();
    }

    public IVpc getVpc() {
        return vpc;
    }

    public ISecurityGroup getApplicationSecurityGroup() {
        return applicationSecurityGroup;
    }

    public String getDatabaseSecretString(){
        return databaseSecret.secretValueFromJson("password").toString();
    }

    public String getDatabaseJDBCConnectionString(){
        return "jdbc:postgresql://" + database.getDbInstanceEndpointAddress() + ":5432/postgres";
    }


    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

    }
}
