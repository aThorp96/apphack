Repo for our Rogue-like game for App Hack 9 (though the project name is meant to reference NetHack, another famous Rogue clone. The game procedurally generates dungeons and spawns around 15 goblins throught the map. The goal of the game is to complete as many maps as possible by killing all the goblins in a dungeon. 

To run:
* From IDE
    1. clone the repository.
    2. import the repository as a gradle project in your favorite IDE
    3. Run DesktopLauncher.java
    
* From command line
    1. Install gradle (if not previously installed)
    2. Clone repository
    3. From the project's root directory, run `gradele -q run`. 

To play:
1. Navigate with WASD.
2. Attack the goblins by clicking in their direction. **The sword animation is currently off by 90 degrees CCW**
3. Try and kill all the golbins in the map to proceed to the next map.
4. When you die or beat a level you will spawn again in a new map. If you kill all the goblins your score will perisist through the spawn.


    
