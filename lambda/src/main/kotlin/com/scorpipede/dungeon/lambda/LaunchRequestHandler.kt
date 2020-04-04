package com.scorpipede.dungeon.lambda

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.LaunchRequest
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.requestType
import java.util.Optional

class LaunchRequestHandler : RequestHandler {
    override fun canHandle(input: HandlerInput?): Boolean =
        input?.matches(requestType(LaunchRequest::class.java)) ?: false

    override fun handle(input: HandlerInput?): Optional<Response> {
        var speech = "Welcome to the dungeon"
        return if (input != null) {
            input.responseBuilder
                .withSpeech(speech)
                .withReprompt(speech)
                .build()
        } else Optional.empty()
    }
}
