import java.lang.Integer.min
import java.util.*

/*
Пусть n - длина слова, |A| - мощность алфавита

Автокоррекция будет выполнена на основе сжатого префиксного дерева.

В качестве способа организации слов в каждом узле будем хранить хеш-таблицу.
Она позволит получить доступ к следующему узлу за ожидаемое время O(1)
 */
class Autocorrector {
    private class Node() {
        var kids = hashMapOf<Char, Node>()
        var isLeaf: Boolean = false
    }

    private val root = Node()
    private var matched = mutableListOf<String>()

    /*
    // длину общего префикса мы получаем за O(n)
    private fun getSharedLength(wordInTrie: String, wordToInsert: String): Int {
        val minLength = min(wordInTrie.length, wordToInsert.length)
        for (i in 0 until minLength) {
            if (wordInTrie[i] != wordToInsert[i]) return i
        }
        return minLength
    }
     */


    /*
    Благодаря использованию хеш-таблицы, вставка слова происходит за O(n)
     */
    fun addWordToDictionary(wordToAdd: String) {
        val word = wordToAdd.lowercase()
        var currentNode = root
        // currentWordIndex позволяет работать с суффиксом слова, избегая рассмотрения префикса
        var currentWordIndex = 0
        for (i in 0 until word.length) {
            val transitSymbol = word[i]
            if (currentNode.kids.contains(transitSymbol)) {
                currentNode = currentNode.kids[transitSymbol]!!
            } else {
                currentNode.kids[transitSymbol] = Node()
                currentNode = currentNode.kids[transitSymbol]!!
            }
        }
        currentNode.isLeaf = true
    }

    private fun search(wordToSearch: String): Boolean {
        var currentNode = root
        val word = wordToSearch.lowercase()
        for (transitSymbol in word) {
            if (currentNode.kids.contains(transitSymbol)) {
                currentNode = currentNode.kids[transitSymbol]!!
            } else {
                return false
            }
        }
        if (currentNode.isLeaf) return true
        return false
    }
    /* fun addWordToDictionary(wordToAdd: String) {
        val word = wordToAdd.lowercase()
        var currentNode = root
        if (currentNode.kids.isEmpty()) {
            currentNode.kids[word[0]] = Node(word, true)
            return
        }
        // currentWordIndex позволяет работать с суффиксом слова, избегая рассмотрения префикса
        var currentWordIndex = 0
        var transitSymbol: Char
        while (currentNode.kids.isNotEmpty()) {
            transitSymbol = word[currentWordIndex]
            if (currentNode.kids.contains(transitSymbol)) {
                // если мы нашли общий префикс (он по крайней мере длины 1)
                val prefixString = currentNode.string
                currentNode = currentNode.kids[transitSymbol]!!
                val sharedLength = getSharedLength(
                    currentNode.string,
                    word.substring(currentWordIndex, word.length)
                )
                if (sharedLength == currentNode.string.length) {
                    // если вставляемое слово оказалось длинней текущего узла
                    currentWordIndex += sharedLength
                } else {
                    val wordInTrie = currentNode.string
                    val tmpMap = currentNode.kids
                    // меняем строку текущего узла на общий префикс
                    currentNode.kids = hashMapOf()
                    currentNode.string = currentNode.string.substring(0, sharedLength)
                    currentNode.isLeaf = false

                    // вставляем в качестве потомков два разных суффикса
                    // суффикс слова, что уже было в дереве
                    currentNode.kids[wordInTrie[sharedLength]] =
                        Node(wordInTrie.substring(sharedLength, wordInTrie.length), true)
                    currentNode.kids[wordInTrie[sharedLength]]!!.kids = tmpMap

                    // суффикс нового слова
                    currentNode.kids[word[currentWordIndex + sharedLength]] =
                        Node(word.substring(currentWordIndex + sharedLength, word.length), true)
                    return
                }
            } else {
                currentNode.kids[word[currentWordIndex]] = Node(word.substring(currentWordIndex, word.length), true)
                return
            }
        }

}
     */
    // Эта реализация перебирает все ключи в каждом узле. Это плохо
    /*
    var currentWordIndex = 0
    while (currentNode.kids.isNotEmpty()) {
        var hasSharedSymbols = false
        for (key in currentNode.kids.keys) {
            val sharedSymbols = prefix(key, word, currentWordIndex)
            if (sharedSymbols != 0) {
                hasSharedSymbols = true
                when (sharedSymbols) {
                    key.length -> {
                        // если ключ является префиксом слова
                        if (key.length == word.length - currentWordIndex) {
                            // если ключ совпал со словом (без учёта префикса)
                            throw Exception("there is already a such word in dictionary")
                        }
                        currentNode = currentNode.kids[key]!!
                        currentWordIndex += sharedSymbols
                        break
                    }
                    else -> {
                        val prefix = word.substring(currentWordIndex, currentWordIndex + sharedSymbols)
                        val suffixOfWord = word.substring(currentWordIndex + sharedSymbols, word.length)
                        val suffixOfKey = key.substring(sharedSymbols, key.length)

                        val tmpNode = currentNode.kids[key]
                        currentNode.kids.remove(key)

                        if (suffixOfWord.isNotEmpty()) {
                            currentNode.kids[prefix] = Node(false)
                            currentNode.kids[prefix]!!.kids[suffixOfWord] = Node(true)
                        } else {
                            currentNode.kids[prefix] = Node(true)
                            currentNode.kids[prefix]!!.kids[suffixOfKey] = tmpNode!!
                        }
                        return
                    }
                }
            }
        }
        // Если мы не нашли общего префикса среди детей узла, добавляем нового ребёнка
        if (!hasSharedSymbols) {
            currentNode.kids[word.substring(currentWordIndex, word.length)] = Node(true)
        }
    }
     */
    fun check(wordToCheck: String): List<String> {
        matched.clear()
        var currentNode = root
        val word = wordToCheck.lowercase()
        //TODO change exc
        if (search(wordToCheck)) throw Exception()
        checkRecursive(root, word, 0, false, "")
        matched.sort()
        return matched
    }

