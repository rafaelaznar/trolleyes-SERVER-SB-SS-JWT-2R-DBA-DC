/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.trolleyesSBserverJWT.component;

/**
 *
 * @author rafa
 */
public interface AccessHandlerInterface {

    public boolean canGetOne(int id);

    public boolean canGetSome();

    public boolean canGetAll();

    public boolean canGetCount();

    public boolean canCreate(int id);

    public boolean canUpdate(int id);

    public boolean canDelete(int id);
}
