package com.scorpipede.dungeon.lambda

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills

class DungeonStreamHandler : SkillStreamHandler(skill) {
    init {
        println("$this.javaClass got initialized...")
    }
    companion object {
        val skill: Skill = Skills.standard()
            .addRequestHandlers(LaunchRequestHandler())
            .withSkillId("amzn1.ask.skill.987654321")
            .build()
    }
}
