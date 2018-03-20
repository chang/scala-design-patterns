// We're creating a maze game. Here are some room definitions with different levels of difficulty (Safe, Normal, Boss),
// all inheriting from the abstract class Room.
abstract class Room(val numEnemies: Int) {
    def passThroughRoom(): Unit

    // A default method implementation.
    def showEnemies() {
        println(s"This room has $numEnemies enemies.")
    }
}

class SafeRoom extends Room(0) {
    // Note: this override keyword isn't necessary in Scala, since we're implementing an abstract class.
    override def passThroughRoom() = {
        println(s"This room is safe. ($numEnemies enemies)")
    }
}

class NormalRoom extends Room(3) {
    def passThroughRoom() = {
        println(s"This room is normal. ($numEnemies enemies)")
    }
}

class BossRoom extends Room(10) {
    def passThroughRoom() = {
        println(s"This room is super dangerous. ($numEnemies enemies)")
    }
}

// Now we implement a 3 room game using the above classes.

/*
 * Note:
 * There're definitely better ways to do this. We could have the constructor take a difficulty argument,
 * and store the rooms as a Vector[Room] as a member. But this just demos the advantages of the factory pattern,
 * if we wanted to have different types/difficulties/styles of games subclass MazeGame.
 */

class MazeGame {

    def startMessage() = { println("\n* Starting a MazeGame *") }

    def playGameCoupled() = {
        // Naming the subclasses explicitly makes this game impossible to change at runtime.
        // What if we wanted to set an easier or harder difficulty?
        val startRoom = new NormalRoom()
        val middleRoom = new NormalRoom()
        val bossRoom = new NormalRoom()

        startRoom.passThroughRoom()
        middleRoom.passThroughRoom()
        bossRoom.passThroughRoom()
    }

    // Now let's try creating rooms using a factory method.
    // This seems like it gets the same result, but looks at what happens when we try to create
    // a different version of the game...
    def play_game_factory() = {
        startMessage()

        val startRoom = makeRoom_factory()
        val middleRoom = makeRoom_factory()
        val bossRoom = makeRoom_factory()

        startRoom.passThroughRoom()
        middleRoom.passThroughRoom()
        bossRoom.passThroughRoom()
    }

    def makeRoom_factory(): Room = {
        new NormalRoom()
    }
}

class HardMazeGame extends MazeGame {
    // Dayum. All we needed to do was override the factory method.
    // To do this using playGameCoupled(), we would've needed to overwrite the whole method,
    // including stuff that doesn't even have to do with room creation!
    override def makeRoom_factory(): Room = {
        new BossRoom()
    }

    override def startMessage() = { println("\n* Starting a Hard MazeGame *") }
}

class VariedMazeGame extends MazeGame {
    // Another fancier example.
    var n = 0

    override def makeRoom_factory(): Room = {
        n += 1
        n match {
            case 1 => new SafeRoom()
            case 2 => new NormalRoom()
            case _ => new BossRoom()
        }
    }

    override def startMessage() = { println("\n* Starting a Varied MazeGame *") }
}


val game = new MazeGame()
game.play_game_factory()

val hard_game = new HardMazeGame()
hard_game.play_game_factory()

val varied_game = new VariedMazeGame()
varied_game.play_game_factory()
