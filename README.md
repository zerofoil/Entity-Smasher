## Entity Smasher

**Entity Smasher** is a fun utility plugin made for fun to give players access to smash entities at ground, with fall damage. This plugin can also be used instead of `Carry On` mod. 
The plugin is simple to use and comes with a config file full of options so you can tweak behavior, and restrictions to your needs.
With right click, you can grab (almost) any entity, move with your cursor to reposition it, and place it where you want. Right click any block to deselect, or use command.

![](https://github.com/zerofoil/Entity-Smasher/blob/main/show.gif?raw=true)

Can be really useful when manipulating with monsters or animals, to keep them in cages, or just smash them for fun :)

**This plugin has not been tested as much. If a config option is not provided in a correct way, error may occur.**

---

### Features
- Grab and move entities with your mouse
- Simple select system - right click to select, right click on block to deselect.
- Highly configurable
- Works with most entity types
- Keep config message empty to not send it
- Use scroll wheel to change entity distance
- Permissions

---

### Permissions
- entitysmasher.use - Allows picking up entities
- entitysmasher.zoom - Allows scroll to change distance

---

### Commands
- `/es` — Deselect the holding entity
- `/es reload` — Reloads the config (OP only)

---

### To-Do

- [ ] /es player [nick] - Instant player teleport + auto select
- [ ] /es distance [val] - Set a distance using command

---

### Need Help?
If you have any questions, suggestions, run into an issue, or need help with the config, feel free to join my [Discord server](https://discord.gg/BzhJk3WPCk) ([https://discord.gg/BzhJk3WPCk](https://discord.gg/BzhJk3WPCk))

### Source Code
Check out the source [here](https://github.com/zerofoil/Entity-Smasher) ([https://github.com/zerofoil/Entity-Smasher](https://github.com/zerofoil/Entity-Smasher))

### How to build?
- Download gradle if you haven't done that yet
1. Open CMD in the repo location
2. Type `gradle clean build`
3. Go to build/libs, there should be your Jar file.