# graphics

Provides implementation-agnostic rendering abstractions.
Contains mutually-exclusive submodules actually implementing the rendering stack.
Currently, only has an `OpenGL` implementation.

It is not necessary to add a direct dependency on this module - prefer an implementation submodule.
