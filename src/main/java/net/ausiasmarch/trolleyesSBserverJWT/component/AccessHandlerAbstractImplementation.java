
package net.ausiasmarch.trolleyesSBserverJWT.component;


public abstract class AccessHandlerAbstractImplementation implements AccessHandlerInterface {

    @Override
    public boolean canGetOne(int id) {
        return false;
    }

    @Override
    public boolean canGetSome() {
        return false;
    }

    @Override
    public boolean canGetAll() {
        return false;
    }

    @Override
    public boolean canGetCount() {
        return false;
    }

    @Override
    public boolean canCreate(int id) {
        return false;
    }

    @Override
    public boolean canUpdate(int id) {
        return false;
    }

    @Override
    public boolean canDelete(int id) {
        return false;
    }

}
