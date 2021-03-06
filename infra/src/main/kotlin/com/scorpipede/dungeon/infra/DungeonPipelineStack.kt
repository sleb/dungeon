package com.scorpipede.dungeon.infra

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.SecretValue
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.services.codebuild.BuildSpec
import software.amazon.awscdk.services.codebuild.PipelineProject
import software.amazon.awscdk.services.codepipeline.Artifact
import software.amazon.awscdk.services.codepipeline.Pipeline
import software.amazon.awscdk.services.codepipeline.StageProps
import software.amazon.awscdk.services.codepipeline.actions.CloudFormationCreateUpdateStackAction
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction
import software.amazon.awscdk.services.lambda.CfnParametersCode

class DungeonPipelineStack(scope: Construct?, lambdaCode: CfnParametersCode, lambdaStackId: String) : Stack(scope, "DungeonPipelineStack") {
    private val cdkBuild: PipelineProject = PipelineProject.Builder.create(this, "CdkBuild")
        .buildSpec(
            BuildSpec.fromObject(
                mapOf(
                    "version" to "0.2",
                    "phases" to mapOf(
                        "build" to mapOf(
                            "commands" to listOf(
                                "cd infra",
                                "npx aws-cdk synth"
                            )
                        )
                    ),
                    "artifacts" to mapOf(
                        "base-directory" to "infra/cdk.out",
                        "files" to listOf("DungeonLambdaStack.template.json")
                    )
                )
            )
        )
        .build()

    private val codeBuild: PipelineProject = PipelineProject.Builder.create(this, "CodeBuild")
        .buildSpec(
            BuildSpec.fromObject(
                mapOf(
                    "version" to "0.2",
                    "phases" to mapOf(
                        "build" to mapOf(
                            "commands" to listOf(
                                "cd lambda",
                                "./gradlew -q installDist"
                            )
                        )
                    ),
                    "artifacts" to mapOf(
                        "base-directory" to "lambda/build/install/lambda",
                        "files" to listOf("**/*")
                    )
                )
            )
        )
        .build()

    private val sourceArtifact = Artifact.artifact("src")
    private val cdkArtifact = Artifact.artifact("cdk")
    private val buildArtifact = Artifact.artifact("zip")

    private val pipeline: Pipeline = Pipeline.Builder.create(this, "DungeonPipeline")
        .pipelineName("DungeonPipeline")
        .stages(
            listOf(
                StageProps.builder()
                    .stageName("Source")
                    .actions(
                        listOf(
                            GitHubSourceAction.Builder.create()
                                .actionName("GitHubSource")
                                .owner("sleb")
                                .repo("dungeon")
                                .oauthToken(SecretValue.secretsManager("github"))
                                .output(sourceArtifact)
                                .build()
                        )
                    )
                    .build(),
                StageProps.builder()
                    .stageName("Build")
                    .actions(
                        listOf(
                            CodeBuildAction.Builder.create()
                                .actionName("DungeonCodeBuild")
                                .input(sourceArtifact)
                                .outputs(listOf(buildArtifact))
                                .project(codeBuild)
                                .build(),
                            CodeBuildAction.Builder.create()
                                .actionName("CdkCodeBuild")
                                .input(sourceArtifact)
                                .project(cdkBuild)
                                .outputs(listOf(cdkArtifact))
                                .build()
                        )
                    )
                    .build(),
                StageProps.builder()
                    .stageName("Deploy")
                    .actions(
                        listOf(
                            CloudFormationCreateUpdateStackAction.Builder.create()
                                .stackName(lambdaStackId)
                                .actionName("LambdaUpdate")
                                .templatePath(cdkArtifact.atPath("DungeonLambdaStack.template.json"))
                                .adminPermissions(true)
                                .parameterOverrides(lambdaCode.assign(buildArtifact.s3Location))
                                .extraInputs(listOf(cdkArtifact, buildArtifact))
                                .build()
                        )
                    )
                    .build()
            )
        )
        .build()
}
