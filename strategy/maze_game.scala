import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/* We already have 3 different types of room: Safe, Normal, and Boss.
 * What if we wanted to add special room behavior to our game? Here are some possible room behaviors:
 *
 * Treasure Room: Upon entering, you get treasure.
 * Trap Room: Upon entering, it springs a trap.
 *
 */

/* Some bad approaches:
 *
 * 1. Add a specialBehavior() method to Room, and overwrite in "special" subclasses:
 * Not all rooms have special behaviors. If the default implementation is "special",
 * you'd have to override it for all non-special rooms.
 *
 * 2. Have special room classes implement a SpecialBehavior trait/interface.
 * This solves the problem of having to override a default implementation. But there's
 * no code reuse, since traits can't have implementations. Then when you have to change
 * the treasure from coins to gems in the 40 different room subclasses, you'll have a bad time.
 *
 * These approaches would also result in many superfluous subclasses: SafeRoom, SafeTreasureRoom, SafeBonusRoom, etc...
 *
 * How could we change the behavior of an existing room at runtime? Might be able to do it with the factory pattern,
 * but then you'd be replacing the whole class. What if the room has state (a score, number of enemies) that needs to be saved?
 */

// Strategy Pattern: delegate the special behavior to another class, and make that class a member of the room.

// The behavior classes implement the SpecialBehavior trait/interface.
trait SpecialBehavior {
    def activateSpecial(): Unit
}

class Treasure extends SpecialBehavior {
    def activateSpecial() = println("Treasure: Found some jewels.")
}

class Trap extends SpecialBehavior {
    def activateSpecial() = println("Trap: Fell into a spike pit!")
}

class Normal extends SpecialBehavior {
    def activateSpecial() = {}  // Do nothing.
}


// (Is it better to make a setter method, provide a default constructor, or overload the constructor?)
// Note: Idiomatic Scala is to make the member directly in the constructor - Room(val special: specialBehavior)
// which is a pretty Scala-specific feature, but there's no better way to do it.

// Make SpecialBehavior a member of Room.
abstract class Room(var special: SpecialBehavior) {

    // A setter allowing us to change the room behavior at runtime.
    def setSpecial(special: SpecialBehavior) = {
        this.special = special
    }

    def roomEvent(): Unit

    // Now the public method passThrough() relies on the special behavior class.
    def passThrough() = {
        roomEvent()
        special.activateSpecial()
    }
}

// No var keyword in these constructors, since that would be overriding the member Room.special.
class SafeRoom(special: SpecialBehavior = new Normal()) extends Room(special) {
    def roomEvent() = println("Safely passing through.")
}

class NormalRoom(special: SpecialBehavior = new Normal()) extends Room(special) {
    def roomEvent() = println("Encountered an enemy.")
}

class BossRoom(special: SpecialBehavior = new Normal()) extends Room(special) {
    def roomEvent() = println("Fighting a boss!")
}

class MazeGame {
    def play() = {
        println("* Playing maze game...")

        val map: ArrayBuffer[Room] = ArrayBuffer(
            new SafeRoom(),
            new NormalRoom(),
            new BossRoom(),
            // Treasure after the boss fight.
            new SafeRoom(new Treasure())
        )

        for (room <- map) {
            room.passThrough()
        }
    }
}

/* As an aside, now if we combine the factory and strategy patterns, we can
 * vary room creation and room behavior very flexibly.
 */

 class BetterMazeGame {
     def play() = {
        println("* Playing a better maze game...")

        // Create map. Done properly, this should be its own method, and
        // use the factory pattern to create the various rooms.
        val map: ArrayBuffer[Room] = ArrayBuffer()
        for (i <- 0 to 10) {

            if (i == 0)
                map += new SafeRoom()
            else if (i == 10)
                map += new BossRoom()
            // Every 5th room has treasure.
            else if (i % 5 == 0)
                map += new NormalRoom(new Treasure())
            else
                map += new NormalRoom()
        }

        val rng = new Random()
        for (room <- map) {

            // The strategy pattern allows us to change behavior at runtime.
            val rn = rng.nextFloat()

            if (rn < 0.3) {
                println("** What terrible luck...")
                room.setSpecial(new Trap())
            } else if (rn > 0.8) {
                println("** What great luck!")
                room.setSpecial(new Treasure())
            }

            room.passThrough()
        }
     }
 }


val game = new MazeGame()
game.play()

val betterGame = new BetterMazeGame()
betterGame.play()
