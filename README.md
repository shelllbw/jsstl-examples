# jsstl-patterns

This code shows how [jSSTL](https://github.com/Quanticol/jsstl/) can be used to study pattern formation in a reaction-diffusion system. These patterns are formalised in terms of spatial and spatio-temporal properties in SSTL.

Alan Turing [conjectured](http://www.jstor.org/stable/92463?seq=1#page_scan_tab_contents) that pattern formation is a consequence of the coupling of reaction and diffusion phenomena involving different chemical species, and that these can be described by a set of PDE reaction-diffusion equations, one for each species.The natural analogue, systems of agents interacting and moving in continuous space, is however prohibitively expensive to analyse computationally; an approach that is more amenable to analysis is to discretise space into a number of cells which are assumed to be spatially homogeneous, and to replace spatial diffusion with transitions between different cells.

From the point of view of formal verification, the formation of patterns is an inherently spatio-temporal phenomenon, in that the relevant issue is how the spatial organisation of the system changes over time.
