package com.merseyside.utils.reflection

import java.lang.reflect.GenericDeclaration
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.*

object ReflectionUtils {

    fun getGenericParameterClass(
        actualClass: Class<*>,
        genericClass: Class<*>,
        parameterIndex: Int
    ): Class<*> {
        if (!genericClass.isAssignableFrom(actualClass.superclass!!)) {
            throw IllegalArgumentException(
                "Class " + genericClass.name + " is not a superclass of " + actualClass.name + "."
            )
        }

        val genericClasses: Stack<ParameterizedType> = Stack<ParameterizedType>()

        var clazz = actualClass
        while (true) {
            val genericSuperclass: Type = clazz.genericSuperclass ?: continue
            val isParameterizedType = genericSuperclass is ParameterizedType
            if (isParameterizedType) {
                genericClasses.push(genericSuperclass as ParameterizedType)
            } else {
                genericClasses.clear()
            }
            val rawType: Type =
                if (isParameterizedType) (genericSuperclass as ParameterizedType).rawType else genericSuperclass
            if (rawType != genericClass) {
                clazz = clazz.superclass!!
            } else {
                break
            }
        }

        var result: Type = genericClasses.pop().actualTypeArguments[parameterIndex]
        while (result is TypeVariable<*> && !genericClasses.empty()) {
            val actualArgumentIndex = getParameterTypeDeclarationIndex(result)
            val type: ParameterizedType = genericClasses.pop()
            result = type.actualTypeArguments[actualArgumentIndex]
        }
        if (result is TypeVariable<*>) {
            throw IllegalStateException(
                ("Unable to resolve type variable " + result + "."
                        + " Try to replace instances of parametrized class with its non-parameterized subtype.")
            )
        }
        if (result is ParameterizedType) {
            result = result.rawType
        }
        if (result !is Class<*>) {
            throw IllegalStateException("Actual parameter type for " + actualClass.name + " is not a Class.")
        }
        return result
    }

    fun getParameterTypeDeclarationIndex(typeVariable: TypeVariable<*>): Int {
        val genericDeclaration: GenericDeclaration = typeVariable.genericDeclaration

        val typeVariables: Array<TypeVariable<*>> = genericDeclaration.typeParameters
        var actualArgumentIndex: Int? = null
        for (i in typeVariables.indices) {
            if (typeVariables[i] == typeVariable) {
                actualArgumentIndex = i
                break
            }
        }
        return actualArgumentIndex
            ?: throw IllegalStateException(
                ("Argument " + typeVariable.toString() + " is not found in "
                        + genericDeclaration.toString() + ".")
            )
    }
}