    private fun checkRecursive(
        currentNode: Node,
        word: String,
        index: Int,
        hasMistakeInParent: Boolean,
        code: String
    ) {
        if(word == "dui") {
            1
        }

        if (index == word.length && currentNode.isLeaf) {
            matched.add(code)
            return
        }
        if (index == word.lastIndex && !hasMistakeInParent && currentNode.isLeaf) {
            matched.add(code)
        }
        if (index == word.length && !hasMistakeInParent && currentNode.kids.isNotEmpty()) {
            for ((key, node) in currentNode.kids) {
                if (node.isLeaf) {
                    matched.add(code + key)
                }
            }
            return
        }
        for ((key, node) in currentNode.kids) {
            if (index == word.length) {
                return
            }
            if (key != word[index]) {
                if (index < word.lastIndex && key == word[index + 1]) {
                    // ошибка вставки лишнего символа
                    if (hasMistakeInParent) continue
                    checkRecursive(currentNode, word, index + 1, true, code)
                }
                if (index < word.lastIndex && key == word[index + 1] && node.kids.contains(word[index])) {
                    // ошибка перестановки
                    if (hasMistakeInParent) continue
                    checkRecursive(node.kids[word[index]]!!, word, index + 2, true, code + key + word[index])
                }
                if (node.kids.contains(word[index])) {
                    // ошибка пропуск символа
                    if (hasMistakeInParent) continue
                    checkRecursive(node, word, index, true, code + key)
                }
                if (key != word[index]) {
                    // ошибка неправильного символа
                    if (hasMistakeInParent) continue
                    checkRecursive(node, word, index + 1, true, code + key)
                }
            } else {
                checkRecursive(node, word, index + 1, hasMistakeInParent, code + key)
            }
        }
    }
/*
fun checkWord(wordToCheck: String) {
        val word = wordToCheck.lowercase()
        val matched = mutableListOf<String>()
        for (node in root.kids.values) {
            recursiveCheck(node, word, 0, false, 0.toChar(), -1)
        }
    }

    private fun recursiveCheck (
        node: Node,
        word: String,
        currentWordIndex: Int,
        hasMistakeInParent: Boolean,
        lastLetterErrorSymbolInParent: Char,
        lastLetterErrorIndexInParent: Int
    ) {
        val prefix = node.string
        val minLen = min(prefix.length, word.length - currentWordIndex)
        var hasMistake = hasMistakeInParent
        var lastLetterErrorIndex = lastLetterErrorIndexInParent
        var lastLetterErrorSymbol = lastLetterErrorSymbolInParent
        var i = 0 // для индексации узла
        var j = currentWordIndex // для индексации проверяемого слова

        if (prefix.length - (word.length - currentWordIndex) > 1) {
            // в случае если префикс длиннее подслова более чем на один символ
            return
        }
        if (prefix.length - (word.length - currentWordIndex) == 1) {
            // в случае если префикс длинне подслова на один символ
            if (hasMistake || !node.isLeaf) {
                return
            } else {
                hasMistake = true
            }
        }
        if (lastLetterErrorIndex == currentWordIndex - 1) {
            // проверка на ошибку перестановки с прошлого префикса
            if (prefix[0] == word[currentWordIndex - 1] && word[currentWordIndex] == lastLetterErrorSymbol) {
                i = 1
            } else {
                // todo fix return
                return
            }
        }
        while (i != minLen) {
            // TODO fix next
            if (prefix[i] != word[currentWordIndex + i]) {
                if (lastLetterErrorIndex == j - 1) {
                    // ошибка в виде перестановки двух подряд идущих символов
                    if (prefix[i - 1] == word[j] && prefix[i] == word[j - 1]) {
                        i++
                        j++
                    } else {
                        return
                    }
                } else if (prefix[i] == word[j + 1]) {
                    // ошибка вставки
                    if (hasMistake) return
                    hasMistake = true
                    i += 2
                    j++
                } else if (prefix[i + 1] == word[j]) {
                    if (hasMistake) return
                    hasMistake = true
                    i++
                    j += 2
                } else {
                    // ошибка неправильной буквы
                    if (hasMistake) return
                    hasMistake = true
                    lastLetterErrorSymbol = prefix[i]
                    lastLetterErrorIndex = j
                    i++
                    j++
                }
            }
        }
        if (prefix.length == minLen) {
            for (node in node.kids.values) {
                recursiveCheck(
                    node,
                    word,
                    currentWordIndex + j,
                    hasMistake,
                    lastLetterErrorSymbol,
                    lastLetterErrorIndex
                )
            }
        }
    }
 */
}


fun main() {
    val scanner = Scanner(System.`in`)
    val autocorrector = Autocorrector()
    autocorrector.addWordToDictionary("hel")
    autocorrector.addWordToDictionary("hep")
    val list = autocorrector.check("ohel")
    for (elem in list) {
        println(elem)
    }
    /*
    run {
        val autocorrector = Autocorrector()
        autocorrector.addWordToDictionary("Весна")
        autocorrector.addWordToDictionary("Вестибулярный")
        autocorrector.addWordToDictionary("Вестибюль")
        autocorrector.addWordToDictionary("Вестник")
        autocorrector.addWordToDictionary("Весть")
        autocorrector.addWordToDictionary("Видение")
        autocorrector.addWordToDictionary("Видеозал")
        autocorrector.addWordToDictionary("Видеозапись")
    }
    */

    /*
    while (scanner.hasNext()) {
        val amountOfWords = scanner.nextInt()
        for (i in 1..amountOfWords) {
            autocorrector.addWordToDictionary(scanner.next())
        }
    }
    */
}