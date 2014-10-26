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

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;

public class LibraryTest {

    static final String IPHOTO_LIBRARY_NAME = "iPhoto-9_4_3";
    static Library library = null;

    @BeforeClass
    public static void readLibrary() throws Exception {
        String file = Thread.currentThread().getContextClassLoader().getResource(IPHOTO_LIBRARY_NAME + ".photolibrary").getFile();
        library = Library.fromPath(file);
        assertThat(library).isNotNull();
    }

    @Test
    public void should_read_library_version() throws Exception {
        assertThat(library.getVersion()).isEqualTo("9.4");
    }

    @Test
    public void should_read_library_albums() throws Exception {
        List<Album> albums = library.getAlbums();
        assertThat(albums).hasSize(5);
        assertThat(albums).onProperty("name").containsExactly("Photos", "Flagged", "12 derniers mois", "Dernière importation",  "JavaOne 11");
        assertThat(albums).onProperty("type").containsExactly("99", "Flagged", "Special Month", "Regular", "Event");
    }

    @Test
    public void should_read_album_by_name() throws Exception {
        Optional<Album> album = library.getAlbum("Photos");
        assertThat(album.isPresent()).isTrue();
        assertThat(album.get().getName()).isEqualTo("Photos");
        assertThat(album.get().getType()).isEqualTo("99");
    }

    @Test
    public void should_return_empty_optional_for_unknown_album() throws Exception {
        Optional<Album> album = library.getAlbum("Photosss");
        assertThat(album.isPresent()).isFalse();
    }

    @Test
    public void should_read_events() throws Exception {
        List<Album> albums = library.getEvents();
        assertThat(albums).hasSize(1);
        assertThat(albums).onProperty("name").containsExactly("JavaOne 11");
        assertThat(albums).onProperty("type").containsExactly("Event");
    }

    @Test
    public void should_read_events_by_name() throws Exception {
        Optional<Album> album = library.getEvent("JavaOne 11");
        assertThat(album.isPresent()).isTrue();
        assertThat(album.get().getName()).isEqualTo("JavaOne 11");
        assertThat(album.get().getType()).isEqualTo("Event");
    }

    @Test
    public void should_return_empty_optional_for_unknown_event() throws Exception {
        Optional<Album> album = library.getEvent("JavaOne 10");
        assertThat(album.isPresent()).isFalse();
    }

    @Test
    public void should_read_photos() throws Exception {
        List<Photo> photos = library.getPhotos();
        assertThat(photos).hasSize(7);
        assertThat(photos).onProperty("caption").containsExactly("P1060169", "P1060170", "P1060174", "P1060252", "P1060584", "P1060587", "P1060605");
        assertThat(photos).onProperty("imagePath").isNotNull();
        assertThat(photos).onProperty("thumbPath").isNotNull();
    }

    @Test
    public void should_read_photo_by_caption() throws Exception {
        Optional<Photo> photo = library.getPhoto("P1060169");
        assertThat(photo.isPresent()).isTrue();
        assertThat(photo.get().getCaption()).isEqualTo("P1060169");
    }

    @Test
    public void should_return_empty_optional_for_unknown_photo() throws Exception {
        Optional<Photo> photo = library.getPhoto("P10601699999");
        assertThat(photo.isPresent()).isFalse();
    }

    @Test
    public void should_read_photos_of_event() throws Exception {
        Optional<Album> album = library.getEvent("JavaOne 11");
        List<Photo> photos = album.get().getPhotos();
        assertThat(photos).hasSize(7);
        assertThat(photos).onProperty("caption").containsExactly("P1060169", "P1060170", "P1060174", "P1060252", "P1060584", "P1060587", "P1060605");
        assertThat(photos).onProperty("imagePath").isNotNull();
        assertThat(photos).onProperty("thumbPath").isNotNull();
    }
}
