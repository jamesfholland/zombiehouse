# zombiehouse


#### Image Credits
wood.png http://opengameart.org/node/9288
hardwood.png http://opengameart.org/node/11950
Player and Zombie sprite sheets http://gaurav.munjal.us/Universal-LPC-Spritesheet-Character-Generator/
pillar.png http://opengameart.org/content/lpc-house-insides

#### Todo
###### Interface
* Tile graphics
    * Walls
    * Exit
* Unit animated graphics
    * Player
    * Zombie
    * Fire
        * Explosion
        * Fizzle out
        * Change surrounding tiles
* Score Bar
    * Current Level
    * Fire trap count
    * Zombies killed*
* Sound
    * Unit based sounds
        * Zombie
            * Shuffle
            * Wall collide
        * Player
            * Walk
            * Wall collide
            * Run
    * Fire trap
        * Explosion
        * Basic fire

###### Units
* Zombie
    * Movement
    * Pathfinding
    * Collisions
    * Zombie Master details
        * Detect if any zombie finds player
        * Other Special abilities
            * Fire starting?
            * Faster?
            * Never loses player location?
            * Spawns zombies?
* Player
    * Collisions
    * Firetrap use
        * Pickup
        * Placement
    * Death
* Fire Trap
    * Trigger
    * Spawning fire units.

###### House Generation
* Rooms
* Halls between rooms
* Zombie locations
* Pillars
* Exit
* Player start
    * Need to place away from exit

*Not required
