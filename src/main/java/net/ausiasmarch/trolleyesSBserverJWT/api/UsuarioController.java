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
package net.ausiasmarch.trolleyesSBserverJWT.api;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import net.ausiasmarch.trolleyesSBserverJWT.entity.TipousuarioEntity;
import net.ausiasmarch.trolleyesSBserverJWT.entity.UsuarioEntity;
import net.ausiasmarch.trolleyesSBserverJWT.helper.RandomHelper;
import net.ausiasmarch.trolleyesSBserverJWT.repository.TipousuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.repository.UsuarioRepository;
import net.ausiasmarch.trolleyesSBserverJWT.service.FillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    HttpSession oHttpSession;

    @Autowired
    UsuarioRepository oUsuarioRepository;

    @Autowired
    TipousuarioRepository oTipousuarioRepository;

    @Autowired
    FillService oFillService;

    @PreAuthorize("@UsuarioAccessHandlerComponent.canGetOne(#id)")
    //@PreAuthorize("@UsuarioBean.getId()==1")
    //@PreAuthorize("hasRole('ADMIN') or !hasRole('ADMIN') and #authUser.id == #userId")
    //@PreAuthorize("hasPermission(#id, 'usuario', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<?> get_USER(
            @PathVariable(value = "id") Long id,
            Authentication authentication,
            Principal principal,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("-----------------");
        System.out.println("get_USER");
        System.out.println("Name: " + authentication.getName());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Name:" + principal.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("username from user: " + user.getUsername());
        System.out.println("-----------------");

        if (oUsuarioRepository.existsById(id)) {
            return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(id), HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("@UsuarioAccessHandlerComponent.isAdmin()")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        if (oUsuarioRepository.count() <= 1000) {
            return new ResponseEntity<List<UsuarioEntity>>(oUsuarioRepository.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.PAYLOAD_TOO_LARGE);
        }
    }

