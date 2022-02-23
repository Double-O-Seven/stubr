/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.samples.spekmockk

import ch.leadrian.stubr.core.Matcher
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.Stubbers
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.matcher.Matchers.*
import ch.leadrian.stubr.core.strategy.StubbingStrategies.memoized
import ch.leadrian.stubr.javafaker.FakerStrategies.firstName
import ch.leadrian.stubr.javafaker.FakerStrategies.lastName
import ch.leadrian.stubr.javafaker.JavaFakerStubbingStrategies.faked
import ch.leadrian.stubr.kotlin.KotlinStubbers
import ch.leadrian.stubr.kotlin.applyWhen
import ch.leadrian.stubr.mockk.MockKStubbingStrategies.mockkAny
import ch.leadrian.stubr.spek.memoizedStub
import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.lifecycle.MemoizedValue
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import java.util.*

fun familyStubber(seed: Long = 1234567890L): Stubber {
    val random = Random(seed)
    return Stubber.builder()
        .include(Stubbers.defaultStubber())
        .include(KotlinStubbers.defaultStubber())
        .stubWith(faked(firstName(), random))
        .stubWith(faked(lastName(), random))
        .stubWith(memoized(mockkAny(relaxed = false)).applyWhen(testSubject().or(mockedTestDependency())))
        .build()
}

private fun testSubject(): Matcher<Type> = site(parent(equalTo(TestSubjectStubbingSite)))

private fun mockedTestDependency(): Matcher<Type> = site(equalTo(MockedTestDependencyStubbingSite))

object TestSubjectStubbingSite : StubbingSite {

    override fun getParent(): Optional<StubbingSite> = Optional.empty()

}

object MockedTestDependencyStubbingSite : StubbingSite {

    override fun getParent(): Optional<StubbingSite> = Optional.empty()

}

inline fun <reified T : Any> LifecycleAware.testSubject(crossinline action: T.() -> Unit = {}): MemoizedValue<T> {
    val javaClass = T::class.java
    if (javaClass.isInterface || Modifier.isAbstract(javaClass.modifiers)) {
        throw IllegalArgumentException("$javaClass must be a concrete class")
    }
    return memoizedStub(site = TestSubjectStubbingSite) { apply(action) }
}

inline fun <reified T : Any> LifecycleAware.mockedTestDependency(crossinline action: T.() -> Unit = {}): MemoizedValue<T> =
    memoizedStub(site = MockedTestDependencyStubbingSite) { apply(action) }