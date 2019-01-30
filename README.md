# abacus
This code is part of a project representing a WDM flexi-grid optical network domain controller; java akka allows for concurrent multi-threaded operation and good scaling capabilities.

The network controller is based on the IETF flexi-grid model "YANG data model for Flexi-Grid Optical Networks", draft-vergara-flexigrid-yang-02. The modular structure is shown below:

![Alt text](/architecture.PNG?raw=true " ")

## Network controller and device driver FSM
Each actor/module has a speciffic role (i.e., Network Controller, Device Driver etc.) and is based on a Finite State Machine (FSM).

![Alt text](/FSM.PNG?raw=true " ")

## Deployment options: centralized or distributed
In the distributed option, the Device Driver agents can be initialized in independent and remote JVMs, possibly hosted as local VNFs close to the network devices. The major advantage from this approach comes from the SSL encryption provided by the optional Akka underlying remote message exchange that establishes a secure management connection between the central app and remote actors/devices. 

![Alt text](/deployment.PNG?raw=true " ")

Full desciption of this work as well as results of scalability and performance testing is available at:
 https://ieeexplore.ieee.org/document/8406170
