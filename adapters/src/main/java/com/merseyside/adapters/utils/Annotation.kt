package com.merseyside.adapters.utils

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR, message = "This is an internal merseyLib.adapters API that " +
            "should not be used from outside of merseyLib.adapters."
)
annotation class InternalAdaptersApi

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR, message = "This api make sense only with BaseAdapter. Using with another " +
            "adapters throws an exception."
)
annotation class BaseAdapterApi