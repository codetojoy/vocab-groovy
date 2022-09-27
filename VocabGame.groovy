
final def random = new Random()
final String TEST = "TEST"
final int MODE_WORDS = 0

def readDictionary = { filename ->
    def dict = [:]
    new File(filename).eachLine { line ->
        def tokens = line.split(':')
        
        if (tokens.length == 2) {
            def key = tokens[0].trim()
            def val = tokens[1].trim()
            dict[key] = val
        }
    }

    return dict
}

// given x,y return i in [x,y]
def getRandom = { min, max ->
    random.nextInt(min, max + 1)
}

def flipCoin = {
    getRandom(0,1)
}

// test flipCoin
def headCount = 0
def tailCount = 0
100.times { 
    def result = flipCoin()
    assert result >= 0 && result <= 1
    if (result) {
        headCount++
    } else {
        tailCount++
    }
}
println "TRACER testFlipCoin h: ${headCount} t: ${tailCount}"

def rollDice = {
    getRandom(1, 12)
}

// test rollDice
def rollDiceResult = new StringBuilder()
10.times { 
    def x = rollDice()
    assert x >= 1 && x <= 12
    rollDiceResult += " " + x
}
println "TRACER testRollDice r: ${rollDiceResult}"

def detectInput = { message ->
    println message
    def scanner = new java.util.Scanner(System.in)
    def response = scanner.next()
}

LinkedHashMap.metaClass.safeGet = { target ->
    delegate[target] ?: delegate.find{k,v -> v == target}.key
}

def turn = { dictionary, items ->
    rollDice().times { Collections.shuffle items }
    def item = items[0]
    def message = new StringBuffer("Q: what is '${item}' ? \n")
    
    def hints = items[0..4]
    rollDice().times { Collections.shuffle hints }
    hints.each { hint ->
        message.append " - ${dictionary.safeGet(hint)}\n" 
    }
        
    detectInput message.toString()
    println "A: ${dictionary.safeGet(item)}"
}

def playGame = { dictionary, mode ->
    if (MODE_WORDS == mode) {
        def words = dictionary.keySet() as List
        turn(dictionary, words)
    } else {
        def definitions = dictionary.values() as List
        turn(dictionary, definitions)
    }
}

// ---------- main

def file = args[0]
if (file == TEST) { System.exit(0) }
assert new File(file).exists()

def dictionary = readDictionary(file)

def numGames = 6

numGames.times { 
    def mode = flipCoin()
    playGame(dictionary, mode)
}

