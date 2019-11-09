package lishui.study

import org.junit.Assert
import org.junit.Before
import org.junit.Test

const val MAX_COUNT = 8
val USER_NAME_FIELD = "UserName"
class JunitKtTest {

    lateinit var lateInitValue: String

    @Before
    fun setUp(){
        lateInitValue = "water"
    }

    @Test
    fun testFirstKtDemo(){
        println("testFirstKtTest")
        Assert.assertEquals(4L,4L)

        val user = User("water", 18)
        println("user: "+user.toString())
        val compare = user.component1().compareTo("water")
        Assert.assertEquals(0, compare)

        val userList = listOf(User("u1", 18))
        userList.plus(User("u2", 19))
        userList.stream().forEach { println(it.toString()) }

        for (x in 10 downTo 1){
            print("$x ")
        }

        println()
        val testStr:String = "hello"
        println("$testStr ${testStr.water} and ${testStr.newMethodForString()}")

        var a = 520
        var b = 1314
        a = b.also { print("use also expr value: $a ") }
        print("and after a = b, value a = $a ")

        println()
        val arrays = Array(5){ i -> i * i}
        arrays[4] = 7
        arrays.forEach { print("$it ")}

        println()
        val strList = mutableListOf<String>()
        strList.add("lin")
        strList.add("li")
        strList.add(2,"shui")
        for (str in strList){
            println("the $str[${strList.indexOf(str)}] and its length: ${str.length}")
        }

        println()
        val text = """
            |Tell me and I forget.
            |Teach me and I remember.
            |Involve me and I learn.
            |(Benjamin Franklin)
            """.trimMargin()
        println(text)
    }

    data class User(val name: String, val age: Int)

    fun String.newMethodForString() {
        print("String class new method! ")
    }
    val String.water: String
        get() = "water"

    @Test
    fun testSecondKtDemo(){
        val a = 3
        val b = 2
        val max = if (a > b) {
            print("Choose a")
            a
        } else {
            print("Choose b")
            b
        }

        println(" and max value = $max")
        Assert.assertEquals(true,a > b)

        val testWhen = hasPrefix("prefixString")
        println("the String has prefix substring: $testWhen")

        println()
        InitOrderDemo("hello")

        println()
        val u1 = User("u1", 18)
        val u2 = u1.copy()
        print("u1: $u1 and u2：$u2 \nu1 == u2: ${u1 == u2} and u1===u2: ${u1 === u2}")
    }

    fun hasPrefix(x: Any) = when(x) {
        is String -> x.startsWith("prefix")
        else -> false
    }

    class InitOrderDemo(name: String) {
        val firstProperty = "First property: $name".also(::println)

        init {
            println("First initializer block that prints ${name}")
        }

        val secondProperty = "Second property: ${name.length}".also(::println)

        init {
            println("Second initializer block that prints ${name.length}")
        }
    }

    open class Base{
        open fun x(){}
    }

    class A : Base(){
        override fun x() {
            super.x()
        }

        inner class Inner{
            fun g(){
                super@A.x()
            }
        }
    }

    companion object {
        val companionVal = "test companion"
    }

}