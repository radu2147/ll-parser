package lexer

import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Automat(var filename: String) {
    var states: MutableMap<String?, State> = mutableMapOf()
    var initialState: String? = null

    // fisier intrare:
    // linia_1=<stare> #(starea initiala)
    // linia_2+=stare " " valoare_stare_finala " " lista_tranzitii
    // valoare_stare_finala=0 | 1 (1 final 0 nu)
    // lista_tranzitii=stare " " caracter " " lista_tranzitii
    // lista_tranzitii=lista_tranzitii
    // stare=ID
    // caracter=[A-Za-z0-9_]


    fun readAutomatFile() {
        val scanner = Scanner(File(filename))
        if (scanner.hasNextLine()) initialState = scanner.nextLine()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val x = line.split(" ".toRegex()).toTypedArray()
            val stateName = x[0]
            val isFinal = x[1] == "1"
            val map = HashMap<Char, String>()
            var i = 2
            while (i < x.size) {
                map[x[i][0]] = x[i + 1]
                i += 2
            }
            val state = State(stateName, isFinal, map)
            states[stateName] = state
        }
    }

    fun readAutomat(){
        initialState = readLine()!!
        while (true){
            val line = readLine()
            if (line == null || line.isEmpty()){
                break
            }
            val x = line.split(" ".toRegex()).toTypedArray()
            val stateName = x[0]
            val isFinal = x[1] == "1"
            val map = HashMap<Char, String>()
            var i = 2
            while (i < x.size) {
                map[x[i][0]] = x[i + 1]
                i += 2
            }
            val state = State(stateName, isFinal, map)
            states[stateName] = state
        }
    }

    fun printDescription() {
        println("States:")
        for ((key) in states) {
            System.out.printf("%s ", key)
        }
        println("\nFinal states:")
        for ((key, value) in states) {
            if (value.isFinal) System.out.printf("%s ", key)
        }
        println("\nTransitions:")
        for ((key, value) in states) for (x in value.map) println("state " + key + " -> " + x.key + " -> state " + x.value)
        println("Alfabet:")
        val set = HashSet<Char>()
        for ((_, value) in states) for (x in value.map) set.add(x.key)
        for (c in set) System.out.printf("%c ", c)
        println()
    }

    fun checkSequence(string: String): Boolean {
        var stateNr = initialState
        val chars = string.toCharArray()
        for (aChar in chars) {
            stateNr = states[stateNr]?.map?.get(aChar)
            if (stateNr == null) return false
        }
        return states[stateNr]?.isFinal ?: false
    }

    fun longest(string: String): String {
        var longest = ""
        val chars = string.toCharArray()
        for(i in 0..chars.size - 2){
            val local = StringBuilder()
            var stateNr = initialState
            for(j in i..chars.size - 1){
                stateNr = states[stateNr]?.map?.get(chars[j])
                if (stateNr == null) break
                local.append(chars[j])
                if (states[stateNr]?.isFinal ?: false) break
            }
            if(local.toString().length > longest.length){
                longest = local.toString()
            }
        }
        return longest
    }

    init {
        readAutomatFile()
    }
}

class State(private val name: String, val isFinal: Boolean, val map: HashMap<Char, String>)