// Define some animal classes.
abstract class Animal {
    def speak()
}

// Normal zoo animals.
class Elephant extends Animal { def speak() = println("Toot!") }
class Lion extends Animal { def speak() = println("Roar!") }
class Orangutan extends Animal { def speak() = println("Ook!") }

// Petting zoo animals.
class Chicken extends Animal { def speak() = println("Bawk.") }
class Sheep extends Animal { def speak() = println("Baaa.") }
class TrashSquirrel extends Animal { def speak() = println("Pizza.") }


/// Now let's make our zoo.
class Zoo {

    // A coupled method that has to be overwritten entirely in subclasses.
    def tourZooCoupled() = {
        val animals: List[Animal] = List(
            new Orangutan,
            new Lion(),
            new Elephant(),
        )
        sayHelloToAll(animals)
    }

    // A method that uses the factory pattern. Put animal creation in makeSizeAnimal() methods.
    def tour() = {
        println("\n*** Touring the zoo...")
        val animals: List[Animal] = List(
            makeSmallAnimal(),
            makeMidAnimal(),
            makeBigAnimal(),
        )
        sayHelloToAll(animals)
    }

    // The factory methods.
    // The Animal type annotations are required, otherwise Scala's compiler
    // will infer the most constrained type (i.e. Orangutan).
    def makeSmallAnimal(): Animal = new Orangutan()
    def makeMidAnimal(): Animal = new Lion()
    def makeBigAnimal(): Animal = new Elephant()

    def sayHelloToAll(animals: List[Animal]): Unit = {
        animals match {
            case animal :: rest => {
                animal.speak()
                sayHelloToAll(rest)
            }
            // Note: equivalent to Nil => () in Rust.
            case Nil =>
        }
    }
}


class PettingZoo extends Zoo {
    // Now, all we have to do the covert the zoo is override the factory methods.
    override def makeSmallAnimal() = new TrashSquirrel()
    override def makeMidAnimal() = new Chicken()
    override def makeBigAnimal() = new Sheep()
}


val zoo = new Zoo()
zoo.tour()

val pettingZoo = new PettingZoo()
pettingZoo.tour()
