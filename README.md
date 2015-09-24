# zombiehouse


#### Image Credits
wood.png http://opengameart.org/node/9288<br>
hardwood.png http://opengameart.org/node/11950<br>
Player and Zombie sprite sheets http://gaurav.munjal.us/Universal-LPC-Spritesheet-Character-Generator/<br>
pillar.png http://opengameart.org/content/lpc-house-insides<br>
Fire graphics http://opengameart.org/content/animated-particle-effects-2

#### Todo
###### Interface
* Tile graphics
    * Exit
* Unit animated graphics
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
