package com.scorpipede.dungeon.lambda

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler
import com.amazon.ask.model.LaunchRequest
import com.amazon.ask.model.Response
import java.util.Optional

const val speech = "Welcome to the dungeon"

class LaunchHandler : LaunchRequestHandler {
    override fun canHandle(input: HandlerInput?, launchRequest: LaunchRequest?): Boolean = true
    override fun handle(input: HandlerInput?, launchRequest: LaunchRequest?): Optional<Response> =
        input?.responseBuilder
            ?.withSpeech(speech)
            ?.withReprompt(speech)
            ?.build()
            ?: Optional.empty()
}
