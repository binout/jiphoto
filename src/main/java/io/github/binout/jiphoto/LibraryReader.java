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

import com.dd.plist.*;

import java.io.File;
import java.util.*;

class LibraryReader {

    static Library read(String path) throws Exception {
        Library library = new Library();

        File albumData = new File(path, "AlbumData.xml");
        NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(albumData);
        String version = rootDict.objectForKey("Application Version").toString();
        library.setVersion(version);

        Map<String, Photo> photosByKey = collectPhotos(rootDict);
        for (Map.Entry<String, Photo> entry : photosByKey.entrySet()) {
            library.addPhoto(entry.getValue());
        }

        NSObject[] nsAlbums = ((NSArray)rootDict.objectForKey("List of Albums")).getArray();
        for (NSObject nsAlbum : nsAlbums) {
            Album album = new Album();
            NSDictionary nsDictAlbum = (NSDictionary) nsAlbum;

            NSObject albumName = nsDictAlbum.objectForKey("AlbumName");
            album.setName(albumName == null ? "" : albumName.toString());

            NSObject albumType = nsDictAlbum.objectForKey("Album Type");
            album.setType(albumType == null ? "" : albumType.toString());

            NSArray photosArray = (NSArray) nsDictAlbum.objectForKey("KeyList");
            NSObject[] nsPhotos = photosArray.getArray();
            for (NSObject nsPhoto : nsPhotos) {
                NSString photoKey = (NSString) nsPhoto;
                Photo photo = photosByKey.get(photoKey.toString());
                album.addPhoto(photo);
            }

            library.addAlbum(album);
        }
        return library;
    }

    private static Map<String, Photo> collectPhotos(NSDictionary rootDict) {
        Map<String, Photo> photoByKey = new LinkedHashMap<>();
        NSDictionary nsDictPhotos = (NSDictionary)rootDict.objectForKey("Master Image List");
        Set<Map.Entry<String,NSObject>> entries = nsDictPhotos.entrySet();
        for (Map.Entry<String,NSObject> entry : entries) {
            Photo photo = new Photo();
            NSDictionary nsDictPhoto = (NSDictionary) entry.getValue();

            NSObject photoCaption = nsDictPhoto.objectForKey("Caption");
            photo.setCaption(photoCaption == null ? "" : photoCaption.toString());

            NSObject imagePath = nsDictPhoto.objectForKey("ImagePath");
            photo.setImagePath(imagePath == null ? "" : imagePath.toString());

            NSObject thumbPath = nsDictPhoto.objectForKey("ThumbPath");
            photo.setThumbPath(thumbPath == null ? "" : thumbPath.toString());

            photoByKey.put(entry.getKey(), photo);
        }
        return photoByKey;
    }
}
