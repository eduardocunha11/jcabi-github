/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.rexsl.test.Request;
import lombok.EqualsAndHashCode;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Github milestone.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.5
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = {"entry", "request", "owner" })
public class GhMilestone implements Milestone {

    /**
     * API entry point.
     */
    private final transient Request entry;

    /**
     * RESTful request.
     */
    private final transient Request request;

    /**
     * Repository.
     */
    private final transient Repo owner;

    /**
     * Milestone number.
     */
    private final transient int num;

    /**
     * Public ctor.
     *
     * @param req    Request
     * @param repo   Repository
     * @param number Number of the get
     */
    GhMilestone(final Request req, final Repo repo, final int number) {
        this.entry = req;
        final Coordinates coords = repo.coordinates();
        this.request = this.entry.uri()
                .path("/repos")
                .path(coords.user())
                .path(coords.repo())
                .path("/milestones")
                .path(Integer.toString(number))
                .back();
        this.owner = repo;
        this.num = number;
    }

    @Override
    public final String toString() {
        return this.request.uri().get().toString();
    }

    @Override
    public final Repo repo() {
        return this.owner;
    }

    @Override
    public final int number() {
        return this.num;
    }

    @Override
    public final JsonObject json() throws IOException {
        return new GhJson(this.request).fetch();
    }

    @Override
    public final void patch(
            @NotNull(message = "JSON object can't be NULL")
            final JsonObject json) throws IOException {
        new GhJson(this.request).patch(json);
    }

    @Override
    public final int compareTo(
            @NotNull(message = "Milestone object can't be NULL")
            final Milestone milestone) {
        return new Integer(this.number()).compareTo(milestone.number());
    }
}
