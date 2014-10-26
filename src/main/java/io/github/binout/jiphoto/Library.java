/*
 * Copyright 2013 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.binout.jiphoto;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library extends Portofolio {

    private String version;
    private List<Album> albums = new LinkedList<>();

    public static Library fromPath(String path) throws Exception {
        return LibraryReader.read(path);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public Optional<Album> getAlbum(final String name) {
        return albums.stream().filter(a -> name.equals(a.getName())).findFirst();
    }

    public List<Album> getEvents() {
        return events().collect(Collectors.toList());
    }

    private Stream<Album> events() {
        return albums.stream().filter(a -> "Event".equals(a.getType()));
    }

    public Optional<Album> getEvent(final String name) {
        return events().filter(a -> name.equals(a.getName())).findFirst();
    }

}
