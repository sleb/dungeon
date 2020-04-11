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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DungeonStreamHandler : SkillStreamHandler(skill) {
    companion object {
        val skill: Skill = Skills.standard()
            .addRequestHandlers(LaunchRequestHandler())
            .addRequestInterceptor(object: GenericRequestInterceptor<HandlerInput> {
                override fun process(input: HandlerInput?) {
                    log.debug("request: {}", input?.requestEnvelopeJson)
                }
            })
            .withSkillId("amzn1.ask.skill.987654321")
            .build()

        val log: Logger = LoggerFactory.getLogger(DungeonStreamHandler::class.java)
    }
}