//    @PreAuthorize("@AccessHandler.canGet(#id)")
//    //@PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/admin/{id}")
//    public ResponseEntity<?> getAdmin(
//            @PathVariable(value = "id") Long id,
//            Authentication authentication,
//            Principal principal,
//            @AuthenticationPrincipal User user
//    ) {
//        System.out.println("get_Admin.. " + id);
//        System.out.println("Name:" + principal.getName());
//        return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(1L), HttpStatus.OK);
//    }
//
//    @PreAuthorize("@AccessHandler.canGet(#id)")
//    //@PreAuthorize("hasRole('USER')")
//    @GetMapping("/user/{id}")
//    public ResponseEntity<?> getUser(
//            @PathVariable(value = "id") Long id,
//            Authentication authentication,
//            Principal principal,
//            @AuthenticationPrincipal User user
//    ) {
//        System.out.println("get_User.. " + id);
//        System.out.println("Name:" + principal.getName());
//        return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(3L), HttpStatus.OK);
//    }
//    @PreAuthorize("@AccessHandler.canGet(#id)")
//    //@PreAuthorize("hasRole('MANAGER')")
//    @GetMapping("/manager/{id}")
//    public ResponseEntity<?> getManager(
//            @PathVariable(value = "id") Long id,
//            Authentication authentication,
//            Principal principal,
//            @AuthenticationPrincipal User user
//    ) {
//        System.out.println("get_Manager.. " + id);
//        System.out.println("Name:" + principal.getName());
//        return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(2L), HttpStatus.OK);
//    }
@PostMapping("/")
        public ResponseEntity<?> create(@RequestBody UsuarioEntity oUsuarioEntityFromRequest) {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) {
                oUsuarioEntityFromRequest.setId(null);
                if (oUsuarioEntityFromRequest.getPassword() == null) {
                    //pass allways sha256 encoded
                    oUsuarioEntityFromRequest.setPassword("da8ab09ab4889c6208116a675cad0b13e335943bd7fc418782d054b32fdfba04"); //trolleyes
                }
                oUsuarioEntityFromRequest.setToken(RandomHelper.getRandomHexString(20));
                oUsuarioEntityFromRequest.setActivo(false);
                oUsuarioEntityFromRequest.setValidado(false);
                if (oUsuarioEntityFromRequest.getDescuento() == null) {
                    oUsuarioEntityFromRequest.setDescuento(0);
                }
                return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.save(oUsuarioEntityFromRequest), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("/count")
        public ResponseEntity<?> count() {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) { //administrador
                return new ResponseEntity<Long>(oUsuarioRepository.count(), HttpStatus.OK);
            } else {  //cliente
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PostMapping("/fill/{amount}")
        public ResponseEntity<?> fill(@PathVariable(value = "amount") Long amount) {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) {
                return new ResponseEntity<Long>(oFillService.usuarioFill(amount), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) { //administrador
                oUsuarioRepository.deleteById(id);
                if (oUsuarioRepository.existsById(id)) {
                    return new ResponseEntity<Long>(id, HttpStatus.NOT_MODIFIED);
                } else {
                    return new ResponseEntity<Long>(0L, HttpStatus.OK);
                }
            } else {  //cliente
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable(value = "id") Long id, @RequestBody UsuarioEntity oUsuarioEntityFromRequest) {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) { //administrador
                if (oUsuarioRepository.existsById(id)) {
                    UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.getOne(id);
                    oUsuarioEntityFromRequest.setPassword(oUsuarioEntityFromDatabase.getPassword());
                    oUsuarioEntityFromRequest.setToken(oUsuarioEntityFromDatabase.getToken());
                    oUsuarioEntityFromRequest.setActivo(oUsuarioEntityFromDatabase.isActivo());
                    oUsuarioEntityFromRequest.setValidado(oUsuarioEntityFromDatabase.isValidado());
                    return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.save(oUsuarioEntityFromRequest), HttpStatus.OK);
                } else {
                    return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.getOne(id), HttpStatus.NOT_FOUND);
                }
            } else {  //cliente
                if (oUsuarioEntityFromSession.getId() == id) {
                    UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.getOne(id);
                    oUsuarioEntityFromRequest.setPassword(oUsuarioEntityFromDatabase.getPassword());
                    oUsuarioEntityFromRequest.setToken(oUsuarioEntityFromDatabase.getToken());
                    oUsuarioEntityFromRequest.setActivo(oUsuarioEntityFromDatabase.isActivo());
                    oUsuarioEntityFromRequest.setValidado(oUsuarioEntityFromDatabase.isValidado());
                    return new ResponseEntity<UsuarioEntity>(oUsuarioRepository.save(oUsuarioEntityFromRequest), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
        }
    }

    @GetMapping("/page")
        public ResponseEntity<?> getPage(@RequestParam("filter") Optional<String> strSearch, @PageableDefault(page = 0, size = 10, direction = Direction.ASC) Pageable oPageable) {
        Page<UsuarioEntity> oPage = null;
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oUsuarioEntityFromSession.getTipousuario().getId() == 1) { //administrador
                if (strSearch.isPresent()) {
                    oPage = oUsuarioRepository.findByDniContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellido1ContainingIgnoreCaseOrApellido2ContainingIgnoreCaseOrLoginContainingIgnoreCase(strSearch.get(), strSearch.get(), strSearch.get(), strSearch.get(), strSearch.get(), oPageable);
                } else {
                    oPage = oUsuarioRepository.findAll(oPageable);
                }
                return new ResponseEntity<Page<UsuarioEntity>>(oPage, HttpStatus.OK);
            } else {  //cliente
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("/page/tipousuario/{id}")
        public ResponseEntity<?> getPageXTipousuario(@PageableDefault(page = 0, size = 10, direction = Direction.ASC) Pageable oPageable, @PathVariable(value = "id") Long id) {
        UsuarioEntity oUsuarioEntityFromSession = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioEntityFromSession == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            if (oTipousuarioRepository.existsById(id)) {
                TipousuarioEntity oTipousuarioEntity = oTipousuarioRepository.getOne(id);
                Page<UsuarioEntity> oPage = oUsuarioRepository.findByTipousuario(oTipousuarioEntity, oPageable);
                return new ResponseEntity<Page<UsuarioEntity>>(oPage, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }
    }

}
