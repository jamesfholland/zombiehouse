CS351 DESIGN OF LARGE PROGRAMS
GROUP PROJECT 1:

ZOMBIE HOUSE

By
James Holland
Rob Doyle
Julian Weisburd

Entry Point: Main.java

Zombie House is an overhead 2D survival game. The player is put into a dark house full of zombies and can only see a short distance.
The goal of each level is to find exit before zombies eat you.

CONTROLS:
W, UP ARROW -  Move up
A, LEFT ARROW - Move left
S, DOWN ARROW - Move down
D, RIGHT ARROW - Move down

R - If the player has stamina available, make the move at twice the walking speed
P - If player is standing over a firetrap, pick up the fire trap. If the player has a firetrap in their inventory and not standing over a firetrap, place a firetrap on a tile

PLAYER:
The unit the user controls. The player moves around the map searching for the exit and trying to avoid zombies. If a zombie catches the player, the level will reset to its original state.
By default, the player has 5 seconds of stamina. The player can run at twice their walk speed while holding down the R key and while stamina is available.
Stamina regenerates at 0.2 seconds/second.
Be careful! If you pass through a firetrap while running you will set it off and die!

OBSTACLE:
Random desks and plants will spawn in the zombie house to hinder your progress through the house.

ZOMBIES:
Zombies make a decision of what to do every 2 seconds by default.
If a zombie can smell the player, the zombie knows the shortest path to the player and walks towards them. Only knows the player’s location as of the last 2 second update.

ZOMBIE TYPES:
Line Zombie - If this zombie cannot smell the player, then he moves in a straight line until he walks into a wall.
If he walks into a wall, a new random heading will be determined.

Random Zombie - If this zombie cannot smell the player, then he moves in a random direction. Each decision update, a Random Zombie tries walking with a new random direction.
If this zombie walks into a wall, then a new heading will be calculated in the exact opposite direction of the way he was trying to go. 

Master Zombie - A Random Zombie with a few extra abilities. This zombie does not set off fire traps. This zombie is immune to fire. This zombie spawns on the exit of every level.
If any zombie on the map can smell the player, then the Master Zombie knows the exact location of the player and will walk towards them. Only one Master Zombie spawns per level.

TOOLS:
Firetrap - A trap which triggers if a player runs or a zombie moves through it. Firetraps will spawn fire in the 9 tiles around the firetrap, including the tile the firetrap is placed in.
If a player or zombie hits a fire, the player will die and the level will reset and the zombie will also be killed. Fire lasts 15 seconds. Beware! Smoke will still kill you! 
If an obstacle is caught in the blaze then it will be destroyed at the end of the burn.

SETTINGS:
A player can change the default settings when starting a new game of Zombie House.





WHO DID WHAT

James Holland:
View, Graphics

Rob Doyle:
House Generation

Julian Weisburd:
Units, Keyboard Input

Everyone:
Controller

#### Image Credits
wood.png http://opengameart.org/node/9288<br>
hardwood.png http://opengameart.org/node/11950<br>
Player and Zombie sprite sheets http://gaurav.munjal.us/Universal-LPC-Spritesheet-Character-Generator/<br>
pillar.png http://opengameart.org/content/lpc-house-insides<br>
Fire graphics http://opengameart.org/content/animated-particle-effects-2
Fire Trap modified version of pillar.png

#### Audio credits
Creaks and Bumps http://opengameart.org/content/25-spooky-sound-effects
footstep.wav https://www.freesound.org/people/OwlStorm/sounds/151232/
Fire http://opengameart.org/content/fire-crackling
Basic fire http://www.freesound.org/people/tc630/sounds/47835/

###### Bugs
* Fires need set off fire traps
* Game starts to lag after playing for ~10 minutes (yes I played that long) Probably a memory leak somewhere.
* If said lag occurs the player may fly off the map and crash the game.
* Other zombies can pass through the Master Zombie. Probably a feature actually.

*Not required
