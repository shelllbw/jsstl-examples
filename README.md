# jSSTL Case Studies
This repository contains some of examples of use of [jSSTL](https://github.com/Quanticol/jsstl/). A detailed description of the case studies is available [here](https://arxiv.org/abs/1706.09334). To run the examples you have first to install jSST (see the instructions available [here](https://github.com/Quanticol/jsstl/blob/master/README.md).

After that you can download the jsst-example folder either by cloning the repository:

'''
git clone https://github.com/Quanticol/jsstl-examples.git
'''

Or by downloading the [zip archive](https://github.com/Quanticol/jsstl-examples/archive/master.zip).

Inside the dowloaded floder you will find two directorie:
- 'pattern': with the code that can be used for studying Turing patters;
- 'bss': with the code for analysing a Bike Sharing System. 

## Studying Turing patters with jSSTL.

This code shows how [jSSTL](https://github.com/Quanticol/jsstl/) can be used to study pattern formation in a reaction-diffusion system. These patterns are formalised in terms of spatial and spatio-temporal properties in SSTL.

Alan Turing [conjectured](http://www.jstor.org/stable/92463?seq=1#page_scan_tab_contents) that pattern formation is a consequence of the coupling of reaction and diffusion phenomena involving different chemical species, and that these can be described by a set of PDE reaction-diffusion equations, one for each species.The natural analogue, systems of agents interacting and moving in continuous space, is however prohibitively expensive to analyse computationally; an approach that is more amenable to analysis is to discretise space into a number of cells which are assumed to be spatially homogeneous, and to replace spatial diffusion with transitions between different cells.

From the point of view of formal verification, the formation of patterns is an inherently spatio-temporal phenomenon, in that the relevant issue is how the spatial organisation of the system changes over time.

### Building the classes

### Running the experiments.


## Bike Sharing System
This example shows how jSSTL can be used to perform analysis of the London Santander Cycles Hire scheme. This is a bike sharing system, modelled  as a *Population Continuous Time Markov Chain* (PCTMC) with time-dependent rates. We use jSSTL to study a number of spatio-temporal properties of the system and to explore their robustness considering a set of parameter values for the formulas. 


### Building the classes

### Running the experiments.

After that you have to clone the '''jsstl-examples''' project. 


Dowload trajectories from this [link](http://bit.ly/2EpsAId)





