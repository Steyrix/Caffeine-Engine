### Caffeine Engine

This project is my humble playground to test out interesting gamedev patterns I run into while browsing the net.

However, I aim to create usable simple engine, that will, hopefully, allow me to create basic games and gain overall experience in gamedev.

My first iteration of engine-creating can be seen in simple-jogl-engine repository, which is based on JOGL,
his project, on the other hand, will use LWJGL OpenGL binding, since the latter is not as dead as the former.

#### Used technologies: 
- OpenGL
- GLFW

## TODO List:
- Encapsulate initializations of LabyrinthDemo :white_check_mark:
- Wrap shader uniforms (create some kind of class for it)
- Think of some kind of collision which interactions will be observed :white_check_mark:
- Think of storing animation frame in a set of parameters :white_check_mark:
- Preprocess tiled map to implement simple AI :white_check_mark:
- Improve composite entity pattern :white_check_mark:
- Implement continuous collision detection for bounding box based collision contexts
- Implement several bounding box collision detection algorithms
- Refactor Tiled Map, provide the ability to change, replace specific tiles for future rerendering :white_check_mark:
- Use parameters data classes instances only in the scope of game object, since it can be hard and messy to perform operation on static entities

## AI TODO List:
- Think of how to determine the vector of movement from tile to tile in a given path :white_check_mark:
- Implement sliding window for representing a path as a graph, each vertex of which can represent multiple tiles (not needed)
- Implement path traversing behavior, which will be responsible for object moving between graph vertices :white_check_mark:
- Shortest path finding algorithm :white_check_mark:
- Fix retraversing graph nodes in TileTraverser :white_check_mark:
- Fix interaction between TileTraverser and BoundingBoxCollider (not needed)
