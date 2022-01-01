package lexer

import java.io.File
import java.io.InputStream

class Lexer {

    val ts: HashTable<Int> = HashTable()
    val fip: MutableList<Pair<Int?, Int?>> = mutableListOf()

    val automatId: Automat = Automat("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\lftc\\limbaje\\src\\id.txt")
    val automatInt: Automat = Automat("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\lftc\\limbaje\\src\\constant.txt")
    val automatFloat: Automat = Automat("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\lftc\\limbaje\\src\\float.txt")

    private var start = 0

    fun isId(elem: String): Boolean {
        return automatId.checkSequence(elem)
    }

    fun isConstant(elem: String): Boolean {
        return automatInt.checkSequence(elem) || automatFloat.checkSequence(elem)
    }

    fun isKeyword(elem: String): Boolean {
        return elem == "cout" || elem == "if" || elem == "else" || elem == "cin" || elem == "while" || elem == "int" || elem == "float" || elem == "struct"
    }

    fun isSeparator(elem: String): Boolean{
        return "({}),;".contains(elem)
    }

    fun isOperator(elem: String): Boolean{
        return elem == "!=" || elem == ">" || elem == "=" || elem == "<" || elem == "+" || elem == "-" || elem == "==" || elem == "<<" || elem == "!" || elem == ">>"
    }

    fun isOperatorRegex(elem: String): Boolean{
        return elem.matches(Regex("([+]|[-]|[*]|[>]|[<]|[!]|[=])+"))
    }


    fun analyzeWithoutSpaces(filename: String): Map<String, Int> {
        var index = 0
        var errors = ""
        var lineCount = 0
        val table = mutableMapOf<String, Int>()
        val inputStream: InputStream = File(filename).inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }.split("\n").map { it.trim() }
        var current = ""
        inputString.forEach { line ->
            lineCount ++

            line.toCharArray().forEach {
                if (isAppendable(current, it)) {
                    current += it
                } else {
                    if (isKeyword(current)) {
                        if (!table.contains(current)) {
                            table[current] = index++
                        }
                        fip.add(Pair<Int?, Int?>(table[current], null))
                    } else if (isOperator(current)) {
                        if (!table.contains(current)) {
                            table[current] = index++
                        }
                        fip.add(Pair<Int?, Int?>(table[current], null))
                    } else if (isId(current)) {
                        if(current.length > 254){
                            errors += "Identifier too long at line ${lineCount}"
                        }
                        else {
                            if (!table.contains("ID")) {
                                table["ID"] = index++
                            }
                            val elem = ts.get(current)
                            if (elem == null) {
                                ts.put(current, ++start)
                                fip.add(Pair<Int?, Int?>(table["ID"], start))
                            } else {
                                fip.add(Pair<Int?, Int?>(table["ID"], elem))
                            }
                        }
                    } else if (isConstant(current)) {
                        if (!table.contains("CONST")) {
                            table["CONST"] = index++
                        }
                        val elem = ts.get(current)
                        if(elem == null) {
                            ts.put(current, ++start)
                            fip.add(Pair<Int?, Int?>(table["CONST"], start))
                        }
                        else{
                            fip.add(Pair<Int?, Int?>(table["CONST"],elem))
                        }
                    } else if(isSeparator(current)){
                        if (!table.contains(current)) {
                            table[current] = index++
                        }
                        fip.add(Pair<Int?, Int?>(table[current], null))

                    }
                    else{
                        errors += "Unidentified token ${current} at line ${lineCount}\n"
                    }
                    current = "" + it.let { if (it == ' ') "" else it }
                }

            }
        }
        if(errors.length > 0){
            throw Exception(errors)
        }
        return table
    }

    private fun isAppendable(current: String, it: Char): Boolean {
        if(current == "")
            return true
        if (it.toString() == " ")
            return false
        if (isOperatorRegex(it.toString())) {
            if(!isOperatorRegex(current))
                return false
        }
        if(isOperatorRegex(current)){
            if(!isOperatorRegex(it.toString()))
                return false
        }
        if (isSeparator(it.toString()) || isSeparator(current)) {
            return false
        }
        return true
    }

}