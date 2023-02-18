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
- Implement speculative collider
- Think of storing animation frame in a set of parameters :white_check_mark:
- Preprocess tiled map to implement simple AI 
- Improve composite entity pattern :white_check_mark:

## AI TODO List:
- Think of how to determine the vector of movement from tile to tile in a given path
- Implement sliding window for representing a path as a graph, each vertex of which can represent multiple tiles
- Implement path traversing behavior, which will be responsible for object moving between graph vertices
- Shortest path finding algorithm (should use Dijkstra)
