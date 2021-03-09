/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.examples.rest;

import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.authorization.util.Tokens;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.util.Resteasy;
import org.keycloak.events.Errors;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resources.Cors;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class HelloResourceProvider implements RealmResourceProvider {

    private KeycloakSession session;

    public HelloResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces("text/plain; charset=utf-8")
    public String get() {
        String name = session.getContext().getRealm().getDisplayName();
        if (name == null) {
            name = session.getContext().getRealm().getName();
        }
        return "Hello " + name;
    }

    @Override
    public void close() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sso")
    public Response sso() {
        System.out.println("==================== (Custom Provider) ====================");

//        final String HEADER = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFVURIM3NHeU0wdm9sRHAyUG9VbV9PQWxqX1ZoVU1ieUdWd19jYVp6OElBIn0.eyJleHAiOjE2MTQ5NzA4NzgsImlhdCI6MTYxNDk3MDU3OCwianRpIjoiNGRmNzExMGEtMzBjNy00OGUyLWE0MWEtODBkNzRmNzcxN2JiIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL2FsZ2FyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjkxMjVmMDMwLThkZDAtNGQ4YS1hNTM0LTEwYjFlNjFmMjBjNyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwcC1wcm9maWxlLWh0bWw1Iiwic2Vzc2lvbl9zdGF0ZSI6ImU2N2U1N2EwLWQzNGItNGM5ZC04MWFhLTU0MDJiMjU4OTIwNCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MTgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiUmVuYXRvIEZlbGl4IiwicHJlZmVycmVkX3VzZXJuYW1lIjoicmZlbGl4IiwiZ2l2ZW5fbmFtZSI6IlJlbmF0byIsImZhbWlseV9uYW1lIjoiRmVsaXgiLCJlbWFpbCI6InJmZWxpeEByZWRoYXQuY29tIn0.IZupGgTFr9Bzzgxbqw5dKnuKqTgxYGWW7nvSVrdHvxqTJ914A1mWS3GTcpxVysd_sCurujAtnCefpSEVOhp8XgAFazfrLs6T4FdcTPGY8WbdZDxLW6EL8oThCxl95E8-h0mVNi5o0iT9WE52fg9u4WxEgAhuKMCCNfoKxvGokLDU3Y30qP_kw2aTg_tDOz0JXnQnVPoyrNyM5O2yLDcf-mWr78akT2USCr2wItDNUNJ8Ha6oZioyfkNIW3PBOQ2GggMfpSFoXrSrygAA_66-pLGMq3wU2ZkUTNZPnTChBr1hA2a53h799UCqOz_qEmvJb0gyyYVKsNgwZ0S8pHzcjg";
        final HttpHeaders headers = session.getContext().getRequestHeaders();
        final String authorization = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("\n\tauthorization >> " + authorization);
        if (authorization == null) {
            throw new ErrorResponseException(Errors.INVALID_INPUT, "Invalid Authorization", Response.Status.UNAUTHORIZED);
        }
        final String[] value = authorization.split(" ");
        final String accessToken = value[1];
        System.out.println("\n\taccessToken >> " + accessToken);
        final AccessToken token = Tokens.getAccessToken(accessToken, session);

        System.out.println("\n\ttoken >> " + token);

        if (token == null) {
            throw new ErrorResponseException(Errors.INVALID_TOKEN, "Invalid access token", Response.Status.UNAUTHORIZED);
        }

        final RealmModel realm = session.getContext().getRealm();
        final UriInfo uriInfo = session.getContext().getUri();
        final ClientConnection clientConnection = session.getContext().getConnection();

        final UserModel user = session.users().getUserById(token.getSubject(), realm);


        final UserSessionModel userSession = session.sessions().getUserSession(realm, token.getSessionState());

        AuthenticationManager.createLoginCookie(session, realm, user, userSession, uriInfo, clientConnection);
/**
 * ver ser irá funcionar com domínios diferentes
 * foi usado biblioteca INTERNA (quebra!)
 * testar
 */
        System.out.println("==================== (Custom Provider) ====================");

//        final HttpRequest request = Resteasy.getContextData(HttpRequest.class);
//
//        return Cors.add(request, Response.ok("", MediaType.APPLICATION_JSON))
//                .auth()
//                .allowedMethods("GET")
//                .allowedOrigins(token)
//                .build();

        return Response.noContent().build();
    }

//    @OPTIONS
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("sso")
//    public Response preflight() {
//        final HttpRequest request = Resteasy.getContextData(HttpRequest.class);
//
//        return Cors.add(request, Response.ok("", MediaType.APPLICATION_JSON))
//                .auth()
//                .preflight()
//                .allowedMethods("GET", "OPTIONS")
//                .build();
//    }


}
