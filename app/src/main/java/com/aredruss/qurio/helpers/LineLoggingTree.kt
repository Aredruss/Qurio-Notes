package com.aredruss.qurio.helpers

import timber.log.Timber

class LineLoggingTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "[L:%s] [M:%s] [C:%s]",
            element.getLineNumber(),
            element.getMethodName(),
            super.createStackElementTag(element)
        )
    }
}