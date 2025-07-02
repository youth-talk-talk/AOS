package com.core.yongproject.timber

import timber.log.Timber

class CustomTimberDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String = "${element.fileName}:${element.lineNumber}#${element.methodName}"
}
