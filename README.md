# Mambo Is Me
Voodoo themed box-pushing puzzle game similar to 'Baba Is You'. The main character is Mambo, a mighty voodoo priestess, with the power to manipulate the rules of the game. Each level comes with a set of rules made up of movable word blocks, corresponding to specific types of objects and object properties. By rearranging the rules you can influence how objects interact with each other and transform objects into other objects.

For example, when words 'MAMBO is ME' are set next to each other, the Mambo character is controlled by you. However, if you change the rule to 'SKULL is ME', you will take control of the Skull objects. Some common object properties include PUSH to make objects pushable and SOLID to make them impassable. To complete a level you just need to reach any object defined as GOAL.

You will learn new rules and gain better understanding of the game logic as you progress in the game. If you ever get stuck, there is a hint for every level that you can choose to show. Many levels do not have one but several alternative solutions, and you can revisit an earlier level at any time. Solved levels are saved in a cookie in your Pokitto, so the game continues where you left off.

### Making New Levels

You can make your own levels by editing the .xml files in the maps/ directory and rebuilding the project in FemtoIDE. Levels are defined as a 14x11 map of ASCII symbols. The script in the scripts/xmlconv.js file automatically converts .xml files into binary data. All the ASCII symbols are explained in the script file.

### License
Source code is released under the MIT License.

Graphics by jpfli, CCBY 3.0\
Sounds by jpfli and from Freesound.org, CC0 1.0
