package com.grayson.audiocross.testbase.rule

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.Locale

/**
 * Android Log Rule
 * Can avoid log execute in test
 */
class LogMockRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockkStatic(Log::class)

                every { Log.d(any(), any()) } answers { printMethod(method.name, args); 0}
                every { Log.i(any(), any()) } answers { printMethod(method.name, args); 0}
                every { Log.e(any(), any()) } answers { printMethod(method.name, args); 0}
                every { Log.w(any(), any<String>()) } answers { printMethod(method.name, args); 0}
                every { Log.w(any(), any<Throwable>()) } answers { printMethod(method.name, args); 0}
                every { Log.v(any(), any()) } answers { printMethod(method.name, args); 0}

                try {
                    base.evaluate()
                } finally {
                    unmockkStatic(Log::class)
                }
            }
        }
    }

    private fun printMethod(name: String, args: List<Any?>) {
        val msg = "${name.uppercase()}/${args[0]}: ${args[1]}"

        if ("e".equals(name, true)) {
            System.err.println(msg)
        } else {
            println(msg)
        }

        (args.getOrNull(2) as? Throwable)?.printStackTrace()
    }
}