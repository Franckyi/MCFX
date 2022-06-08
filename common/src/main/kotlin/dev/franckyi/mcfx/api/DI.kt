package dev.franckyi.mcfx.api

import kotlin.reflect.KClass

object DI {
    private val providers: MutableMap<KClass<out Any>, ParameterProvider.() -> Any> = mutableMapOf()

    inline fun <reified T : Any> singleton(value: T) = factory { value }

    fun <T : Any> singleton(type: KClass<T>, value: T) = factory(type) { value }

    inline fun <reified T : Any> factory(noinline provider: ParameterProvider.() -> T) = factory(T::class, provider)

    fun <T : Any> factory(type: KClass<T>, provider: ParameterProvider.() -> T) {
        providers[type] = provider
    }

    inline fun <reified T : Any> get(vararg params: Any): T = get(T::class, *params)

    @Suppress("unchecked_cast")
    fun <T : Any> get(type: KClass<T>, vararg params: Any): T =
        providers[type]!!.invoke(ParameterProvider(*params)) as T

    inline fun <reified T : Any> inject(vararg params: Any): Lazy<T> = inject(T::class, *params)

    fun <T : Any> inject(type: KClass<T>, vararg params: Any): Lazy<T> = lazy { get(type, *params) }
}

class ParameterProvider(private vararg val param: Any) {
    private var index = 0

    fun <T> param(): T = paramAt(index++)

    @Suppress("unchecked_cast")
    fun <T> paramAt(index: Int): T = param[index] as T
}