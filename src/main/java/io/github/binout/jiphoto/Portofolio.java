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

public abstract class Portofolio {

    private List<Photo> photos = new LinkedList<>();

    public List<Photo> getPhotos() {
        return photos;
    }

    public Optional<Photo> getPhoto(final String caption) {
        return photos.stream().filter(p -> caption.equals(p.getCaption())).findFirst();
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }
}
