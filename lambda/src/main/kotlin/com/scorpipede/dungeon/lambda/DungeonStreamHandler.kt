package com.scorpipede.dungeon.lambda

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.request.SkillRequest
import com.amazon.ask.request.impl.BaseSkillRequest
import com.amazon.ask.request.interceptor.GenericRequestInterceptor
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory

class DungeonStreamHandler : SkillStreamHandler(skill) {
    init {
        println("initializing logging...")
        val configurationBuilder = ConfigurationBuilderFactory.newConfigurationBuilder()
        configurationBuilder.add(configurationBuilder.newRootLogger(Level.DEBUG))
        Configurator.initialize(configurationBuilder.build())
    }
    companion object {
        val skill: Skill = Skills.standard()
            .addRequestHandlers(LaunchRequestHandler())
            .addRequestInterceptor(object: GenericRequestInterceptor<HandlerInput> {
                override fun process(input: HandlerInput?) {
                    println(input?.requestEnvelopeJson)
                    super.process(input)
                }
            })
            .withSkillId("amzn1.ask.skill.987654321")
            .build()
    }

}
