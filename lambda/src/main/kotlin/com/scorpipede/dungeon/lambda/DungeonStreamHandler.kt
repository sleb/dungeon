package com.scorpipede.dungeon.lambda

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills

class DungeonStreamHandler : SkillStreamHandler(skill) {
    companion object {
        val skill: Skill = Skills.standard()
            .addRequestHandlers(LaunchRequestHandler())
            .build()
    }
}
