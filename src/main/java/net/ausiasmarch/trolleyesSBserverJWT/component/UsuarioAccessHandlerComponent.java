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

import net.ausiasmarch.trolleyesSBserverJWT.repository.UsuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.service.AuthenticatedUserService;
import org.springframework.stereotype.Component;

@Component("UsuarioAccessHandlerComponent")
public class UsuarioAccessHandlerComponent extends AccessHandlerAbstractImplementation {

    public UsuarioAccessHandlerComponent(UsuarioRepository repo, AuthenticatedUserService aus) {
        super(repo, aus);
    }

    @Override
    public boolean canGetOne(int id) {
        return this.getUserRollId() == 1 || this.getUserId() == id;
    }

    @Override
    public boolean canGetSome() {
        return this.getUserRollId() == 1;
    }

    @Override
    public boolean canGetAll() {
        return this.getUserRollId() == 1;
    }

    @Override
    public boolean canGetCount() {
        return this.getUserRollId() == 1;
    }

    @Override
    public boolean canCreate(int id) {
        return this.getUserRollId() == 1;
    }

    @Override
    public boolean canUpdate(int id) {
        return this.getUserRollId() == 1 || this.getUserId() == id;
    }

    @Override
    public boolean canDelete(int id) {
        return this.getUserRollId() == 1 || this.getUserId() == id;
    }

}
