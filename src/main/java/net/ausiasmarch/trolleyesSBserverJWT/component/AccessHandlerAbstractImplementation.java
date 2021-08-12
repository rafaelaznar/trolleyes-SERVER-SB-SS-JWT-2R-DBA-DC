/*
 * Copyright (c) 2021
 *
 * by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com) & DAW students
 *
 * TROLLEYES: Free Open Source Shopping Site
 *
 * Sources at:                https://github.com/rafaelaznar
 *
 * TROLLEYES is distributed under the MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.ausiasmarch.trolleyesSBserverJWT.component;

import net.ausiasmarch.trolleyesSBserverJWT.entity.UsuarioEntity;
import net.ausiasmarch.trolleyesSBserverJWT.repository.UsuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.service.AuthenticatedUserService;
import org.springframework.security.core.userdetails.User;

public abstract class AccessHandlerAbstractImplementation implements AccessHandlerInterface {

    private final UsuarioRepository oUsuarioRepository;
    private final AuthenticatedUserService oAuthenticatedUserService;

    public AccessHandlerAbstractImplementation(UsuarioRepository repo, AuthenticatedUserService aus) {
        this.oUsuarioRepository = repo;
        this.oAuthenticatedUserService = aus;
    }

    protected Long getUserId() {
        User oUser = oAuthenticatedUserService.getAuthenticatedUser();
        UsuarioEntity oUsuarioEntity = oUsuarioRepository.findByLogin(oUser.getUsername());
        return oUsuarioEntity.getId();
    }

    protected Long getUserRollId() {
        User oUser = oAuthenticatedUserService.getAuthenticatedUser();
        UsuarioEntity oUsuarioEntity = oUsuarioRepository.findByLogin(oUser.getUsername());
        return oUsuarioEntity.getTipousuario().getId();
    }

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

    @Override
    public boolean goOn() {
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public boolean isAdmin() {
        return getUserRollId() == 1;
    }

}
