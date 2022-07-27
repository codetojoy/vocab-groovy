
def readDictionary = { filename ->
    def map = [:]
    new File(filename).eachLine { line ->
        def tokens = line.split(':')
        
        if (tokens.length == 2) {
            def key = tokens[0].trim()
            def val = tokens[1].trim()
            map[key] = val
        }
    }

    return map
}

def flipCoin = {
    def time = System.currentTimeMillis() as String
    def pick = time[-2] as int
    ((pick % 2) == 0) ? 0 : 1
}

def rollDice = {
    def time = System.currentTimeMillis() as String
    def result = (time[-1] as int) + 2
    println "TRACER rollDice : ${result}"
    return result
}

def getInput = { message ->
    def ok = false
    def scanner = new java.util.Scanner(System.in)
    
    while (!ok) {
        println message
        String response = scanner.next()
        ok = true
    }
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
        
    getInput message.toString()
    println "A: ${dictionary.safeGet(item)}"
}

def playGame = { dictionary, coinFlip ->
    if (0 == (coinFlip as int)) {
        def words = dictionary.keySet() as List
        turn(dictionary, words)
    } else {
        def definitions = dictionary.values() as List
        turn(dictionary, definitions)
    }
}

// ---------- main

def file = args[0]
assert new File(file).exists()

def dictionary = readDictionary(file)

/*
def x = flipCoin()

if (args != null && args.length != 0) {
    x = args[0]
} 
*/ 

def numGames = 6

numGames.times { 
    def x = flipCoin()
    playGame(dictionary, x)
}

// try { Thread.sleep(800) } catch (Exception ex) {} 


