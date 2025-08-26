package it.unicam.cs.ids.filieraagricola.system;

/*
* STRATEGY INTERFACE
* Every "system action" implements this contract and can be executed
* in the same way by the dispatcher
*
 */

public interface SystemAction {
     void handleRequest(FormatRequest request);
}
