package com.merseyside.utils.reflection

import java.lang.reflect.GenericDeclaration
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.*

object ReflectionUtils {
    /**
     * Для некоторого класса определяет каким классом был параметризован один из его предков с generic-параметрами.
     *
     * @param actualClass   анализируемый класс
     * @param genericClass  класс, для которого определяется значение параметра
     * @param parameterIndex номер параметра
     * @return        класс, являющийся параметром с индексом parameterIndex в genericClass
     */
    fun getGenericParameterClass(
        actualClass: Class<*>,
        genericClass: Class<*>,
        parameterIndex: Int
    ): Class<*> {
        // Прекращаем работу если genericClass не является предком actualClass.
        if (!genericClass.isAssignableFrom(actualClass.superclass!!)) {
            throw IllegalArgumentException(
                "Class " + genericClass.name + " is not a superclass of "
                        + actualClass.name + "."
            )
        }

        // Нам нужно найти класс, для которого непосредственным родителем будет genericClass.
        // Мы будем подниматься вверх по иерархии, пока не найдем интересующий нас класс.
        // В процессе поднятия мы будем сохранять в genericClasses все классы - они нам понадобятся при спуске вниз.

        // Проейденные классы - используются для спуска вниз.
        val genericClasses: Stack<ParameterizedType> = Stack<ParameterizedType>()

        // clazz - текущий рассматриваемый класс
        var clazz = actualClass
        while (true) {
            val genericSuperclass: Type = clazz.genericSuperclass ?: continue
            val isParameterizedType = genericSuperclass is ParameterizedType
            if (isParameterizedType) {
                // Если предок - параметризованный класс, то запоминаем его - возможно он пригодится при спуске вниз.
                genericClasses.push(genericSuperclass as ParameterizedType)
            } else {
                // В иерархии встретился непараметризованный класс. Все ранее сохраненные параметризованные классы будут бесполезны.
                genericClasses.clear()
            }
            // Проверяем, дошли мы до нужного предка или нет.
            val rawType: Type =
                if (isParameterizedType) (genericSuperclass as ParameterizedType).rawType else genericSuperclass
            if (rawType != genericClass) {
                // genericClass не является непосредственным родителем для текущего класса.
                // Поднимаемся по иерархии дальше.
                clazz = clazz.superclass!!
            } else {
                // Мы поднялись до нужного класса. Останавливаемся.
                break
            }
        }

        // Нужный класс найден. Теперь мы можем узнать, какими типами он параметризован.
        var result: Type = genericClasses.pop().actualTypeArguments[parameterIndex]
        while (result is TypeVariable<*> && !genericClasses.empty()) {
            // Похоже наш параметр задан где-то ниже по иерархии, спускаемся вниз.

            // Получаем индекс параметра в том классе, в котором он задан.
            val actualArgumentIndex = getParameterTypeDeclarationIndex(result)
            // Берем соответствующий класс, содержащий метаинформацию о нашем параметре.
            val type: ParameterizedType = genericClasses.pop()
            // Получаем информацию о значении параметра.
            result = type.actualTypeArguments.get(actualArgumentIndex)
        }
        if (result is TypeVariable<*>) {
            // Мы спустились до самого низа, но даже там нужный параметр не имеет явного задания.
            // Следовательно из-за "Type erasure" узнать класс для параметра невозможно.
            throw IllegalStateException(
                ("Unable to resolve type variable " + result + "."
                        + " Try to replace instances of parametrized class with its non-parameterized subtype.")
            )
        }
        if (result is ParameterizedType) {
            // Сам параметр оказался параметризованным.
            // Отбросим информацию о его параметрах, она нам не нужна.
            result = result.rawType
        }
        if (result !is Class<*>) {
            // Похоже, что параметр - массив или что-то еще, что не является классом.
            throw IllegalStateException("Actual parameter type for " + actualClass.name + " is not a Class.")
        }
        return result
    }

    fun getParameterTypeDeclarationIndex(typeVariable: TypeVariable<*>): Int {
        val genericDeclaration: GenericDeclaration = typeVariable.genericDeclaration

        // Ищем наш параметр среди всех параметров того класса, где определен нужный нам параметр.
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