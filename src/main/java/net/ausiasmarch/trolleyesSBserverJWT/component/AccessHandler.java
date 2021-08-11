/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * https://www.mscharhag.com/spring/security-authorization-bean-methods
 */
package net.ausiasmarch.trolleyesSBserverJWT.component;

import java.util.Optional;
import net.ausiasmarch.trolleyesSBserverJWT.entity.UsuarioEntity;
import net.ausiasmarch.trolleyesSBserverJWT.repository.UsuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.service.AuthenticatedUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component("AccessHandler")
public class AccessHandler {

    private final UsuarioRepository oUsuarioRepository;
    private final AuthenticatedUserService oAuthenticatedUserService;

    public AccessHandler(UsuarioRepository repo, AuthenticatedUserService aus) {
        this.oUsuarioRepository = repo;
        this.oAuthenticatedUserService = aus;
    }
    
    public boolean canGet(int id) {
        System.out.println("----------canGet--------");
        System.out.println("get_User.. " + id);
        System.out.println("Name:" + oAuthenticatedUserService.getAuthenticatedUser().getUsername());
        return true;
    }
    
    public boolean canUpdate(int id) {
        return isOwner(id);
    }

    public boolean canDelete(int id) {
        return isOwner(id);
    }

    private boolean isOwner(int id) {
        User oUser = oAuthenticatedUserService.getAuthenticatedUser();
        Optional<UsuarioEntity> oUsuarioEntity = oUsuarioRepository.findById(Long.valueOf(id));
        if (oUsuarioEntity.isPresent()) {
            return (oUsuarioEntity.get().getLogin() == oUser.getUsername());
        } else {
            return false;
        }
    }
}
