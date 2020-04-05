package com.scorpipede.dungeon.infra

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.services.codedeploy.LambdaDeploymentConfig
import software.amazon.awscdk.services.codedeploy.LambdaDeploymentGroup
import software.amazon.awscdk.services.lambda.Alias
import software.amazon.awscdk.services.lambda.CfnParametersCode
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import java.time.Instant

class DungeonLambdaStack(scope: Construct?, id: String) : Stack(scope, id) {
    var lambdaCode: CfnParametersCode = CfnParametersCode.fromCfnParameters()

    private val func: Function = Function.Builder.create(this, "DungeonLambda")
        .code(lambdaCode)
        .handler("com.scorpipede.dungeon.lambda.DungeonStreamHandler")
        .runtime(Runtime.JAVA_11)
        .build()

    private val version = func.addVersion(Instant.now().toString())
    private val alias: Alias = Alias.Builder.create(this, "LambdaAlias")
        .aliasName("LambdaAlias")
        .version(version)
        .build()

    private val deploymentGroup: LambdaDeploymentGroup = LambdaDeploymentGroup.Builder.create(this, "DeploymentGroup")
        .alias(alias)
        .deploymentConfig(LambdaDeploymentConfig.ALL_AT_ONCE)
        .build();
}
