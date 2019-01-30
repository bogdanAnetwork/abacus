# abacus
this is a project sample with an example flexiGrid domain controller, java akka allowing concurrent multi threaded operation 

A modular network controller based on the IETF flexi-grid model "YANG data model for Flexi-Grid Optical Networks", draft-vergara-flexigrid-yang-02

![Alt text](/architecture.png?raw=true " ") 

## Network Controller and device driver: Simplified FSM
Each actor/module has a speciffic role (i.e., Network Driver, Device Driver etc.) and is based on a Finite State Machine (FSM).

![Alt text](/FSM.png?raw=true " ")

##Deployment options: centralized or distributed
In the distributed option, the Device Driver agents acan be initialized in independent and remote JVMs possibly hosted as local VNFs.

![Alt text](/deployment.png?raw=true " ")
