package lexer

import java.lang.Exception

data class LinkedList<V>(val key: String, val value: V, var next: LinkedList<V>? = null)

class HashTable<V>(val key: Int = 10){
    val table: MutableList<LinkedList<V>?> = mutableListOf()

    init {
        for(i in 0..key){
            table.add(null)
        }
    }

    private fun hash(key: String): Int{
        return key[0].code % this.key
    }

    fun get(key: String): V?{
        val code = hash(key)
        var it = table[code]
        while(it?.key != null && it.key != key){
            it = it.next
        }
        return it?.value
    }

    fun put(key: String, value: V){
        if(get(key) != null){
            throw Exception("Element already exists")
        }
        val code = hash(key)
        var it = table[code]
        if(it == null){
            table[code] = LinkedList(key, value)
            return
        }
        while (it?.next != null){
            it = it.next
        }
        it?.next = LinkedList(key, value)
    }

    fun print(){
        table.forEach {
            var iter = it
            while(iter != null){
                println(iter.key + " : " + iter.value)
                iter = iter.next
            }
        }
    }
}