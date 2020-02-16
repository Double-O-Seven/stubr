package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Matcher
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.site.ConstructorParameterStubbingSite
import ch.leadrian.stubr.core.site.MethodParameterStubbingSite
import ch.leadrian.stubr.core.site.MethodReturnValueStubbingSite
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

internal class KotlinTypeIsNullableMatcher<T> : Matcher<T> {

    override fun matches(context: StubbingContext, value: T): Boolean {
        return when (val site = context.site) {
            is MethodParameterStubbingSite -> isParameterTypeNullable(site)
            is MethodReturnValueStubbingSite -> isReturnTypeNullable(site)
            is ConstructorParameterStubbingSite -> isParameterTypeNullable(site)
            else -> false
        }
    }

    private fun isParameterTypeNullable(site: MethodParameterStubbingSite): Boolean {
        val function = site.method.kotlinFunction ?: return false
        // Kotlin function might have an offset if there's a receiver
        val parameterOffset = function.parameters.size - site.method.parameterCount
        val index = site.parameterIndex + parameterOffset
        return function.isParameterTypeNullable(index)
    }

    private fun isReturnTypeNullable(site: MethodReturnValueStubbingSite): Boolean =
            site.method.kotlinFunction?.returnType?.isMarkedNullable ?: false

    private fun isParameterTypeNullable(site: ConstructorParameterStubbingSite): Boolean {
        val function = site.constructor.kotlinFunction ?: return false
        return function.isParameterTypeNullable(site.parameterIndex)
    }

    private fun KFunction<*>.isParameterTypeNullable(index: Int): Boolean = parameters[index].type.isMarkedNullable
}