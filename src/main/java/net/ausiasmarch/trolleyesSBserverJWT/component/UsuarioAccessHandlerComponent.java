/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.trolleyesSBserverJWT.component;

import net.ausiasmarch.trolleyesSBserverJWT.entity.UsuarioEntity;
import net.ausiasmarch.trolleyesSBserverJWT.repository.UsuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.service.AuthenticatedUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component("UsuarioAccessHandlerComponent")
public class UsuarioAccessHandlerComponent extends AccessHandlerAbstractImplementation {

    private final UsuarioRepository oUsuarioRepository;
    private final AuthenticatedUserService oAuthenticatedUserService;

    public UsuarioAccessHandlerComponent(UsuarioRepository repo, AuthenticatedUserService aus) {
        this.oUsuarioRepository = repo;
        this.oAuthenticatedUserService = aus;
    }

    private Long getTokenUserId() {
        User oUser = oAuthenticatedUserService.getAuthenticatedUser();
        UsuarioEntity oUsuarioEntity = oUsuarioRepository.findByLogin(oUser.getUsername());
        return oUsuarioEntity.getId();
    }

    @Override
    public boolean canGetOne(int id) {
        return this.getTokenUserId() == 1 || this.getTokenUserId() == id;
    }

    @Override
    public boolean canGetSome() {
        return this.getTokenUserId() == 1;
    }

    @Override
    public boolean canGetAll() {
        return this.getTokenUserId() == 1;
    }

    @Override
    public boolean canGetCount() {
        return this.getTokenUserId() == 1;
    }

    @Override
    public boolean canCreate(int id) {
        return this.getTokenUserId() == 1;
    }

    @Override
    public boolean canUpdate(int id) {
        return this.getTokenUserId() == 1 || this.getTokenUserId() == id;
    }

    @Override
    public boolean canDelete(int id) {
        return this.getTokenUserId() == 1 || this.getTokenUserId() == id;
    }

}
