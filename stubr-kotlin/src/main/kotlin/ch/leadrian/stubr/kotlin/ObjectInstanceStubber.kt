package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingException
import ch.leadrian.stubr.core.stubber.SimpleStubber
import java.lang.reflect.ParameterizedType

internal object ObjectInstanceStubber : SimpleStubber<Any>() {

    override fun acceptsClass(context: StubbingContext, type: Class<*>): Boolean {
        return type.kotlin.objectInstance != null
    }

    override fun acceptsParameterizedType(context: StubbingContext, type: ParameterizedType): Boolean {
        return accepts(context, type.rawType)
    }

    override fun stubClass(context: StubbingContext, type: Class<*>): Any {
        return type.kotlin.objectInstance ?: throw StubbingException(context.site, type)
    }

    override fun stubParameterizedType(context: StubbingContext, type: ParameterizedType): Any {
        return stub(context, type.rawType)
    }


}