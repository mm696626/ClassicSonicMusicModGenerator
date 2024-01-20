# ClassicSonicMusicModGenerator

### Classic Sonic Music Mod Generator
* A tool to generate music mods for the fan-made Sonic 1, 2, and 3 remasters, and the decomp of Sonic CD (2011) 
    * Link to Sonic 1 Forever: https://teamforeveronline.wixsite.com/home/sonic-1-forever
    * Link to Sonic 2 Absolute: https://teamforeveronline.wixsite.com/home/sonic-2-absolute
    * Link to Sonic CD Decompilation: https://github.com/Rubberduckycooly/Sonic-CD-11-Decompilation
    * Link to Sonic 3 AIR: https://sonic3air.org/

### Setup to get this tool working
* Have Sonic 1 Forever, Sonic 2 Absolute, Sonic CD (2011), or Sonic 3 AIR installed and working
* Open the tool (you'll need the latest JDK installed for that)
    * Link to JDK: https://www.oracle.com/java/technologies/downloads/
* Press Pick Music for Mod and then the tool will prompt for a folder to look for ogg files
    * It will look through subfolders as well
* Pick your options and press Save Music Choices
* Press Generate Mod INI (Generate Mod JSON for 3 AIR) and fill out the fields to create your mod.ini/mod.json file
* Once you have picked your music and generated your mod.ini/mod.json file, just press Generate Mod Folder
* Move that newly created folder into your mods folder
* Happy Listening!

### Limitations
* The loop points are not automated (will just replay the audio file on loop)
* No adding slots to the game as this tool will just generate a mod to replace songs
* For Sonic CD, the FMV cutscenes can't have their music replaced since the music is within the video file itself